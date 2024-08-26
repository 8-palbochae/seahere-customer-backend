package com.seahere.backend.outgoing.repository;

import com.seahere.backend.outgoing.entity.OutgoingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OutgoingJpaRepository extends JpaRepository<OutgoingEntity, Long> {

    @Query("select o from OutgoingEntity o join fetch o.company c where o.outgoingId = :outgoingId")
    Optional<OutgoingEntity> findByIdFetchCompany(@Param("outgoingId") Long outgoingId);

    List<OutgoingEntity> findByCustomerIdAndOutgoingDate(Long customerId, LocalDate outgoingDate);

}
