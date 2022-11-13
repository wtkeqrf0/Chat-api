package com.Chatapi.models;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UserModel {
    @NotNull
    private String surname;
    @NotNull
    private String name;
    @NotNull
    private String middleName;
    private byte[] avatar;
}