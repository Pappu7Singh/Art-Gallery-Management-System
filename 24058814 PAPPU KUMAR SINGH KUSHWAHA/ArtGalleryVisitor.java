


public abstract class ArtGalleryVisitor {
    protected int visitorId;
    protected String fullName;
    protected String gender;
    protected String contactNumber;
    protected String registrationDate;
    protected double ticketCost;
    protected String ticketType;
    protected int visitCount;
    protected double rewardPoints;
    protected final int cancelLimit = 3;
    protected int cancelCount;
    protected String cancellationReason;
    protected double refundableAmount;
    protected boolean isActive;
    protected boolean isBought;
    protected int buyCount;
    protected double finalPrice;
    protected double discountAmount;
    protected String artworkName;
    protected double artworkPrice;

    // Constructor
    public ArtGalleryVisitor(int visitorId, String fullName, String gender, String contactNumber, 
                             String registrationDate, double ticketCost, String ticketType) {
        this.visitorId = visitorId;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.registrationDate = registrationDate;
        this.ticketCost = ticketCost;
        this.ticketType = ticketType;
        this.visitCount = 0;
        this.rewardPoints = 0;
        this.cancelCount = 0;
        this.buyCount = 0;
        this.discountAmount = 0;
        this.finalPrice = 0;
        this.refundableAmount = 0;
        this.isActive = false;
        this.isBought = false;
        this.cancellationReason = "";
        this.artworkName = null;
        this.artworkPrice = 0;
    }

    // Accessors (getters)
    public int getVisitorId() { return visitorId; }
    public String getFullName() { return fullName; }
    public String getGender() { return gender; }
    public String getContactNumber() { return contactNumber; }
    public String getRegistrationDate() { return registrationDate; }
    public double getTicketCost() { return ticketCost; }
    public String getTicketType() { return ticketType; }
    public int getVisitCount() { return visitCount; }
    public double getRewardPoints() { return rewardPoints; }
    public int getCancelCount() { return cancelCount; }
    public int getCancelLimit() {return cancelLimit;}
    public String getCancellationReason() { return cancellationReason; }
    public double getRefundableAmount() { return refundableAmount; }
    public boolean getIsActive() { return isActive; }
    public boolean getIsBought() { return isBought; }
    public int getBuyCount() { return buyCount; }
    public double getFinalPrice() { return finalPrice; }
    public double getDiscountAmount() { return discountAmount; }
    public String getArtworkName() { return artworkName; }
    public double getArtworkPrice() { return artworkPrice; }

    // Mutators (setters)
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setGender(String gender) { this.gender = gender; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    // Method to log visit
    public void logVisit() {
        this.visitCount++;
        this.isActive = true;
    }
 
    // Abstract methods
    public abstract String buyProduct(String artworkName, double artworkPrice);
    public abstract double calculateDiscount();
    public abstract double calculateRewardPoint();
    public abstract String cancelProduct(String artworkName, String cancellationReason);
    public abstract void generateBill();
     
    // Display method
    public void display() {
        System.out.println("Visitor ID: " + visitorId);
        System.out.println("Full Name: " + fullName);
        System.out.println("Gender: " + gender);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Ticket Type: " + ticketType);
        System.out.println("Ticket Cost: " + ticketCost);
        System.out.println("Visit Count: " + visitCount);
        System.out.println("Reward Points: " + rewardPoints);
        System.out.println("Cancel Count: " + cancelCount);
        System.out.println("Cancellation Reason: " + cancellationReason);
        System.out.println("Refundable Amount: " + refundableAmount);
        System.out.println("Active Status: " + isActive);
        System.out.println("Product Bought: " + isBought);
        System.out.println("Buy Count: " + buyCount);
        System.out.println("Final Price: " + finalPrice);
        System.out.println("Discount Amount: " + discountAmount);
        System.out.println("Artwork Name: " + artworkName);
        System.out.println("Artwork Price: " + artworkPrice);
    }
}

