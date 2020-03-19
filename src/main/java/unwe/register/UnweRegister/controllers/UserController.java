package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.user.UserLoginRequest;
import unwe.register.UnweRegister.dtos.user.UserRegisterRequest;
import unwe.register.UnweRegister.services.UserLoginResponse;
import unwe.register.UnweRegister.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userService.loginUser(userLoginRequest));
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserLoginResponse> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return ResponseEntity.status(201).body(userService.registerUser(userRegisterRequest));
    }

}
