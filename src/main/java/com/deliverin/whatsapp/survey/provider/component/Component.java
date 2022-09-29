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
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Component implements Serializable {
    private String type;
    private Parameter[] parameters;
    private String sub_type;
    private Integer index;
}
