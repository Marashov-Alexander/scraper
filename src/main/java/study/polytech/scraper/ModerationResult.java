package study.polytech.scraper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

public class ModerationResult implements Serializable {

    private final String url;
    private final String title;
    private final Path screenshotPath;
    private final Long screenshotHash;
    private final String errorStatus;

    public ModerationResult(@NonNull String url,
                            @Nullable String title,
                            @Nullable Path screenshotPath,
                            @Nullable Long screenshotHash) {
        Objects.requireNonNull(url);
        this.url = url;
        this.title = title;
        this.screenshotPath = screenshotPath;
        this.screenshotHash = screenshotHash;
        this.errorStatus = null;
    }

    public ModerationResult(@NonNull String url, @NonNull String errorStatus) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(errorStatus);
        this.url = url;
        this.title = null;
        this.screenshotPath = null;
        this.screenshotHash = null;
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
    public Path getScreenshotPath() {
        return screenshotPath;
    }

    @Nullable
    public Long getScreenshotHash() {
        return screenshotHash;
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
