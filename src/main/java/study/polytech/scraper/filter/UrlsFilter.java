package study.polytech.scraper.filter;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UrlsFilter {

    private final Set<String> whitelistUrls;
    private final Set<String> blacklistUrls;

    public UrlsFilter(@NonNull Set<String> whitelistUrls,
                      @NonNull Set<String> blacklistUrls) {
        this.whitelistUrls = whitelistUrls;
        this.blacklistUrls = blacklistUrls;
    }

    @NonNull
    public UrlAnalyzerResult analyze(@NonNull String url) {
        Objects.requireNonNull(url);
        if (isWrongUrl(url)) {
            return UrlAnalyzerResult.WRONG_FORMAT;
        }
        if (whitelistUrls.contains(url)) {
            return UrlAnalyzerResult.WHITELIST;
        }
        if (blacklistUrls.contains(url)) {
            return UrlAnalyzerResult.BLACKLIST;
        }
        return UrlAnalyzerResult.UNKNOWN;
    }

    private static boolean isWrongUrl(@NonNull String url) {
        return !url.startsWith("http://") && !url.startsWith("https://");
    }

}
