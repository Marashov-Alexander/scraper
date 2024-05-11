package study.polytech.scraper.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "t_url", schema = "public", catalog = "Scraper")
public class TUrlEntity {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "base_url")
    private String baseUrl;
    @Basic
    @Column(name = "utm_template")
    private String utmTemplate;
    @Basic
    @Column(name = "date_created")
    private long dateCreated;
    @Basic
    @Column(name = "date_updated")
    private long dateUpdated;
    @Basic
    @Column(name = "date_referece_updated")
    private long dateRefereceUpdated;
    @Basic
    @Column(name = "date_deviation_updated")
    private long dateDeviationUpdated;
    @Basic
    @Column(name = "date_last_check")
    private long dateLastCheck;
    @Basic
    @Column(name = "domain_age_in_days")
    private int domainAgeInDays;
    @Basic
    @Column(name = "reference_screenshot_hash")
    private Long referenceScreenshotHash;
    @Basic
    @Column(name = "reference_light_screenshot_hash")
    private Long referenceLightScreenshotHash;
    @Basic
    @Column(name = "deviation_screenshot_hash")
    private Long deviationScreenshotHash;
    @Basic
    @Column(name = "deviation_light_screenshot_hash")
    private Long deviationLightScreenshotHash;
    @Basic
    @Column(name = "decision_status")
    private int decisionStatus;
    @Basic
    @Column(name = "light_screenshot_distance")
    private Integer lightScreenshotDistance;

    @Basic
    @Column(name = "ref_screenshot_name")
    private String refScreenshotName;

    @Basic
    @Column(name = "ref_light_screenshot_name")
    private String refLightScreenshotName;

    @Basic
    @Column(name = "dev_screenshot_name")
    private String devScreenshotName;

    @Basic
    @Column(name = "dev_light_screenshot_name")
    private String devLightScreenshotName;

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

    public String getUtmTemplate() {
        return utmTemplate;
    }

    public void setUtmTemplate(String utmTemplate) {
        this.utmTemplate = utmTemplate;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public long getDateRefereceUpdated() {
        return dateRefereceUpdated;
    }

    public void setDateRefereceUpdated(long dateRefereceUpdated) {
        this.dateRefereceUpdated = dateRefereceUpdated;
    }

    public long getDateDeviationUpdated() {
        return dateDeviationUpdated;
    }

    public void setDateDeviationUpdated(long dateDeviationUpdated) {
        this.dateDeviationUpdated = dateDeviationUpdated;
    }

    public long getDateLastCheck() {
        return dateLastCheck;
    }

    public void setDateLastCheck(long dateLastCheck) {
        this.dateLastCheck = dateLastCheck;
    }

    public int getDomainAgeInDays() {
        return domainAgeInDays;
    }

    public void setDomainAgeInDays(int domainAgeInDays) {
        this.domainAgeInDays = domainAgeInDays;
    }

    public Long getReferenceScreenshotHash() {
        return referenceScreenshotHash;
    }

    public void setReferenceScreenshotHash(Long referenceScreenshotHash) {
        this.referenceScreenshotHash = referenceScreenshotHash;
    }

    public Long getReferenceLightScreenshotHash() {
        return referenceLightScreenshotHash;
    }

    public void setReferenceLightScreenshotHash(Long referenceLightScreenshotHash) {
        this.referenceLightScreenshotHash = referenceLightScreenshotHash;
    }

    public Long getDeviationScreenshotHash() {
        return deviationScreenshotHash;
    }

    public void setDeviationScreenshotHash(Long deviationScreenshotHash) {
        this.deviationScreenshotHash = deviationScreenshotHash;
    }

    public Long getDeviationLightScreenshotHash() {
        return deviationLightScreenshotHash;
    }

    public void setDeviationLightScreenshotHash(Long deviationLightScreenshotHash) {
        this.deviationLightScreenshotHash = deviationLightScreenshotHash;
    }

    public int getDecisionStatus() {
        return decisionStatus;
    }

    public void setDecisionStatus(int decisionStatus) {
        this.decisionStatus = decisionStatus;
    }

    public Integer getLightScreenshotDistance() {
        return lightScreenshotDistance;
    }

    public void setLightScreenshotDistance(Integer lightScreenshotDistance) {
        this.lightScreenshotDistance = lightScreenshotDistance;
    }

    public String getRefScreenshotName() {
        return refScreenshotName;
    }

    public void setRefScreenshotName(String refScreenshotName) {
        this.refScreenshotName = refScreenshotName;
    }

    public String getRefLightScreenshotName() {
        return refLightScreenshotName;
    }

    public void setRefLightScreenshotName(String refLightScreenshotName) {
        this.refLightScreenshotName = refLightScreenshotName;
    }

    public String getDevScreenshotName() {
        return devScreenshotName;
    }

    public void setDevScreenshotName(String devScreenshotName) {
        this.devScreenshotName = devScreenshotName;
    }

    public String getDevLightScreenshotName() {
        return devLightScreenshotName;
    }

    public void setDevLightScreenshotName(String devLightScreenshotName) {
        this.devLightScreenshotName = devLightScreenshotName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TUrlEntity that = (TUrlEntity) o;

        return new EqualsBuilder().append(id, that.id).append(dateCreated, that.dateCreated)
                .append(dateUpdated, that.dateUpdated).append(dateRefereceUpdated, that.dateRefereceUpdated)
                .append(dateDeviationUpdated, that.dateDeviationUpdated).append(dateLastCheck, that.dateLastCheck)
                .append(domainAgeInDays, that.domainAgeInDays).append(decisionStatus, that.decisionStatus)
                .append(baseUrl, that.baseUrl).append(utmTemplate, that.utmTemplate)
                .append(referenceScreenshotHash, that.referenceScreenshotHash)
                .append(referenceLightScreenshotHash, that.referenceLightScreenshotHash)
                .append(deviationScreenshotHash, that.deviationScreenshotHash)
                .append(deviationLightScreenshotHash, that.deviationLightScreenshotHash)
                .append(lightScreenshotDistance, that.lightScreenshotDistance)
                .append(refScreenshotName, that.refScreenshotName)
                .append(devScreenshotName, that.devScreenshotName)
                .append(refLightScreenshotName, that.refLightScreenshotName)
                .append(devLightScreenshotName, that.devLightScreenshotName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(baseUrl)
                .append(utmTemplate).append(dateCreated).append(dateUpdated).append(dateRefereceUpdated)
                .append(dateDeviationUpdated).append(dateLastCheck).append(domainAgeInDays).append(referenceScreenshotHash)
                .append(referenceLightScreenshotHash).append(deviationScreenshotHash).append(deviationLightScreenshotHash)
                .append(decisionStatus).append(lightScreenshotDistance)
                .append(refScreenshotName).append(devScreenshotName).append(refLightScreenshotName).append(devLightScreenshotName)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TUrlEntity{" +
                "id=" + id +
                ", baseUrl='" + baseUrl + '\'' +
                ", utmTemplate='" + utmTemplate + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", dateRefereceUpdated=" + dateRefereceUpdated +
                ", dateDeviationUpdated=" + dateDeviationUpdated +
                ", dateLastCheck=" + dateLastCheck +
                ", domainAgeInDays=" + domainAgeInDays +
                ", referenceScreenshotHash=" + referenceScreenshotHash +
                ", referenceLightScreenshotHash=" + referenceLightScreenshotHash +
                ", deviationScreenshotHash=" + deviationScreenshotHash +
                ", deviationLightScreenshotHash=" + deviationLightScreenshotHash +
                ", decisionStatus=" + decisionStatus +
                ", lightScreenshotDistance=" + lightScreenshotDistance +
                ", refScreenshotName=" + refScreenshotName +
                ", refLightScreenshotName=" + refLightScreenshotName +
                ", devScreenshotName=" + devScreenshotName +
                ", devLightScreenshotName=" + devLightScreenshotName +
                '}';
    }
}
