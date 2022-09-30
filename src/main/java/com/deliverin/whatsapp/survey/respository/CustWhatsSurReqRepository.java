package com.deliverin.whatsapp.survey.respository;

import com.deliverin.whatsapp.survey.model.dto.CustWhatsSurReq;
import com.deliverin.whatsapp.survey.model.dto.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustWhatsSurReqRepository extends JpaRepository<CustWhatsSurReq, Long> {

    List<CustWhatsSurReq> findAllByCustomerPhoneAndFormId(String phoneNo, Long formId);

    CustWhatsSurReq findByCustomerPhoneAndFormIdAndQuestionIdAndStage(String customerPhone, Long formId, Long questions, Integer stage);

}
