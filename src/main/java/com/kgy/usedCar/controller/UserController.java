package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.consult.ConsultRequestDto;
import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.request.user.UserUpdateRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.user.CartResponseDto;
import com.kgy.usedCar.dto.response.user.UserInfoResponseDto;
import com.kgy.usedCar.dto.response.user.UserLoginResponse;
import com.kgy.usedCar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/userInfo")
    public Response<UserInfoResponseDto> userInfo(Principal userId){
        UserInfoResponseDto userInfo = userService.userInfo(userId.getName());
        return Response.success(userInfo);
    }

    @PutMapping("/update")
    public Response<UserInfoResponseDto> update(Principal principal, @RequestBody UserUpdateRequestDto dto){
        UserInfoResponseDto userInfo = userService.update(principal.getName(), dto);
        return Response.success(userInfo);
    }

    @GetMapping("/cart")
    public Response<List<CartResponseDto>> getCart(Principal principal){
        List<CartResponseDto> cartList = userService.getCart(principal.getName());
        return Response.success(cartList);
    }

    @PostMapping("/cart/add/{carId}")
    public Response<Void> addCart(@PathVariable Long carId, Principal principal){
        userService.addCart(carId, principal.getName());
        return Response.success();
    }

    @DeleteMapping("/cart/delete/{carId}")
    public Response<Void> deleteCart(@PathVariable Long carId){
        userService.deleteCart(carId);
        return Response.success();
    }



}
