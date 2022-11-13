package com.Chatapi.Entities;

import com.Chatapi.Enums.MessageType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID messageId;

    @Column(nullable = false, length = 1000)
    private String text;

    private String data;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String mediaUrl;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long recipientId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp timestamp;
}