package Model;

import java.time.LocalDateTime;

/**
 * Appointments Class
 */
public class Appointments {
    private final int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Appointments Class primary constructor, includes all appointment parameters.
     */
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy,
                        LocalDateTime lastUpdated, String lastUpdatedBy, int customerId, int userId, int contactId) {
        this.appointmentId = appointmentId;
        this.title=title;
        this.description=description;
        this.location=location;
        this.type=type;
        this.start=start;
        this.end=end;
        this.createDate=createDate;
        this.createdBy=createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy=lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Appointment Contact and Customer Schedule constructor,
     * same as other constructors, excluding appropriate parameters
     */
    public Appointments(int appointmentId, String title, String description, String type,
                        LocalDateTime start, LocalDateTime end, int customerId) {
        this.appointmentId = appointmentId;
        this.title=title;
        this.description=description;
        this.type=type;
        this.start=start;
        this.end=end;
        this.customerId = customerId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
      return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
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

    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public int getContactId() {
        return contactId;
    }

}
