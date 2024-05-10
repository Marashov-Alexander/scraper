package study.polytech.scraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.polytech.scraper.entity.TUrlEntity;

public interface UrlRepository extends JpaRepository<TUrlEntity, Long> {



}
