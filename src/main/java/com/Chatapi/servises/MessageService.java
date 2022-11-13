package com.Chatapi.servises;

import com.Chatapi.Entities.Message;
import com.Chatapi.repos.MessageRepo;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepo repo;

    public Message findMessage(UUID uuid) throws ChangeSetPersister.NotFoundException {
        return repo.findById(uuid).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public void updateData(String data, UUID messageId) {
        repo.updateData(data, messageId);
    }

    public Message saveMessage(Message message) {
        return repo.save(message);
    }


    public MessageService(MessageRepo repo) {
        this.repo = repo;
    }
}
