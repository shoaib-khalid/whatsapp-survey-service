package com.deliverin.whatsapp.survey.respository;

import com.deliverin.whatsapp.survey.model.dto.CustomerSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSurveyRepository extends JpaRepository<CustomerSurvey, String>, PagingAndSortingRepository<CustomerSurvey, String> {
}
