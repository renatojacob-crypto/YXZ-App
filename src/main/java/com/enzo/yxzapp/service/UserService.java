package com.enzo.yxzapp.service;

import com.enzo.yxzapp.dto.common.PageResponse;
import com.enzo.yxzapp.dto.user.CreateUserRequest;
import com.enzo.yxzapp.dto.user.UpdateUserRequest;
import com.enzo.yxzapp.dto.user.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
     UserResponse create(CreateUserRequest req);
     UserResponse update (Long id, UpdateUserRequest req);
     PageResponse<UserResponse> list(Pageable pageable);
     UserResponse getById(Long id);

}
