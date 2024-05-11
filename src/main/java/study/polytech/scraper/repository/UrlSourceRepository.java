package study.polytech.scraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.polytech.scraper.entity.TUrlSourcesEntity;

public interface UrlSourceRepository extends JpaRepository<TUrlSourcesEntity, Long> {



}
