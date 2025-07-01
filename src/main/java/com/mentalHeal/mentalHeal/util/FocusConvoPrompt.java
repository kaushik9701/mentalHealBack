package com.mentalHeal.mentalHeal.util;

import java.util.*;

public class FocusConvoPrompt {

    public static List<Map<String, String>> buildPrompt(List<String> conversationHistory, String currentUserInput) {
        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of("role", "system", "content",
                """
                You are a supportive mental health therapist talking one-on-one with a user who is opening up about their thoughts.
                Be warm, empathetic, and speak like a real human — just a couple of sentences per reply.
                Ask meaningful follow-up questions or offer comfort, but never sound robotic or overly formal.
                Avoid long paragraphs. Keep responses short and personal.
                """
        ));

        for (String line : conversationHistory) {
            if (line.startsWith("User:")) {
                messages.add(Map.of("role", "user", "content", line.substring(5).trim()));
            } else if (line.startsWith("AI:")) {
                messages.add(Map.of("role", "assistant", "content", line.substring(3).trim()));
            }
        }

        messages.add(Map.of("role", "user", "content", currentUserInput));

        return messages;
    }
}
