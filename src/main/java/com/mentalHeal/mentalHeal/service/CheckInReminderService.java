package com.mentalHeal.mentalHeal.service;


import com.mentalHeal.mentalHeal.model.FocusCheckIn;
import com.mentalHeal.mentalHeal.model.FocusObjective;
import com.mentalHeal.mentalHeal.repository.FocusObjectiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckInReminderService {

    private final FocusObjectiveRepository focusObjectiveRepository;
    private final EmailService emailService;


    @Scheduled(cron = "0 0 8 * * *") // daily at 8 AM

    @Transactional(readOnly = true)
    public void sendMissedCheckInEmails() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Object[]> results = focusObjectiveRepository.findMissedCheckInsWithObjectives(yesterday);

        // Group by email and collect missed objectives
        Map<String, List<String>> emailToObjectives = new HashMap<>();
        for (Object[] row : results) {
            String email = (String) row[0];
            String objective = (String) row[1];
            emailToObjectives.computeIfAbsent(email, k -> new ArrayList<>()).add(objective);
        }

        // Send personalized emails
        for (Map.Entry<String, List<String>> entry : emailToObjectives.entrySet()) {
            String email = entry.getKey();
            List<String> missedObjectives = entry.getValue();

            String body = "Hi there!\n\nYou missed a check-in yesterday for the following objectives:\n\n"
                    + missedObjectives.stream().map(o -> "- " + o).collect(Collectors.joining("\n"))
                    + "\n\nTry to stay consistent — we're here for you! 💪";

            emailService.sendSimpleMessage(
                    email,
                    "You missed a check-in yesterday 😟",
                    body
            );
        }
    }
}

