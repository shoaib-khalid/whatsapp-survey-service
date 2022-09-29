package com.deliverin.whatsapp.survey.controller;

import com.deliverin.whatsapp.survey.SurveyApplication;
import com.deliverin.whatsapp.survey.model.dao.WhatsappMessage;
import com.deliverin.whatsapp.survey.utils.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;


@Service
public class PushThread {

    private JSONObject jsonObject;

    HttpServletRequest request;
    @Autowired
    PushMessageController pushMessageController;

    public PushThread() {
    }


    public PushThread(JSONObject object, PushMessageController pushMessageController, HttpServletRequest request) {
        this.jsonObject = object;
        this.pushMessageController = pushMessageController;
        this.request = request;
    }

    @Scheduled(cron = "${delivery-service:0 0/02" +
            "" +
            "" +
            " * * * ?}")
    public void dailyScheduler() throws ParseException {
        try {

            String logprefix = "PUSH THREAD ";
//            Logger.application.info(Logger.pattern, SurveyApplication.VERSION, logprefix, "callback-message-get, test: " + this.jsonObject);
            WhatsappMessage messageBody = new WhatsappMessage();
            try {
                pushMessageController.pushSurvey();
            } catch (Exception e) {
                Logger.application.error(Logger.pattern, SurveyApplication.VERSION, logprefix, "Exception: ", e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("e ::" + e.getMessage());

            throw new RuntimeException(e);
        }

    }
}
