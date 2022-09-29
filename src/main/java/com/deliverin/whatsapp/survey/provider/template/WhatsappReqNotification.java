package com.deliverin.whatsapp.survey.provider.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WhatsappReqNotification implements Serializable {
    private String recipient_type;
    private String messaging_product;
    private String to;
    private Text text;    
}
