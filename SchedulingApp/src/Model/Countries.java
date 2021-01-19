package Model;

import java.time.LocalDateTime;

/**
 * Countries Class
 */
public class Countries {
    private Integer countryId;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdateBy;

    /**
     * Primary Countries Constructor
     * @param countryId - Unique ID
     * @param country - Country Name
     * @param createDate - Date of creation
     * @param createdBy - User who created record
     * @param lastUpdated - Date of last update
     * @param lastUpdateBy - User who last updated
     */
    public Countries(Integer countryId, String country, LocalDateTime createDate,
                     String createdBy, LocalDateTime lastUpdated, String lastUpdateBy) {
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Countries(String country) {
        this.country=country;
    }

    public String getCountry() {
        return country;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return country;
    }

}
