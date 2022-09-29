/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deliverin.whatsapp.survey.provider.template;

import com.deliverin.whatsapp.survey.provider.component.ButtonParameter;
import com.deliverin.whatsapp.survey.provider.component.Component;
import com.deliverin.whatsapp.survey.provider.component.Language;
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
public class Template implements Serializable {
    private String name;
    private Language language;
    private Component[] components;

    private String[] parameters;
    private ButtonParameter[] buttonParameters; //new field, more customize
    private String[] parametersHeader;

}
