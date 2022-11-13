package com.Chatapi.models;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class AuthenticationRequestDTO {
 @NotNull
 @Min(2)
 @Max(30)
 private String login;

 @NotNull
 private String password;
}