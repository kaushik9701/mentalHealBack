package com.mentalHeal.mentalHeal.service;

import com.mentalHeal.mentalHeal.model.FocusObjective;
import com.mentalHeal.mentalHeal.model.User;

import java.util.List;

public interface FocusObjectiveService {
    FocusObjective createObjective(Long userId, FocusObjective objective);
    List<FocusObjective> getObjectivesByUser(User user);
}