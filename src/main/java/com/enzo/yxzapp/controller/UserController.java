package com.enzo.yxzapp.controller;

import com.enzo.yxzapp.dto.common.PageResponse;
import com.enzo.yxzapp.dto.user.CreateUserRequest;
import com.enzo.yxzapp.dto.user.UpdateUserRequest;
import com.enzo.yxzapp.dto.user.UserResponse;
import com.enzo.yxzapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@RequestBody @Valid CreateUserRequest req){
        return userService.create(req);
    }

    @PatchMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest req){
        return userService.update(id,req);
    }

    @GetMapping
    public PageResponse<UserResponse> list (Pageable pageable){
        return userService.list(pageable);
    }

    @GetMapping("/{id}")
    public UserResponse getById (@PathVariable Long id){
        return userService.getById(id);
    }
}
