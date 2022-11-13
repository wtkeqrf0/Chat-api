package com.Chatapi.Controllers;

import com.Chatapi.Entities.User;
import com.Chatapi.Enums.Role;
import com.Chatapi.models.AuthenticationRequestDTO;
import com.Chatapi.securities.JwtTokenProvider;
import com.Chatapi.servises.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequestDTO request) {
        try {
            User user = userService.findUser(request.getLogin());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

            Map<Object, Object> response = new HashMap<>();

            response.put("token", jwtTokenProvider.createToken(request.getLogin(), user.getRole().name()));
            response.put("userId", user.getUserId());
            response.put("login", user.getLogin());
            response.put("role", user.getRole().name());

            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Register before sign up", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid login/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<?> register(@RequestBody @Valid AuthenticationRequestDTO request,
                                      @RequestBody(required = false) Boolean isClient) {
        if (isClient == null) isClient = true;
        try {
            userService.findUser(request.getLogin());
            return new ResponseEntity<>("This login already exists", HttpStatus.UNAUTHORIZED);

        } catch (UsernameNotFoundException e) {
            User user = new User();
            user.setLogin(request.getLogin());
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            user.setRole(isClient ? Role.CLIENT : Role.OPERATOR);
            userService.saveUser(user);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/jwt/verify")
    public ResponseEntity<?> getJwtInfo(@RequestHeader("Authorization") String token) {
        UserDetails userDetails = (UserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal();
        User user = userService.findUser(userDetails.getUsername());
        HashMap<Object, Object> result = new HashMap<>();
        result.put("userId", user.getUserId());
        result.put("role", user.getRole());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}