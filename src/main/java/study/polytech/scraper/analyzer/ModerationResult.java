package study.polytech.scraper.analyzer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class ModerationResult implements Serializable {

    private final String url;
    private final String finalUrl;
    private final String title;
    private final String defaultScreenshotName;
    private final String screenshotWithoutMediaName;
    private final Long defaultScreenshotHash;
    private final Long screenshotWithoutMediaHash;
    private final String errorStatus;

    public ModerationResult(@NonNull String url,
                            @Nullable String finalUrl,
                            @Nullable String title,
                            @Nullable String defaultScreenshotName,
                            @Nullable String screenshotWithoutMediaName,
                            @Nullable Long defaultScreenshotHash,
                            @Nullable Long screenshotWithoutMediaHash) {
        Objects.requireNonNull(url);
        this.url = url;
        this.finalUrl = finalUrl;
        this.title = title;
        this.defaultScreenshotName = defaultScreenshotName;
        this.screenshotWithoutMediaName = screenshotWithoutMediaName;
        this.defaultScreenshotHash = defaultScreenshotHash;
        this.screenshotWithoutMediaHash = screenshotWithoutMediaHash;
        this.errorStatus = null;
    }

    public ModerationResult(@NonNull String url, @NonNull String errorStatus) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(errorStatus);
        this.url = url;
        this.finalUrl = null;
        this.title = null;
        this.defaultScreenshotName = null;
        this.screenshotWithoutMediaName = null;
        this.defaultScreenshotHash = null;
        this.screenshotWithoutMediaHash = null;
        this.errorStatus = errorStatus;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getFinalUrl() {
        return finalUrl;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDefaultScreenshotName() {
        return defaultScreenshotName;
    }

    @Nullable
    public String getScreenshotWithoutMediaName() {
        return screenshotWithoutMediaName;
    }

    @Nullable
    public Long getDefaultScreenshotHash() {
        return defaultScreenshotHash;
    }

    @Nullable
    public Long getScreenshotWithoutMediaHash() {
        return screenshotWithoutMediaHash;
    }

    @Nullable
    public String getErrorStatus() {
        return errorStatus;
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
