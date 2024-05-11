package study.polytech.scraper.profile;

import org.openqa.selenium.devtools.v118.emulation.model.UserAgentBrandVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import study.polytech.scraper.ScrapRequest;
import study.polytech.scraper.entity.TProfileEntity;
import study.polytech.scraper.repository.ProfileRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileManager.class);

    private final ProfileRepository profileRepository;

    public ProfileManager(@NonNull ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @NonNull
    public ScraperProfile getProfile(String url) {
        try {
            ScraperProfile scraperProfile = profileRepository.findRandom()
                    .map(this::convertToScraperProfile)
                    .orElse(null);
            if (scraperProfile == null) {
                LOGGER.info("For url [{}] profile not found so we will use default one", url);
                return ScraperProfile.DEFAULT;
            } else {
                LOGGER.info("For url [{}] got random profile [{}]", url, scraperProfile);
                return scraperProfile;
            }
        } catch (RuntimeException e) {
            LOGGER.error("Get profile error", e);
            return ScraperProfile.DEFAULT;
        }
    }

    @NonNull
    private ScraperProfile convertToScraperProfile(@NonNull TProfileEntity entity) {
        return new ScraperProfile(
                entity.getWidth(), entity.getHeight(), entity.getHardwareConcurrency(), entity.getMaxTouchPoints(),
                entity.getUserAgent(), entity.getAcceptLanguage(), toBrandVersions(entity.getBrandsVersions()),
                entity.getFullVersion(), entity.getPlatform(), entity.getPlatformVersion(), entity.getArchitecture(), entity.getModel(), entity.isMobile()
        );
    }

    @NonNull
    private List<UserAgentBrandVersion> toBrandVersions(String brandsVersions) {
        String[] brandAndVersions = brandsVersions.split(";");
        List<UserAgentBrandVersion> result = new ArrayList<>();
        for (String brandAndVersion : brandAndVersions) {
            String[] params = brandAndVersion.split(",");
            result.add(new UserAgentBrandVersion(params[0], params[1]));
        }
        return result;
    }

}
