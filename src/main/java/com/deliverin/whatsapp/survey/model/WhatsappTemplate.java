package com.deliverin.whatsapp.survey.model;

import com.deliverin.whatsapp.survey.provider.component.ButtonParameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class WhatsappTemplate { 
    private String[] parameters;
    private String name;  
    private String[] parametersButton;

    private String[] parametersHeader;
    private ButtonParameter[] buttonParameters; //new field, more customize
    private String parametersDocument;
    private String parametersDocumentFileName;

}