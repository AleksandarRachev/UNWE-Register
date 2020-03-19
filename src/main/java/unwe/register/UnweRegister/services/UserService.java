package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unwe.register.UnweRegister.config.JwtTokenUtil;
import unwe.register.UnweRegister.dtos.user.UserLoginRequest;
import unwe.register.UnweRegister.dtos.user.UserRegisterRequest;
import unwe.register.UnweRegister.dtos.user.UserResponse;
import unwe.register.UnweRegister.dtos.interfaces.UserAuthenticationRequest;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.enums.Role;
import unwe.register.UnweRegister.exceptions.ElementAlreadyExistsException;
import unwe.register.UnweRegister.exceptions.PasswordsNotMatchingException;
import unwe.register.UnweRegister.exceptions.WrongCredentialsException;
import unwe.register.UnweRegister.repositories.UserRepository;

@Service
public class UserService {

    private static final String WRONG_CREDENTIALS = "Wrong credentials";
    private static final String EMAIL_TAKEN = "Email taken";
    private static final String PHONE_TAKEN = "Phone taken";
    private static final String PASSWORDS_DOES_NOT_MATCH = "Passwords does not match";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper,
                       JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private UserLoginResponse getUserRegisterResponse(UserAuthenticationRequest userAuthenticationRequest, User user) {
        UserResponse userLogin = new UserResponse(user.getUid(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getPhone(), user.getRole());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userAuthenticationRequest.getEmail());
        return new UserLoginResponse(jwtTokenUtil.generateToken(userDetails), userLogin);
    }

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new WrongCredentialsException(WRONG_CREDENTIALS));

        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }

        return getUserRegisterResponse(userLoginRequest, user);
    }

    public UserLoginResponse registerUser(UserRegisterRequest userRegisterRequest) {
        validateRegisterInfo(userRegisterRequest);

        User user = modelMapper.map(userRegisterRequest, User.class);

        if (userRegisterRequest.getDepartment() == null) {
            user.setRole(Role.EMPLOYER);
        } else {
            user.setRole(Role.COORDINATOR);
        }

        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        userRepository.save(user);

        return getUserRegisterResponse(userRegisterRequest, user);
    }

    private void validateRegisterInfo(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByEmail(userRegisterRequest.getEmail()).isPresent()) {
            throw new ElementAlreadyExistsException(EMAIL_TAKEN);
        }

        if (userRepository.findByPhone(userRegisterRequest.getPhone()).isPresent()) {
            throw new ElementAlreadyExistsException(PHONE_TAKEN);
        }

        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getRepeatPassword())) {
            throw new PasswordsNotMatchingException(PASSWORDS_DOES_NOT_MATCH);
        }
    }
}
