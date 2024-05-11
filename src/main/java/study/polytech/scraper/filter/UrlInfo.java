package study.polytech.scraper.filter;

public class UrlInfo {

    private long id;
    private String baseUrl;
    private Integer ageInDays;
    private int decisionStatus;

    public UrlInfo() {

    }

    public UrlInfo(long id, String baseUrl, Integer ageInDays, int decisionStatus) {
        this.id = id;
        this.baseUrl = baseUrl;
        this.ageInDays = ageInDays;
        this.decisionStatus = decisionStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Integer getAgeInDays() {
        return ageInDays;
    }

    public void setAgeInDays(Integer ageInDays) {
        this.ageInDays = ageInDays;
    }

    public int getDecisionStatus() {
        return decisionStatus;
    }

    public void setDecisionStatus(int decisionStatus) {
        this.decisionStatus = decisionStatus;
    }
}
