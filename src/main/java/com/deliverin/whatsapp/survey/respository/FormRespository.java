package com.deliverin.whatsapp.survey.respository;

import com.deliverin.whatsapp.survey.model.dto.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRespository extends JpaRepository<Form, Long> {

    Form findByActive(Boolean active);
}
