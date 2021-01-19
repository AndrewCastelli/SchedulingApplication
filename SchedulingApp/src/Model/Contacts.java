package Model;

/**
 * Contacts Class
 */
public class Contacts {
    private final int contactID;
    private final String contactName;
    private String contactEmail;

    /**
     * Primary Constructor (email is never used)
     */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /**
     * Contacts Constructor for Appointments Data
     * @param contactId - Contact ID
     * @param contactName - Contact Name
     */
    public Contacts(int contactId, String contactName) {
        this.contactID = contactId;
        this.contactName = contactName;
    }

    public int getContactID() { return contactID; }

    public String getContactName() {
        return contactName;
    }

    @Override
    public String toString() { return "ID: " + contactID + " - " + contactName; }

}
