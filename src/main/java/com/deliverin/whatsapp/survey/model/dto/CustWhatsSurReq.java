package com.deliverin.whatsapp.survey.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "custWhatsSurReq")
@ToString
public class CustWhatsSurReq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String customerPhone;

    Long formId;

    Long questionId;

    Date created;
    Integer stage;
    Long optionId;

}
