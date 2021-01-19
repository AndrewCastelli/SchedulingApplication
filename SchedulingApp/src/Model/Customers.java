package Model;

import java.time.LocalDateTime;

/**
 * Customers Class
 * Many of the parameters were not required
 */
public class Customers {
    public int customerId;
    public String customerName;
    private String address;
    private String postal;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;
    private int divisionId;

    /**
     * Primary Customers Constructor
     * @param customerId
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param createDate
     * @param createdBy
     * @param lastUpdated
     * @param lastUpdatedBy
     * @param divisionId
     */
    public Customers(int customerId, String customerName, String address, String postalCode,
                     String phone, LocalDateTime createDate, String createdBy,
                     LocalDateTime lastUpdated, String lastUpdatedBy, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address =  address;
        this.postal = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public int getCustomerId() { return customerId; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }

    public String getAddress() { return address; }

    public String getPostal() { return postal; }

    public String getPhone() { return phone; }

    public LocalDateTime getCreateDate() { return createDate; }

    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public String getCreatedBy() { return createdBy; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public String getLastUpdatedBy() { return lastUpdatedBy; }

    public int getDivisionId() { return divisionId; }

    @Override
    public String toString() { return customerName; }

}
