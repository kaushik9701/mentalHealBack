package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.model.*;
import com.mentalHeal.mentalHeal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FocusAIService {

    private final UserRepository userRepository;
    private final FocusObjectiveRepository focusObjectiveRepository;

    @Value("${together.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    public FocusObjective handleConversation(String email, String userInput, List<Map<String, String>> conversationHistory, String objectiveId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FocusObjective objective;
        boolean isNewObjective = false;

        if (objectiveId != null) {
            objective = focusObjectiveRepository.findById(objectiveId)
                    .orElseThrow(() -> new RuntimeException("Objective not found"));
        } else {
            // Create a new objective
            objective = FocusObjective.builder()
                    .user(user)
                    .objective(userInput)
                    .createdAt(LocalDate.now())
                    .completed(false)
                    .checkIns(new ArrayList<>())
                    .build();
            isNewObjective = true;
        }

        // Add user's latest input to conversation
        List<Map<String, String>> messages = new ArrayList<>();
        // If objectiveId exists, convert previous check-ins to conversation history
        if (objectiveId != null) {
            List<FocusCheckIn> pastCheckIns = objective.getCheckIns();
            for (FocusCheckIn checkIn : pastCheckIns) {
                String note = checkIn.getNote();
                if (note != null) {
                    String lower = note.toLowerCase(Locale.ROOT);
                    if (lower.startsWith("user:")) {
                        messages.add(Map.of("role", "user", "content", note.replaceFirst("user:\\s*", "")));
                    } else if (lower.startsWith("assistant:")) {
                        messages.add(Map.of("role", "assistant", "content", note.replaceFirst("assistant:\\s*", "")));
                    }
                }
            }
        }

// Add latest user input to conversation
        messages.add(Map.of("role", "user", "content", userInput));

// Debug: Print the conversation history before sending to AI
        System.out.println("🗨️ Conversation History sent to AI:");
        for (Map<String, String> msg : messages) {
            System.out.println(msg.get("role") + ": " + msg.get("content"));
        }


        String aiReply = fetchAIResponse(messages);

        LocalDate today = LocalDate.now();

        // ✅ Create check-ins AND set focusObjective
        FocusCheckIn userCheckIn = FocusCheckIn.builder()
                .note("user: " + userInput)
                .date(today)
                .focusObjective(objective) // ✅ critical!
                .build();

        FocusCheckIn aiCheckIn = FocusCheckIn.builder()
                .note("assistant: " + aiReply)
                .date(today)
                .focusObjective(objective) // ✅ critical!
                .build();

        // Add to objective
        objective.getCheckIns().add(userCheckIn);
        objective.getCheckIns().add(aiCheckIn);

        // Save
        FocusObjective saved = focusObjectiveRepository.save(objective);

        System.out.println("✅ AI Reply: " + aiReply);
        return saved;
    }

    private String fetchAIResponse(List<Map<String, String>> chatMessages) {
        try {
            Map<String, String> systemMessage = Map.of(
                    "role", "system",
                    "content", "You are a kind, emotionally intelligent mental health coach. Keep your replies short and human-like — just one or two sentences. Respond like you're having a casual conversation. Always remember the user’s goal, refer back to their past check-ins naturally, and never sound robotic or preachy. Prioritize support over advice."
            );

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(systemMessage);
            messages.addAll(chatMessages);

            Map<String, Object> request = Map.of(
                    "model", "mistralai/Mixtral-8x7B-Instruct-v0.1",
                    "messages", messages,
                    "max_tokens", 300,
                    "temperature", 0.7
            );

            return webClient.post()
                    .uri("https://api.together.xyz/inference")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        System.err.println("❌ Together API error:");
                                        System.err.println("Status code: " + clientResponse.statusCode());
                                        System.err.println("Error body: " + errorBody);
                                        return Mono.error(new RuntimeException("Together API error: " + errorBody));
                                    })
                    )
                    .bodyToMono(Map.class)
                    .map(response -> {
                        List<?> choices = (List<?>) response.get("choices");
                        if (choices == null || choices.isEmpty()) return "No choices in response.";
                        Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
                        Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");
                        return (String) message.get("content");
                    })
                    .block();

        } catch (WebClientResponseException e) {
            throw new RuntimeException("AI service is temporarily unavailable. Please try again later.");
        }
    }
}
