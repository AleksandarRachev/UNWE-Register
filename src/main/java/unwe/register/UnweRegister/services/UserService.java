package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unwe.register.UnweRegister.config.JwtTokenUtil;
import unwe.register.UnweRegister.dtos.interfaces.UserAuthenticationRequest;
import unwe.register.UnweRegister.dtos.user.*;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.enums.Role;
import unwe.register.UnweRegister.exceptions.*;
import unwe.register.UnweRegister.repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String WRONG_CREDENTIALS = "Wrong credentials";
    private static final String EMAIL_TAKEN = "Email taken";
    private static final String PHONE_TAKEN = "Phone taken";
    private static final String PASSWORDS_DOES_NOT_MATCH = "Passwords does not match";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String EMAIL_MUST_NOT_BE_EMPTY = "Email must not be empty!";
    private static final String FIRST_NAME_MUST_NOT_BE_EMPTY = "First name must not be empty!";
    private static final String LAST_NAME_MUST_NOT_BE_EMPTY = "Last name must not be empty!";
    private static final String PHONE_MUST_NOT_BE_EMPTY = "Phone must not be empty!";
    private static final String INVALID_PHONE_NUMBER = "Invalid phone number!";
    private static final String SUCCESSFULLY_EDITED_PASSWORD = "Successfully edited password";
    private static final String PHONE_REGEX = "(\\+)?(359|0)8[789]\\d{1}\\d{3}\\d{3}";
    private static final String COMPANY_NAME_MISSING = "Company name missing!";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Value("${picture.url.user}")
    private String pictureUserUrl;

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
                user.getLastName(), user.getPhone(), user.getRole(), user.getAddress(), user.getContactPerson(),
                user.getCompanyName(), getUserPictureUrl(user));

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

        if (userRegisterRequest.getDepartmentId() == null) {
            user.setRole(Role.EMPLOYER);
        } else {
            user.setRole(Role.COORDINATOR);
        }

        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        userRepository.save(user);

        return getUserRegisterResponse(userRegisterRequest, user);
    }

    private void validateRegisterInfo(UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest.getDepartmentId() == null) {
            if (userRegisterRequest.getCompanyName().isBlank()) {
                throw new FieldMissingException(COMPANY_NAME_MISSING);
            }
        }
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

    public UserResponse editProfile(UserEditPersonalInfoRequest userEdit, MultipartFile multipartFile,
                                    String userId) throws IOException {
        User user = getUser(userId);

        validateEditUserMandatoryFields(userEdit, user);

        user.setEmail(userEdit.getEmail());
        user.setFirstName(userEdit.getFirstName());
        user.setLastName(userEdit.getLastName());
        user.setAddress(userEdit.getAddress());
        user.setContactPerson(userEdit.getContactPerson());
        user.setCompanyName(userEdit.getCompanyName());
        if (multipartFile != null) {
            user.setImage(multipartFile.getBytes());
        }
        UserResponse userResponse = modelMapper.map(userRepository.save(user), UserResponse.class);
        userResponse.setImageUrl(pictureUserUrl + userId);
        return userResponse;
    }

    private void validateEditUserMandatoryFields(UserEditPersonalInfoRequest userEdit, User user) {
        if (user.getRole() == Role.EMPLOYER) {
            if (userEdit.getCompanyName().isBlank()) {
                throw new FieldMissingException(COMPANY_NAME_MISSING);
            }
        }
        if (userEdit.getEmail().isBlank()) {
            throw new FieldMissingException(EMAIL_MUST_NOT_BE_EMPTY);
        }
        if (userEdit.getFirstName().isBlank()) {
            throw new FieldMissingException(FIRST_NAME_MUST_NOT_BE_EMPTY);
        }
        if (userEdit.getLastName().isBlank()) {
            throw new FieldMissingException(LAST_NAME_MUST_NOT_BE_EMPTY);
        }
        if (userEdit.getPhone().isBlank()) {
            throw new FieldMissingException(PHONE_MUST_NOT_BE_EMPTY);
        }
        if (!userEdit.getPhone().matches(PHONE_REGEX)) {
            throw new FieldValidationException(INVALID_PHONE_NUMBER);
        }
    }

    public byte[] getUserPicture(String userId) {
        return getUser(userId).getImage();
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ElementNotPresentException(USER_NOT_FOUND));
    }

    public String getUserPictureUrl(User user) {
        return user.getImage() != null ? pictureUserUrl + user.getUid() : null;
    }

    public String editPassword(EditPasswordRequest editPasswordRequest, String userId) {
        User user = getUser(userId);

        if (!passwordEncoder.matches(editPasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }

        if (!editPasswordRequest.getPassword().equals(editPasswordRequest.getRepeatPassword())) {
            throw new PasswordsNotMatchingException(PASSWORDS_DOES_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(editPasswordRequest.getPassword()));

        userRepository.save(user);

        return SUCCESSFULLY_EDITED_PASSWORD;
    }

    public List<UserResponse> getAllEmployers() {
        return userRepository.findAllByRole(Role.EMPLOYER)
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserResponse userResponse = modelMapper.map(user, UserResponse.class);
                    if (user.getImage() != null) {
                        userResponse.setImageUrl(pictureUserUrl + user.getUid());
                    }
                    return userResponse;
                })
                .collect(Collectors.toList());
    }

    public UserResponse getUserDetails(String userId) {
        User user = getUser(userId);

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        if (user.getImage() != null) {
            userResponse.setImageUrl(pictureUserUrl + userId);
        }
        return userResponse;
    }
}
