package study.polytech.scraper.analyzer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import study.polytech.scraper.ScrapRequest;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

public class ScrapResult implements Serializable {

    private final ScrapRequest request;
    private final String title;
    private final Path screenshotPath;
    private final String errorStatus;

    public ScrapResult(@NonNull ScrapRequest request,
                       @NonNull String title,
                       @Nullable Path screenshotPath) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(title);
        this.request = request;
        this.title = title;
        this.screenshotPath = screenshotPath;
        this.errorStatus = null;
    }

    public ScrapResult(@NonNull ScrapRequest request, @NonNull String errorStatus) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(errorStatus);
        this.request = request;
        this.title = null;
        this.screenshotPath = null;
        this.errorStatus = errorStatus;
    }

    @NonNull
    public ScrapRequest getScrapRequest() {
        return request;
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
