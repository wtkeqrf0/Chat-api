package com.Chatapi.models;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class WidgetModel {
    @NotNull
    private UUID messageId;
    @NotNull
    private String data;
}