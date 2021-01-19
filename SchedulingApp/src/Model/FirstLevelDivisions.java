package Model;

import java.time.LocalDateTime;

/**
 * First Level Divisions Class
 * Used to compare and organize countries
 */
public class FirstLevelDivisions {
    public Integer divisionId;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * First Level Division Primary Constructor
     * @param divisionId
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdated
     * @param lastUpdatedBy
     * @param countryId
     */
    public FirstLevelDivisions(Integer divisionId, String division, LocalDateTime createDate, String createdBy,
                               LocalDateTime lastUpdated, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     * First Level Divisions ID-Only Constructor
     * @param divisionId - Unique Division ID
     */
    public FirstLevelDivisions(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Integer getDivisionId() { return divisionId; }

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
        if (division != null) {
           return "Division: " + division + " - ID: " + divisionId;
        } else {
           return "Division ID: " + divisionId;
        }
    }

}
