package com.seahere.backend.company.service;

import com.seahere.backend.company.exception.DuplicateCompanyException;
import com.seahere.backend.user.controller.response.SettingCompanyResponse;
import com.seahere.backend.company.entity.CompanyEntity;
import com.seahere.backend.company.exception.CompanyNotFound;
import com.seahere.backend.company.repository.CompanyRepository;
import com.seahere.backend.company.repository.CompanySelectRepository;
import com.seahere.backend.company.controller.request.CompanyCreateReq;
import com.seahere.backend.company.controller.request.CompanySearch;
import com.seahere.backend.company.controller.response.CompanyResponse;
import com.seahere.backend.company.controller.response.CompanyFollowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanySelectRepository companySelectRepository;

    @Override
    public CompanyResponse getCompanyById(Long id) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow(CompanyNotFound::new);

        return CompanyResponse.from(company);
    }

    @Override
    public CompanyFollowResponse getCompanyByIdForCustomer(Long customerId, Long id) {
        return companySelectRepository.findCompanyByIdForCustomer(customerId, id);
    }

    @Override
    public CompanyResponse getCompanyByRegNumber(String registrationNumber) {
        CompanyEntity company = companyRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(CompanyNotFound::new);

        return CompanyResponse.from(company);
    }

    @Override
    public List<CompanyResponse> getCompanyByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName)
                .stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long save(CompanyCreateReq companyCreateReq) {
        CompanyEntity company = CompanyEntity.from(companyCreateReq);
        companyRepository.save(company);

        return company.getId();
    }

    @Transactional
    @Override
    public CompanyResponse editProfileImage(Long id, String profileImage) {
        CompanyEntity company = companyRepository.findById(id)
                .orElseThrow(CompanyNotFound::new);
        company.editProfileImage(profileImage);
        return CompanyResponse.from(company);
    }

    @Override
    public List<CompanyResponse> getList(CompanySearch companySearch) {
        return companySelectRepository.getList(companySearch)
                .stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyFollowResponse> getListForCustomer(Long customerId, CompanySearch companySearch) {
        return companySelectRepository.getListForCustomer(customerId, companySearch);
    }

    @Override
    public List<CompanyFollowResponse> getFollowListForCustomer(Long customerId, CompanySearch companySearch) {
        return companySelectRepository.getFollowListForCustomer(customerId, companySearch);
    }

    @Override
    public CompanyResponse getMostOutgoingCompany() {
        CompanyEntity bestCompany = companySelectRepository.findCompanyWithBestOutgoing();
        return CompanyResponse.from(bestCompany);
    }
    @Override
    public List<CompanyResponse> getTradeCompany(CompanySearch companySearch, Long companyId) {
        return companySelectRepository.findTradeCompanyList(companySearch, companyId)
                .stream()
                .map(CompanyResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyFollowResponse getMostOutgoingCompanyForCustomer(Long customerId) {
        return companySelectRepository.findCompanyWithBestOutgoingForCustomer(customerId);
    }



    @Override
    public SettingCompanyResponse getCompanyAndEmployee(Long id) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow(CompanyNotFound::new);
        return SettingCompanyResponse.from(company);
    }

    @Override
    public Boolean duplicateRegNumber(String registrationNumber) {
        Optional<CompanyEntity> company = companyRepository.findByRegistrationNumber(registrationNumber);
        if(company.isPresent()){
            throw new DuplicateCompanyException();
        }
        else{
            return true;
        }
    }

}
