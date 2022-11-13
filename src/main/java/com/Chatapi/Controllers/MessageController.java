package com.Chatapi.Controllers;

import com.Chatapi.Entities.Dialog;
import com.Chatapi.Entities.Message;
import com.Chatapi.Enums.MessageType;
import com.Chatapi.models.MessageModel;
import com.Chatapi.models.WidgetModel;
import com.Chatapi.servises.DialogService;
import com.Chatapi.servises.MessageService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.UUID;

@RestController
@PreAuthorize("hasAuthority('read&write')")
public class MessageController {

    private final MessageService messageService;
    private final DialogService dialogService;

    @PostMapping("message/send")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid MessageModel messageModel) {
        final Message message = new Message();
        final Dialog dialog;
        try {
            dialog = dialogService.findDialog(messageModel.getDialogId());

        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("Dialog with id " +
                    messageModel.getDialogId() + " does not exist", HttpStatus.NOT_FOUND);
        }
        message.setDialogId(dialog);
        message.setText(messageModel.getText());
        message.setMessageType(messageModel.getMessageType());

        message.setSenderId(dialog.getSenderId());
        message.setRecipientId(dialog.getRecipientId());

        HashMap<Object, Object> response = new HashMap<>();

        if (messageModel.getMessageType() == MessageType.MEDIA) {
            String path = Path.of("src\\files").toAbsolutePath() + "\\" +
                    UUID.randomUUID() + "-" + messageModel.getFileName();

            try {
                Files.write(Path.of(path),
                        messageModel.getMediaByte(),
                        StandardOpenOption.CREATE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            message.setMediaUrl(path);
            response.put("mediaUrl", path);
        }
        response.put("messageId", messageService.saveMessage(message).getMessageId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("chat/message/update")
    public ResponseEntity<?> updateWidget(@RequestBody @Valid WidgetModel widgetModel) {
        try {
            messageService.findMessage(widgetModel.getMessageId());

        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>("This message id does not exist", HttpStatus.NOT_FOUND);
        }

        messageService.updateData(widgetModel.getData(), widgetModel.getMessageId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public MessageController(MessageService messageService, DialogService dialogService) {
        this.messageService = messageService;
        this.dialogService = dialogService;
    }
}