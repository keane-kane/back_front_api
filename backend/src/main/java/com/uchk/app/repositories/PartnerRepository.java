package com.uchk.app.repositories;

import com.uchk.app.models.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    
    List<Partner> findByType(Partner.PartnerType type);
    
    @Query("SELECT p FROM Partner p WHERE p.partnershipEndDate IS NULL OR p.partnershipEndDate >= CURRENT_DATE")
    List<Partner> findActivePartners();
    
    @Query("SELECT p FROM Partner p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Partner> search(@Param("query") String query);
}
