package com.deliverin.whatsapp.survey.respository;


import com.deliverin.whatsapp.survey.model.dto.Options;
import com.deliverin.whatsapp.survey.model.dto.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Options, Long> {

    List<Options> findByQuestionId(Long questionId);

    Options findByQuestionIdAndOption(Long questionId, String option);

}
