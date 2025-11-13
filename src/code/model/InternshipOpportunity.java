package code.model;

import code.enums.InternshipLevel;
import code.enums.OpportunityStatus;
import java.time.LocalDate;

// Represents an internship opportunity posted by a company.
public class InternshipOpportunity {

    private final String id;
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private OpportunityStatus status = OpportunityStatus.PENDING;
    private String companyName;
    private CompanyRepresentative createdBy;
    private int maxSlots;
    private int confirmedSlots;
    private boolean visible;

    public InternshipOpportunity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyRepresentative getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CompanyRepresentative createdBy) {
        this.createdBy = createdBy;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getConfirmedSlots() {
        return confirmedSlots;
    }

    public void setConfirmedSlots(int confirmedSlots) {
        this.confirmedSlots = confirmedSlots;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /*
     * An opportunity is available if:
     * - It is approved by career center staff
     * - It is visible to students
     * - It has available slots (confirmed slots < max slots)
     * - The application period is still open (today is between opening and closing
     * dates)
     * //today refers to the current date we can swap it out for a hard set date if
     * needed
     */
    public boolean isAvailable() {
        LocalDate today = LocalDate.now();

        return status == OpportunityStatus.APPROVED
                && visible
                && confirmedSlots < maxSlots
                && !today.isBefore(openingDate)
                && !today.isAfter(closingDate);
    }
}
