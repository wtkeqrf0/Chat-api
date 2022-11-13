package com.Chatapi.servises;

import com.Chatapi.Entities.Dialog;
import com.Chatapi.repos.DialogRepo;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DialogService {

    private final DialogRepo repo;

    public Dialog findDialog(Long dialogId) throws ChangeSetPersister.NotFoundException {
        return repo.findById(dialogId).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public Dialog saveDialog(Dialog dialog) {
        if (repo.uniqueDialog(dialog.getSenderId(), dialog.getRecipientId()).orElse(null) == null)
            return repo.save(dialog);
        return null;
    }

    public Dialog uniqueDialog(Long senderId, Long recipientId) {
        return repo.uniqueDialog(senderId, recipientId).orElse(null);
    }

    public Set<Dialog> getUserDialogs(Long senderId) {
        return repo.getUserDialogs(senderId);
    }

    public DialogService(DialogRepo repo) {
        this.repo = repo;
    }
}