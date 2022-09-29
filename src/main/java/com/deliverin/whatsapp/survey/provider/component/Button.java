package com.deliverin.whatsapp.survey.provider.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Button {
    private String type;
    private Reply reply;
    
    public Button() {
        this.type="reply";
    }
    
     public Button(Reply reply) {
        this.type="reply";
        this.reply=reply;
    }
}
