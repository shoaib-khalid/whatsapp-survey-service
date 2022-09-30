package com.deliverin.whatsapp.survey.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "form")
@ToString
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Long noQuestion;
    Boolean active;
}
