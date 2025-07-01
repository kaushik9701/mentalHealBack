package com.mentalHeal.mentalHeal.controller;

import com.mentalHeal.mentalHeal.DTO.ConversationRequest;
import com.mentalHeal.mentalHeal.DTO.ConversationResponse;
import com.mentalHeal.mentalHeal.model.FocusCheckIn;
import com.mentalHeal.mentalHeal.model.FocusObjective;
import com.mentalHeal.mentalHeal.service.FocusAIService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/focus")
@RequiredArgsConstructor
public class FocusConversationController {

    private final FocusAIService focusAIService;

    @PostMapping("/conversation")
    public ResponseEntity<ConversationResponse> handleConversation(@RequestBody ConversationRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String userMessage = request.getMessage();
        String objectiveId = request.getObjectiveId();

        List<Map<String, String>> history = request.getHistory()
                .stream()
                .map(msg -> Map.of("role", msg.getRole(), "content", msg.getContent()))
                .toList();

        try {
            FocusObjective savedObjective = focusAIService.handleConversation(email, userMessage, history, objectiveId);

            List<FocusCheckIn> checkIns = savedObjective.getCheckIns();

            if (checkIns == null || checkIns.isEmpty()) {
                return ResponseEntity.status(500).body(new ConversationResponse("No check-ins found.", null));
            }

            String aiReply = (checkIns.get(checkIns.size() - 1).getNote() != null)
                    ? checkIns.get(checkIns.size() - 1).getNote().replace("assistant: ", "")
                    : "No reply";

            // ✅ FIX: Convert Long to String
            ConversationResponse response = new ConversationResponse(aiReply, String.valueOf(savedObjective.getId()));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // ✅ More helpful debug info
            e.printStackTrace();
            System.out.println("❌ Error processing conversation: " + e.getMessage());
            return ResponseEntity.status(500).body(new ConversationResponse("Something went wrong. Please try again.", null));
        }
    }
}
