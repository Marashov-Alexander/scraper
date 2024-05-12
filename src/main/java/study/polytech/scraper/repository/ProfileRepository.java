package study.polytech.scraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.polytech.scraper.entity.TProfileEntity;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<TProfileEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM t_profile WHERE is_for_masking = :isForMasking ORDER BY RANDOM() LIMIT 1")
    Optional<TProfileEntity> findRandom(boolean isForMasking);

}
