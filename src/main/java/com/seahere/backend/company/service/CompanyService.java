package com.seahere.backend.company.service;

import com.seahere.backend.company.controller.request.CompanyCreateReq;
import com.seahere.backend.company.controller.request.CompanySearch;
import com.seahere.backend.company.controller.response.CompanyResponse;
import com.seahere.backend.user.controller.response.SettingCompanyResponse;
import com.seahere.backend.company.controller.response.CompanyFollowResponse;

import java.util.List;

public interface CompanyService {
    CompanyResponse getCompanyById(Long id);
    CompanyFollowResponse getCompanyByIdForCustomer(Long customerId, Long id);
    CompanyResponse getCompanyByRegNumber(String registrationNumber);
    List<CompanyResponse> getCompanyByCompanyName(String companyName);
    Long save(CompanyCreateReq companyCreateReq);
    CompanyResponse editProfileImage(Long id, String profileImage);

    List<CompanyResponse> getList(CompanySearch companySearch);
    List<CompanyFollowResponse> getListForCustomer(Long customerId, CompanySearch companySearch);

    CompanyResponse getMostOutgoingCompany();
    List<CompanyResponse> getTradeCompany(CompanySearch companySearch, Long companyId);
    CompanyFollowResponse getMostOutgoingCompanyForCustomer(Long customerId);

    List<CompanyFollowResponse> getFollowListForCustomer(Long customerId, CompanySearch companySearch);
    SettingCompanyResponse getCompanyAndEmployee(Long id);

    Boolean duplicateRegNumber(String registrationNumber);
}
