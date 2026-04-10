package com.enzo.yxzapp.auth;

import com.enzo.yxzapp.dto.auth.LoginRequest;
import com.enzo.yxzapp.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest req);
}
