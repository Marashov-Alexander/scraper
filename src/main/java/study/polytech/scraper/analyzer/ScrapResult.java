package study.polytech.scraper.analyzer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import study.polytech.scraper.ScrapRequest;

import java.io.Serializable;
import java.util.Objects;

public class ScrapResult implements Serializable {

    private final ScrapRequest request;
    private final String title;
    private final String finalUrl;
    private final String defaultScreenshotName;
    private final String screenshotWithoutMediaName;
    private final String errorStatus;

    public ScrapResult(@NonNull ScrapRequest request,
                       @NonNull String title,
                       @NonNull String finalUrl,
                       @Nullable String defaultScreenshotName,
                       @Nullable String screenshotWithoutMediaName) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(title);
        Objects.requireNonNull(finalUrl);
        this.request = request;
        this.title = title;
        this.finalUrl = finalUrl;
        this.defaultScreenshotName = defaultScreenshotName;
        this.screenshotWithoutMediaName = screenshotWithoutMediaName;
        this.errorStatus = null;
    }

    public ScrapResult(@NonNull ScrapRequest request, @NonNull String errorStatus) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(errorStatus);
        this.request = request;
        this.title = null;
        this.finalUrl = null;
        this.defaultScreenshotName = null;
        this.screenshotWithoutMediaName = null;
        this.errorStatus = errorStatus;
    }

    @NonNull
    public ScrapRequest getRequest() {
        return request;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getFinalUrl() {
        return finalUrl;
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
