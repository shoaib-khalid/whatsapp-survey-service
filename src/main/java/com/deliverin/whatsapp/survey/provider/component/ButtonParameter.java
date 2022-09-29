package com.deliverin.whatsapp.survey.provider.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ButtonParameter implements Serializable {
    private String sub_type;
    private Integer index;
    private String[] parameters;
}