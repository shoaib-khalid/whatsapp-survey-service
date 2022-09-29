package com.deliverin.whatsapp.survey.provider.component;

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
public class BroadcastTemplate {

    private String parameters;
    private String[] parametersHeader;
    private String[] parametersButton;
    private ButtonParameter[] buttonParameters; //new field, more customize
    private String parametersDocument;
    private String parametersDocumentFileName;
    private String name;

}
