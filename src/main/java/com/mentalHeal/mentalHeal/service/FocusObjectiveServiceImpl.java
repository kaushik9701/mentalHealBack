package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.model.FocusObjective;
import com.mentalHeal.mentalHeal.model.User;
import com.mentalHeal.mentalHeal.repository.FocusObjectiveRepository;
import com.mentalHeal.mentalHeal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FocusObjectiveServiceImpl implements FocusObjectiveService {

    private final FocusObjectiveRepository focusObjectiveRepository;
    private final UserRepository userRepository;

    @Override
    public List<FocusObjective> getObjectivesByUser(User user) {
        return focusObjectiveRepository.findByUserId(user.getId());
    }


    @Override
    public FocusObjective createObjective(Long userId, FocusObjective objective) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        objective.setUser(user);
        objective.setCreatedAt(LocalDate.now());
        objective.setCompleted(false);
        return focusObjectiveRepository.save(objective);
    }
}
