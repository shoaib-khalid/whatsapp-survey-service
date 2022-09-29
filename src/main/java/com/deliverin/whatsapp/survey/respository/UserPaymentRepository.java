package com.deliverin.whatsapp.survey.respository;

import com.deliverin.whatsapp.survey.model.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sarosh
 */
@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, String>, PagingAndSortingRepository<UserPayment, String> {
 
}
