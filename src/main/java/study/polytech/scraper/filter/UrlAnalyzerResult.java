package study.polytech.scraper.filter;

public enum UrlAnalyzerResult {

    UNKNOWN,
    WHITELIST,
    BLACKLIST,
    WRONG_FORMAT,
    ;

    public boolean shouldProcess() {
        return this == UNKNOWN;
    }

}
