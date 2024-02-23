package study.polytech.scraper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class ScrapResult {

    private final String url;
    private final String result;

    public ScrapResult(@NonNull String url, @NonNull String result) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(result);
        this.url = url;
        this.result = result;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ScrapResult that = (ScrapResult) o;

        return new EqualsBuilder()
                .append(url, that.url)
                .append(result, that.result)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .append(result)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ScrapResult{" +
                "url='" + url + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
