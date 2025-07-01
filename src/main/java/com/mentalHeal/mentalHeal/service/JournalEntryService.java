package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.model.JournalEntry;
import com.mentalHeal.mentalHeal.model.User;
import com.mentalHeal.mentalHeal.repository.JournalEntryRepository;
import com.mentalHeal.mentalHeal.util.PromptBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JournalEntryService {

    private final JournalEntryRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${together.api.key}")
    private String togetherApiKey;

    public JournalEntryService(JournalEntryRepository repository) {
        this.repository = repository;
    }
    public JournalEntry createEntryWithAI(String content, User user) {
        try {
            String aiResponse = getAIResponse(content);
            return createJournalEntryFromAIResponse(aiResponse, content, user);
        } catch (Exception e) {
            System.err.println("AI entry creation failed: " + e.getMessage());
            return createDefaultEntry(content, user);
        }
    }

    public JournalEntry createJournalEntryFromAIResponse(String aiResponse, String content, User user) {
        if (aiResponse == null || aiResponse.isBlank()) {
            return createDefaultEntry(content, user);
        }

        // Case-insensitive search for insight marker
        Pattern insightPattern = Pattern.compile("insight\\s*:", Pattern.CASE_INSENSITIVE);
        Matcher matcher = insightPattern.matcher(aiResponse);

        String title;
        String insight;

        if (matcher.find()) {
            int insightIndex = matcher.start();
            title = cleanTitle(aiResponse.substring(0, insightIndex).trim());
            insight = aiResponse.substring(matcher.end()).trim();
        } else {
            title = cleanTitle(aiResponse);
            insight = "";
        }

        JournalEntry entry = new JournalEntry();
        entry.setContent(content);
        entry.setTitle(title);
        entry.setInsight(insight);
        entry.setUser(user);

        return repository.save(entry);
    }

    private String cleanTitle(String titlePart) {
        // Remove leading/trailing quotes independently
        String cleaned = titlePart.trim();

        if (cleaned.startsWith("\"")) {
            cleaned = cleaned.substring(1);
        }

        if (cleaned.endsWith("\"")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }

        // Remove "title:" prefix (case-insensitive)
        cleaned = cleaned.replaceFirst("(?i)^title\\s*:\\s*", "").trim();

        // Capitalize first letter
        if (!cleaned.isEmpty()) {
            cleaned = cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1);
        }

        return cleaned.isEmpty() ? "Untitled" : cleaned;
    }

    public JournalEntry createDefaultEntry(String content, User user) {
        JournalEntry entry = new JournalEntry();
        entry.setContent(content);
        entry.setTitle("Untitled");
        entry.setInsight("");
        entry.setUser(user);
        return repository.save(entry);
    }
private String getAIResponse(String content) {
    String url = "https://api.together.xyz/inference";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(togetherApiKey);

    Map<String, Object> requestBody = Map.of(
            "model", "mistralai/Mixtral-8x7B-Instruct-v0.1",
            "prompt", PromptBuilder.buildTherapistPrompt(content),
            "max_tokens", 400,
            "temperature", 0.5
    );

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
    if (response.getStatusCode().is2xxSuccessful()) {
        Map<String, Object> body = response.getBody();
        System.out.println("Response body: " + body);  // For debugging

        if (body != null && body.containsKey("choices")) {
            Object choicesObj = body.get("choices");
            if (choicesObj instanceof List<?>) {
                List<?> choices = (List<?>) choicesObj;
                if (!choices.isEmpty() && choices.get(0) instanceof Map) {
                    Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
                    Object textObj = firstChoice.get("text");
                    if (textObj instanceof String) {
                        return ((String) textObj).trim();
                    }
                }
            }
        }
    }
    throw new RuntimeException("AI response failed or invalid format");
}

}
