package com.deliverin.whatsapp.survey.controller;

import com.deliverin.whatsapp.survey.SurveyApplication;
import com.deliverin.whatsapp.survey.model.dao.WhatsappMessage;
import com.deliverin.whatsapp.survey.model.dao.WhatsappTemplate;
import com.deliverin.whatsapp.survey.model.dto.CustomerSurvey;
import com.deliverin.whatsapp.survey.model.dto.UserSession;
import com.deliverin.whatsapp.survey.provider.component.ButtonParameter;
import com.deliverin.whatsapp.survey.provider.component.Interactive;
import com.deliverin.whatsapp.survey.provider.whatsapp.FacebookCloud;
import com.deliverin.whatsapp.survey.respository.CustomerSurveyRepository;
import com.deliverin.whatsapp.survey.respository.UserPaymentRepository;
import com.deliverin.whatsapp.survey.respository.UserSessionRepository;
import com.deliverin.whatsapp.survey.utils.DateTimeUtil;
import com.deliverin.whatsapp.survey.utils.HttpResponse;
import com.deliverin.whatsapp.survey.utils.Logger;
import com.deliverin.whatsapp.survey.utils.TxIdUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/message")
public class PushMessageController {

    @Autowired
    UserSessionRepository userSessionRepository;

    @Autowired
    UserPaymentRepository userPaymentRepository;

    @Value("${whatsapp.service.order.reminder.templatename:deliverin_survey}")
    private String sendSurvey;

    @Value("${whatsapp.button.reply.prefix:SUR}")
    private String surButtonReplyPrefix;

    @Value("${whatsapp.service.template.url:https://api.symplified.it/whatsapp-java-service/v1/templatemessage/push}")
    private String whatsappServiceUrl;


    @Autowired
    CustomerSurveyRepository customerSurveyRepository;


    @PostMapping(path = {"/survey/webhook"}, name = "push-template-message-post")
    public ResponseEntity<HttpResponse> webhook(HttpServletRequest request, @RequestBody JsonObject json) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        HttpResponse response = new HttpResponse(request.getRequestURI());

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, URL:  " + json);
        JsonObject jsonResp = new Gson().fromJson(String.valueOf(json), JsonObject.class);
        JsonObject entry = jsonResp.get("entry").getAsJsonArray().get(0).getAsJsonObject();
        JsonObject changes = entry.get("changes").getAsJsonArray().get(0).getAsJsonObject();

        //user input : {"from":"60133731869","id":"wamid.HBgLNjAxMzM3MzE4NjkVAgASGBQzRUIwQkI3Q0FCRjIwQjNEMjg4OQA=","timestamp":"1660023849","text":{"body":"hello"},"type":"text"}
        //user select from list : {"context":{"from":"60125063299","id":"wamid.HBgLNjAxMzM3MzE4NjkVAgARGBJGQkVGQURCODBDRDQ0OUQ4M0IA"},"from":"60133731869","id":"wamid.HBgLNjAxMzM3MzE4NjkVAgASGBQzRUIwMDAxOURCMDk1RDhGNjk4QwA=","timestamp":"1660024660","type":"interactive","interactive":{"type":"list_reply","list_reply":{"id":"b4b3fac1-f593-4dff-ad64-2ad532cf4724","title":"Brew Coffee"}}}

        JsonObject messages = null;
        try {
            messages = changes.get("value").getAsJsonObject().get("messages").getAsJsonArray().get(0).getAsJsonObject();
        } catch (Exception ex) {
            //not a message
            Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, not a message");
            JsonObject statuses = changes.get("value").getAsJsonObject().get("statuses").getAsJsonArray().get(0).getAsJsonObject();
            Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, receive a status : " + statuses.toString());
            response.setSuccessStatus(HttpStatus.OK);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, MessageBody: " + messages);

        JsonObject context = null;
        try {
            context = messages.get("context").getAsJsonObject();
        } catch (Exception ex) {
        }

        WhatsappMessage messageBody = new WhatsappMessage();
        String phone = null;
        String userInput = null;
        String type = null;
        String replyTitle = null;
        String replyId = null;
        Boolean sendSurvey;

        if (context != null) {
            //user reply
            phone = messages.get("from").getAsString();
            type = messages.get("type").getAsString();

            if (type.equals("interactive")) {//interactive
                JsonObject interactive = messages.get("interactive").getAsJsonObject();
                String interactiveType = interactive.get("type").getAsString();
                if (interactiveType.equals("list_reply")) {
                    JsonObject listReply = interactive.get("list_reply").getAsJsonObject();
                    replyId = listReply.get("id").getAsString();
                    replyTitle = listReply.get("title").getAsString();
                } else if (interactiveType.equals("button_reply")) {
                    JsonObject listReply = interactive.get("button_reply").getAsJsonObject();
                    replyId = listReply.get("id").getAsString();
                    replyTitle = listReply.get("title").getAsString();
                }
            }
            List<String> mp = new ArrayList<>();
            mp.add(phone);
            messageBody.setRecipientIds(mp);
        } else {
            //user input
            type = "input";
            phone = messages.get("from").getAsString();
            userInput = messages.get("text").getAsJsonObject().get("body").getAsString();
            List<String> mp = new ArrayList<>();
            mp.add(phone);
            messageBody.setRecipientIds(mp);
        }

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Incoming message. Msisdn:" + phone + " UserInput:" + userInput);

        List<Object[]> userSession = userSessionRepository.findActiveSession(phone);
        Interactive interactiveMsg = null;
        if (!userSession.isEmpty()) {
            //get stage
            Optional<UserSession> sessionOpt = userSessionRepository.findById(phone);
            UserSession session = sessionOpt.get();

            if (userInput != null && userInput.equals("00")) {
                //main menu
                Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Show main menu");
                session.setStage(0);
                session.setExpiry(DateTimeUtil.expiryTimestamp(120));
                userSessionRepository.save(session);
                interactiveMsg = SessionController.GenerateResponseMessage(phone, 0, userInput);
            } else if (userInput != null && userInput.equals("99")) {
                //view cart
                Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "View cart");
                interactiveMsg = SessionController.UserViewCart(session.getCartId());
                session.setExpiry(DateTimeUtil.expiryTimestamp(120));
                userSessionRepository.save(session);
            } else if (userInput != null && userInput.equals("88")) {
                //checkout
                Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Checkout");
                interactiveMsg = SessionController.Checkout(session.getCartId());
                session.setExpiry(DateTimeUtil.expiryTimestamp(120));
                userSessionRepository.save(session);
            } else {
                int stage = session.getStage();
                if (type.equals("input")) {
                    if (stage == 0) {
                        interactiveMsg = SessionController.GenerateResponseMessage(phone, stage, userInput);
                        session.setStage(1);
                    } else if (stage == 1) {
                        interactiveMsg = SessionController.GenerateResponseMessage(phone, stage, userInput);
                        session.setStage(2);
                    } else if (stage == 2) {
                        //generate payment form
                        session.setStage(3);
                        session.setEmail(userInput);
                        session.setExpiry(DateTimeUtil.expiryTimestamp(120));
                        userSessionRepository.save(session);

                        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Send message completed");
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                } else {
                    stage = 0;
                    session.setStage(stage);
                    interactiveMsg = SessionController.GenerateResponseMessage(phone, stage, userInput);
                    if (replyId != null && replyId.startsWith("SUR_")) {
                        interactiveMsg = SessionController.GenerateResponseMessage(phone, stage, userInput);
                    }
                }
                session.setExpiry(DateTimeUtil.expiryTimestamp(120));
                userSessionRepository.save(session);
            }
        } else {
            //generate new cart
            //generate new session
            UserSession session = new UserSession();
            session.setMsisdn(phone);
            session.setStage(0);
            session.setExpiry(DateTimeUtil.expiryTimestamp(120));
            userSessionRepository.save(session);
//            interactiveMsg = SessionController.GenerateResponseMessage(phone, 0, userInput);
        }

        try {
            FacebookCloud.sendInteractiveMessage(messageBody, interactiveMsg);
            response.setSuccessStatus(HttpStatus.CREATED);
        } catch (Exception exp) {
            Logger.application.error(Logger.pattern, SurveyApplication.VERSION, logprefix, "Error sending message : ", exp);
            response.setMessage(exp.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Send message completed");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = {"/survey/webhook"}, name = "push-template-message-post")
    public ResponseEntity<HttpResponse> verifyWebHook(HttpServletRequest request, @RequestParam(name = "hub.mode") String mode, @RequestParam(name = "hub.challenge") String challenge, @RequestParam(name = "hub.verify_token") String token) throws Exception {
        String logprefix = request.getRequestURI() + " ";
        HttpResponse response = new HttpResponse(request.getRequestURI());

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, URL:  " + request.getRequestURI());

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, messageBody: " + mode);
        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, Message Text : " + token);

        JsonObject object = new JsonObject();
        object.addProperty("hub.challenge", challenge);
        response.setData(object);

        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Send message completed");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public void pushSurvey() {
        String logprefix = "PUSH_MESSAGE_THREAD ";

        List<CustomerSurvey> customerSurveys = customerSurveyRepository.findAll();
        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "Send message ");

        for (CustomerSurvey customer : customerSurveys) {
            if (!customer.getSentSurvey()) {
                String transaction = TxIdUtil.generateReferenceId("SUR");

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                WhatsappMessage request = new WhatsappMessage();
                request.setGuest(false);
                request.setRecipientIds(Collections.singletonList(customer.getCustomerPhone()));
                request.setRefId(customer.getCustomerPhone());
                request.setReferenceId(transaction);
                request.setOrderId(transaction);
                request.setMerchantToken("");

                WhatsappTemplate template = new WhatsappTemplate();
                template.setName("deliverin_survey");
                String[] message = {customer.getCustomerName()};

                template.setParameters(message);

                ButtonParameter[] buttonParameters = new ButtonParameter[2];
                ButtonParameter buttonParameter1 = new ButtonParameter();
                buttonParameter1.setIndex(0);
                buttonParameter1.setSub_type("quick_reply");
                String[] params = {surButtonReplyPrefix + "_SURE"};
                buttonParameter1.setParameters(params);
                buttonParameters[0] = buttonParameter1;
                ButtonParameter buttonParameter2 = new ButtonParameter();
                buttonParameter2.setIndex(1);
                buttonParameter2.setSub_type("quick_reply");
                String[] params2 = {surButtonReplyPrefix + "_NOPE"};
                buttonParameter2.setParameters(params2);
                buttonParameters[1] = buttonParameter2;
                template.setButtonParameters(buttonParameters);

                request.setTemplate(template);
                HttpEntity<WhatsappMessage> httpEntity = new HttpEntity<>(request, headers);
                Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "url: " + whatsappServiceUrl, "");
                Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "httpEntity: " + httpEntity, "");

                try {
                    ResponseEntity<String> res = restTemplate.postForEntity(whatsappServiceUrl, httpEntity, String.class);

                    if (res.getStatusCode() == HttpStatus.ACCEPTED || res.getStatusCode() == HttpStatus.OK) {
                        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "res: " + res.getBody(), "");
                    } else {
                        Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "could not send Survey res: " + res, "");
                    }

                } catch (Exception ex) {
                    Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "could not send Survey res: " + ex.getMessage(), "");
                }

            }
        }
    }


}
