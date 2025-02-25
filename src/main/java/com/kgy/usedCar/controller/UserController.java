package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.user.UserLoginResponse;
import com.kgy.usedCar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<String> signup(@RequestBody UserSignupRequest request){
        userService.signup(request);
        return Response.success("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request);
        return Response.success(new UserLoginResponse(token));
    }

}
