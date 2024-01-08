package com.clover.youngchat.domain.user.controller;

import com.clover.youngchat.domain.user.dto.request.UserSignupReq;
import com.clover.youngchat.domain.user.dto.response.UserSignupRes;
import com.clover.youngchat.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupRes> signup(@Valid @RequestBody UserSignupReq userSignupReq) {
        return ResponseEntity.ok(userService.signup(userSignupReq));
    }
}
