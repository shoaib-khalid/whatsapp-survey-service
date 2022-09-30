package com.deliverin.whatsapp.survey.respository;

import com.deliverin.whatsapp.survey.model.dto.Form;
import com.deliverin.whatsapp.survey.model.dto.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {


    Questions findByFormIdAndRanking(Long form, Long ranking);
}
