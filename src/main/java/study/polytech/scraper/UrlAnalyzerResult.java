package study.polytech.scraper;

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
