package com.mentalHeal.mentalHeal.repository;

import com.mentalHeal.mentalHeal.model.FocusObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FocusObjectiveRepository extends JpaRepository<FocusObjective, String> {

    List<FocusObjective> findByUserId(Long userId);

    @Query("""
SELECT u.email, fo.objective
FROM FocusObjective fo
JOIN fo.user u
LEFT JOIN fo.checkIns ci WITH ci.date = :yesterday
WHERE ci.id IS NULL
""")
    List<Object[]> findMissedCheckInsWithObjectives(@Param("yesterday") LocalDate yesterday);

}
