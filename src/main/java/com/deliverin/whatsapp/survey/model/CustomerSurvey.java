package com.deliverin.whatsapp.survey.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity
@Table(name = "customer_survey")
public class CustomerSurvey {

    @Id
    String customerPhone;

    String customerName;
    Boolean sentSurvey;


}
