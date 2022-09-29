package com.deliverin.whatsapp.survey.respository;

import com.deliverin.whatsapp.survey.model.dto.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Sarosh
 */
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String>, PagingAndSortingRepository<UserSession, String> {
    
    @Query(value = "SELECT stage "
            + "FROM `user_session` "
            + "WHERE msisdn=:userMsisdn "
            + "AND expiry> NOW()", nativeQuery = true)
    List<Object[]> findActiveSession(@Param("userMsisdn") String userMsisdn);
    
}
