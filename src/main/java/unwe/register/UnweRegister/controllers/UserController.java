package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unwe.register.UnweRegister.dtos.user.*;
import unwe.register.UnweRegister.services.UserLoginResponse;
import unwe.register.UnweRegister.services.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/checkLogged")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('COORDINATOR')")
    public void checkIfLogged() {}

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

    @PutMapping
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('COORDINATOR')")
    public ResponseEntity<UserResponse> editUserProfile(@RequestPart(value = "image", required = false) MultipartFile multipartFile,
                                                        @RequestParam("email") String email,
                                                        @RequestParam("firstName") String firstName,
                                                        @RequestParam("lastName") String lastName,
                                                        @RequestParam("phone") String phone,
                                                        @RequestParam(value = "address", required = false) String address,
                                                        @RequestParam(value = "contactPerson", required = false) String contactPerson,
                                                        @RequestAttribute("userId") String userId) throws IOException {
        return ResponseEntity.ok(userService.editProfile(
                new UserEditPersonalInfoRequest(email, firstName, lastName, phone, address, contactPerson), multipartFile, userId));
    }

    @GetMapping(value = "/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserPicture(userId));
    }

    @PutMapping("/password")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('COORDINATOR')")
    public ResponseEntity<String> editPassword(@Valid @RequestBody EditPasswordRequest editPasswordRequest,
                                               @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(userService.editPassword(editPasswordRequest, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<List<UserResponse>> getAllEmployers() {
        return ResponseEntity.ok(userService.getAllEmployers());
    }

}
