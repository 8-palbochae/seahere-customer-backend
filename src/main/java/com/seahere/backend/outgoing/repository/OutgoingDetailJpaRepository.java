package com.seahere.backend.outgoing.repository;

import com.seahere.backend.outgoing.entity.OutgoingDetailEntity;
import com.seahere.backend.outgoing.entity.OutgoingDetailState;
import com.seahere.backend.outgoing.entity.OutgoingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutgoingDetailJpaRepository extends JpaRepository<OutgoingDetailEntity , Long> {

    @Modifying
    @Query("update OutgoingDetailEntity o set o.state = :state where o.outgoing = :outgoing")
    void updateByOutgoingDetailStateToActive(@Param("outgoing")OutgoingEntity outgoing, @Param("state")OutgoingDetailState state);

    List<OutgoingDetailEntity> findByOutgoingAndState(OutgoingEntity outgoingEntity, OutgoingDetailState state);
}

