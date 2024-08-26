package com.seahere.backend.user.service;

import com.seahere.backend.user.controller.response.UserInfoRes;
import com.seahere.backend.user.request.*;

public interface UserService {
    Long signupCustomer(CustomerSignupReq customerSignupReq);
    Long signupBroker(BrokerSignupReq brokerSignupReq);
    Long signupCeo(CeoSignupReq ceoSignupReq);
    Long signupOauth(OAuthSignupReq oauthSignupReq);
    Boolean validateEmail(String email);
    void approveEmployee(String ceoEmail, String employeeEmail);
    void deleteEmployee(Long userId);
    UserInfoRes getUser(Long userId);

    void editUser(Long userId, UserEditReq userEdit);
}
