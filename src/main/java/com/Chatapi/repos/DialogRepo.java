package com.Chatapi.repos;

import com.Chatapi.Entities.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface DialogRepo extends JpaRepository<Dialog, Long> {
    @Query(value = "SELECT * FROM dialogs WHERE sender_id = :senderId AND recipient_id = :recipientId ;", nativeQuery = true)
    Optional<Dialog> uniqueDialog(Long senderId, Long recipientId);
}
