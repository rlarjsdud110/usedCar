package com.kgy.usedCar.controller;

import com.kgy.usedCar.dto.request.user.UserLoginRequest;
import com.kgy.usedCar.dto.request.user.UserSignupRequest;
import com.kgy.usedCar.dto.request.user.UserUpdateRequestDto;
import com.kgy.usedCar.dto.response.Response;
import com.kgy.usedCar.dto.response.user.CartResponseDto;
import com.kgy.usedCar.dto.response.user.PurchaseResponse;
import com.kgy.usedCar.dto.response.user.UserInfoResponseDto;
import com.kgy.usedCar.dto.response.user.UserLoginResponseDto;
import com.kgy.usedCar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Response<UserLoginResponseDto> login(@RequestBody UserLoginRequest request){
        UserLoginResponseDto userLoginResponse = userService.login(request);
        return Response.success(userLoginResponse);
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
    public Response<Void> deleteCart(@PathVariable Long carId, Principal principal){
        userService.deleteCart(carId, principal.getName());
        return Response.success();
    }

    @PostMapping("/purchase/{carId}")
    public Response<Void> purchaseRequest(@PathVariable Long carId, Principal principal){
        userService.purchaseRequest(carId, principal.getName());
        return Response.success();
    }

    @GetMapping("/purchase")
    public Response<List<PurchaseResponse>> purchaseList(Principal principal){
        List<PurchaseResponse> purchaseResponseList = userService.purchaseList(principal.getName());
        return Response.success(purchaseResponseList);
    }

    @DeleteMapping("/purchase/{purchaseId}")
    public Response<Void> purchaseDelete(@PathVariable Long purchaseId) {
        userService.purchaseDelete(purchaseId);
        return Response.success();
    }

}
