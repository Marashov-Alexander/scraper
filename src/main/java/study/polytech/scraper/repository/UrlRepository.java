package study.polytech.scraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import study.polytech.scraper.entity.TUrlEntity;
import study.polytech.scraper.filter.UrlInfo;

import java.util.List;

public interface UrlRepository extends JpaRepository<TUrlEntity, Long> {

    @Query("SELECT new study.polytech.scraper.filter.UrlInfo(u.id, u.baseUrl, u.domainAgeInDays) FROM TUrlEntity u")
    List<UrlInfo> findAllUrls();

    @Modifying
    @Transactional
    @Query("UPDATE TUrlEntity t SET t.domainAgeInDays = :domainAgeDays WHERE t.id = :id")
    void updateDomainAgeDaysById(long id, int domainAgeDays);

}
