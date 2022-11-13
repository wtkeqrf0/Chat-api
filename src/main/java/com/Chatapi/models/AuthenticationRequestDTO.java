package com.Chatapi.models;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class AuthenticationRequestDTO {
 @NotNull
 private String login;
 @NotNull
 private String password;
}