package com.seahere.backend.incoming.repository;

import com.seahere.backend.incoming.entity.IncomingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IncomingJpaRepository extends JpaRepository<IncomingEntity, Long> {

}
