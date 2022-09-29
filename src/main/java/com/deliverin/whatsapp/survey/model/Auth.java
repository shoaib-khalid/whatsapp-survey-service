package com.deliverin.whatsapp.survey.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This model us used for authetication and authrization
 *
 * @author Sarosh
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Auth {

    private Session session;

    private String role;
    private List<String> authorities;

    @JsonCreator
    public Auth(@JsonProperty("session") Session session, 
            @JsonProperty("role") String role,  
            @JsonProperty("authorities") List<String> authorities) {
        this.session = session;
        this.role = role;
        this.authorities = authorities;
    }

}
