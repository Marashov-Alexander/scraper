package study.polytech.scraper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import study.polytech.scraper.profile.ScraperProfile;

import java.io.Serializable;
import java.util.Objects;

public class ScrapRequest implements Serializable {

    private final long urlId;
    private final String url;
    private final boolean disableMedia;
    private final ScraperProfile profile;

    public ScrapRequest(long urlId, @NonNull String url, @Nullable Boolean disableMedia, @NonNull ScraperProfile profile) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(profile);
        this.urlId = urlId;
        this.url = url;
        this.disableMedia = disableMedia != null && disableMedia;
        this.profile = profile;
    }

    public long getUrlId() {
        return urlId;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public boolean isDisableMedia() {
        return disableMedia;
    }

    @NonNull
    public ScraperProfile getProfile() {
        return profile;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
