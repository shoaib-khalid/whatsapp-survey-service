/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deliverin.whatsapp.survey.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author taufik
 */

@Entity
@Getter
@Setter
@Table(name = "payment")
@ToString
public class UserPayment {
    
    @Id
    private String paymentOrderId;
   
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updated;
       
    private String name;
    
    private String email;
    
    private String paymentDetail;
    
    private String paymentHash;
    
    private Double paymentAmount;
    
    private String paymentLink;
    
    private String msisdn;
       
        
}
