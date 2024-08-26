package com.seahere.backend.company.repository;

import com.seahere.backend.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
    Optional<CompanyEntity> findByRegistrationNumber(String registrationNumber);
    List<CompanyEntity> findByCompanyName(String companyName);
}