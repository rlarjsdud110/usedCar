package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.UserLoginRequest;
import com.kgy.usedCar.dto.request.UserSignupRequest;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.UserLoginResponse;
import com.kgy.usedCar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<Void> signup(@RequestBody UserSignupRequest request){
        userService.signup(request);
        return Response.success(null);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request);
        return Response.success(new UserLoginResponse(token));
    }

}
