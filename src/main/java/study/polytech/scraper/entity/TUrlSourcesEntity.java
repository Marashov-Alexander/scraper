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
@Table(name = "t_url_sources", schema = "public", catalog = "Scraper")
public class TUrlSourcesEntity {

    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "variables")
    private String variables;
    @Basic
    @Column(name = "final_url")
    private String finalUrl;
    @Basic
    @Column(name = "source_url")
    private String sourceUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TUrlSourcesEntity that = (TUrlSourcesEntity) o;

        return new EqualsBuilder().append(id, that.id).append(code, that.code).append(variables, that.variables).append(finalUrl, that.finalUrl).append(sourceUrl, that.sourceUrl).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).append(variables).append(finalUrl).append(sourceUrl).toHashCode();
    }
}
