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

    private final String url;
    private final boolean disableMedia;
    private final boolean emulateDevice;
    private final ScraperProfile profile;

    public ScrapRequest(@NonNull String url, @Nullable Boolean disableMedia, @Nullable Boolean emulateDevice, @NonNull ScraperProfile profile) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(profile);
        this.url = url;
        this.disableMedia = disableMedia != null && disableMedia;
        this.emulateDevice = emulateDevice != null && emulateDevice;
        this.profile = profile;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public boolean isDisableMedia() {
        return disableMedia;
    }

    public boolean isEmulateDevice() {
        return emulateDevice;
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
