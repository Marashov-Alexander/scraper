package study.polytech.scraper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class ScrapResult implements Serializable {

    private final String url;
    private final String title;
    private final String screenshotPath;
    private final String errorStatus;

    public ScrapResult(@NonNull String url,
                       @NonNull String title,
                       @Nullable String screenshotPath) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(title);
        this.url = url;
        this.title = title;
        this.screenshotPath = screenshotPath;
        this.errorStatus = null;
    }

    public ScrapResult(@NonNull String url, @NonNull String errorStatus) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(errorStatus);
        this.url = url;
        this.title = null;
        this.screenshotPath = null;
        this.errorStatus = errorStatus;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getScreenshotPath() {
        return screenshotPath;
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
