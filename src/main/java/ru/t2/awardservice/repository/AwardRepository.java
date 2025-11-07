package ru.t2.awardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t2.awardservice.entity.Award;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
}
