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
@Table(name = "t_profile", schema = "public", catalog = "Scraper")
public class TProfileEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "width")
    private int width;
    @Basic
    @Column(name = "height")
    private int height;
    @Basic
    @Column(name = "hardware_concurrency")
    private int hardwareConcurrency;
    @Basic
    @Column(name = "max_touch_points")
    private int maxTouchPoints;
    @Basic
    @Column(name = "user_agent")
    private String userAgent;
    @Basic
    @Column(name = "accept_language")
    private String acceptLanguage;
    @Basic
    @Column(name = "brands_versions")
    private String brandsVersions;
    @Basic
    @Column(name = "full_version")
    private String fullVersion;
    @Basic
    @Column(name = "platform")
    private String platform;
    @Basic
    @Column(name = "platform_version")
    private String platformVersion;
    @Basic
    @Column(name = "architecture")
    private String architecture;
    @Basic
    @Column(name = "model")
    private String model;
    @Basic
    @Column(name = "is_mobile")
    private boolean isMobile;

    public TProfileEntity() {

    }

    public TProfileEntity(int id, int width, int height, int hardwareConcurrency, int maxTouchPoints,
                          String userAgent, String acceptLanguage, String brandsVersions, String fullVersion,
                          String platform, String platformVersion, String architecture, String model, boolean isMobile) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.hardwareConcurrency = hardwareConcurrency;
        this.maxTouchPoints = maxTouchPoints;
        this.userAgent = userAgent;
        this.acceptLanguage = acceptLanguage;
        this.brandsVersions = brandsVersions;
        this.fullVersion = fullVersion;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.architecture = architecture;
        this.model = model;
        this.isMobile = isMobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHardwareConcurrency() {
        return hardwareConcurrency;
    }

    public void setHardwareConcurrency(int hardwareConcurrency) {
        this.hardwareConcurrency = hardwareConcurrency;
    }

    public int getMaxTouchPoints() {
        return maxTouchPoints;
    }

    public void setMaxTouchPoints(int maxTouchPoints) {
        this.maxTouchPoints = maxTouchPoints;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getBrandsVersions() {
        return brandsVersions;
    }

    public void setBrandsVersions(String brandsVersions) {
        this.brandsVersions = brandsVersions;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public void setFullVersion(String fullVersion) {
        this.fullVersion = fullVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TProfileEntity that = (TProfileEntity) o;

        return new EqualsBuilder().append(id, that.id).append(width, that.width).append(height, that.height).append(hardwareConcurrency, that.hardwareConcurrency).append(maxTouchPoints, that.maxTouchPoints).append(isMobile, that.isMobile).append(userAgent, that.userAgent).append(acceptLanguage, that.acceptLanguage).append(brandsVersions, that.brandsVersions).append(fullVersion, that.fullVersion).append(platform, that.platform).append(platformVersion, that.platformVersion).append(architecture, that.architecture).append(model, that.model).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(width).append(height).append(hardwareConcurrency).append(maxTouchPoints).append(userAgent).append(acceptLanguage).append(brandsVersions).append(fullVersion).append(platform).append(platformVersion).append(architecture).append(model).append(isMobile).toHashCode();
    }

    @Override
    public String toString() {
        return "TProfileEntity{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                ", hardwareConcurrency=" + hardwareConcurrency +
                ", maxTouchPoints=" + maxTouchPoints +
                ", userAgent='" + userAgent + '\'' +
                ", acceptLanguage='" + acceptLanguage + '\'' +
                ", brandsVersions='" + brandsVersions + '\'' +
                ", fullVersion='" + fullVersion + '\'' +
                ", platform='" + platform + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", architecture='" + architecture + '\'' +
                ", model='" + model + '\'' +
                ", isMobile=" + isMobile +
                '}';
    }
}
