package com.Chatapi.Controllers;

import com.Chatapi.Entities.Dialog;
import com.Chatapi.Entities.User;
import com.Chatapi.securities.JwtTokenProvider;
import com.Chatapi.servises.DialogService;
import com.Chatapi.servises.UserService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/chat")
@PreAuthorize("hasAuthority('read&write')")
public class ChatController {

    private final UserService userService;
    private final DialogService dialogService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/dialog")
    public ResponseEntity<?> getDialogId(@RequestHeader("Authorization") String token,
                                         @RequestParam(required = false) Long recipientId) {
        UserDetails userDetails = (UserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal();
        User user = userService.findUser(userDetails.getUsername());

        HashMap<Object, Object> response = new HashMap<>();
        if (recipientId != null) response.put("dialogId", dialogService.uniqueDialog(
                user.getUserId(), recipientId));

        else response.put("dialogIds", dialogService.getUserDialogs(user.getUserId()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/new/dialog")
    public ResponseEntity<?> createDialog(@RequestHeader("Authorization") String token,
                                          @RequestParam Long recipientId) {
        UserDetails userDetails = (UserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal();
        Dialog dialog = new Dialog();

        dialog.setSenderId(userService.findUser(userDetails.getUsername()).getUserId());
        dialog.setRecipientId(recipientId);

        Dialog result = dialogService.saveDialog(dialog);
        if (result == null)
            return new ResponseEntity<>("Dialog already exist", HttpStatus.NOT_FOUND);

        HashMap<Object, Object> response = new HashMap<>();
        response.put("dialogId", result.getDialogId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam Long dialogId,
                                        @RequestParam(required = false) Integer limit,
                                        @RequestParam(required = false) Long timestamp,
                                        @RequestParam(required = false) Boolean older) {
        if (limit == null || limit > 50 || limit < 1) limit = 20;
        Dialog dialog;
        try {
            dialog = dialogService.findDialog(dialogId);

        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HashMap<Object, Object> response = new HashMap<>();

        if (timestamp == null || older == null)
            response.put("messages", dialog.getMessages().stream().limit(limit).toList());

        else if (older)
            response.put("messages", dialog.getMessages().stream().filter(e ->
                            e.getTimestamp().getTime() <= timestamp)
                    .limit(limit).toList());

        else response.put("messages", dialog.getMessages().stream().filter(e ->
                            e.getTimestamp().getTime() >= timestamp)
                    .limit(limit).toList());

        return ResponseEntity.ok(response);
    }


    public ChatController(UserService userService, DialogService dialogService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.dialogService = dialogService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}