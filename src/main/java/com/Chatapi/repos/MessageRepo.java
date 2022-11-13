package com.Chatapi.repos;

import com.Chatapi.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@EnableJpaRepositories
public interface MessageRepo extends JpaRepository<Message, UUID> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE messages SET data = :data WHERE message_id = :messageId ;", nativeQuery = true)
    void updateData(String data, UUID messageId);
}