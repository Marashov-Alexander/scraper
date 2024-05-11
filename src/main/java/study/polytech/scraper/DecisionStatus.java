package study.polytech.scraper;

public enum DecisionStatus {

    DEFAULT_MODERATION(0),
    LIGHT_MODERATION(1),
    MANUAL_CHECK(2),
    BLOCKED(3),
    ;
    private final int code;

    DecisionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
