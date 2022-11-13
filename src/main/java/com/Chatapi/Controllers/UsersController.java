package com.Chatapi.Controllers;

import com.Chatapi.Entities.User;
import com.Chatapi.models.UserModel;
import com.Chatapi.securities.JwtTokenProvider;
import com.Chatapi.servises.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAuthority('read&write')")
public class UsersController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token,
                                         @RequestParam(required = false) Long userId) {
        final User user;
        try {

            if (userId == null) {
                UserDetails userDetails = (UserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal();
                user = userService.findUser(userDetails.getUsername());
            } else user = userService.findUser(userId);

        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HashMap<Object, Object> response = new HashMap<>();
        response.put("userId", user.getUserId().toString());
        response.put("avatar", user.getAvatar());
        response.put("surname", user.getSurname());
        response.put("name", user.getName());
        response.put("middleName", user.getMiddleName());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/info")
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String token,
                                            @RequestBody @Valid UserModel user) {
        UserDetails userDetails = (UserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal();

        userService.updateUser(user.getName(), user.getMiddleName(),
                user.getSurname(), user.getAvatar(), userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public UsersController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}