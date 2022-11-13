package com.Chatapi.models;

import com.Chatapi.Enums.MessageType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class MessageModel {
    @NotNull
    private Long dialogId;

    @NotNull
    @NotBlank
    private String text;

    @NotNull
    private MessageType messageType;

    private String data;

    private byte[] mediaByte;

    private String fileName;
}