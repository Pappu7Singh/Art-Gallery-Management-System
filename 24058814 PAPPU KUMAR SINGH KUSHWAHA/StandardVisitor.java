

public class StandardVisitor extends ArtGalleryVisitor {
    private boolean isEligibleForDiscountUpgrade;
    private final int visitLimit;
    private float discountPercent;

    // Constructor
    public StandardVisitor(int visitorId, String fullName, String gender, String contactNumber,
                           String registrationDate, double ticketCost, String ticketType) {
        super(visitorId, fullName, gender, contactNumber, registrationDate, ticketCost, ticketType);
        this.visitLimit = 5;
        this.discountPercent = 0.10f;
        this.isEligibleForDiscountUpgrade = false;
    }

    // Accessors
    public boolean getIsEligibleForDiscountUpgrade() { return isEligibleForDiscountUpgrade; }
    public int getVisitLimit() { return visitLimit; }
    public float getDiscountPercent() { return discountPercent; }

    // Check if visitor gets discount upgrade
    public boolean checkDiscountUpgrade() {
        if (visitCount >= visitLimit) {
            isEligibleForDiscountUpgrade = true;
            discountPercent = 0.15f;
        }
        return isEligibleForDiscountUpgrade;
    }

    // Buy product
    @Override
    public String buyProduct(String artworkName, double artworkPrice) {
        if (!isActive) {
            return "Please log in before making a purchase.";
        }
        if (!isBought || !this.artworkName.equals(artworkName)) {
            this.artworkName = artworkName;
            this.artworkPrice = artworkPrice;
            this.isBought = true;
            this.buyCount++;
            return "Purchase successful for artwork: " + artworkName;
        }
        return "You have already purchased this artwork.";
    }

    // Calculate discount
    @Override
    public double calculateDiscount() {
        if (!isBought) return 0;
        checkDiscountUpgrade();
        discountAmount = artworkPrice * discountPercent;
        finalPrice = artworkPrice - discountAmount;
        return discountAmount;
    }

    // Calculate reward point
    @Override
    public double calculateRewardPoint() {
        if (!isBought) return 0;
        rewardPoints += finalPrice * 5;
        return rewardPoints;
    }

    // Generate bill
    @Override
    public void generateBill() {
        if (!isBought) {
            System.out.println("No purchase made. Cannot generate bill.");
            return;
        }
        System.out.println("----- Standard Visitor Bill -----");
        System.out.println("Visitor ID: " + visitorId);
        System.out.println("Visitor Name: " + fullName);
        System.out.println("Artwork Name: " + artworkName);
        System.out.println("Artwork Price: " + artworkPrice);
        System.out.println("Discount Amount: " + discountAmount);
        System.out.println("Final Price: " + finalPrice);
        System.out.println("----------------------------------");
    }

    // Private terminate method
    private void terminateVisitor() {
        isActive = false;
        isEligibleForDiscountUpgrade = false;
        visitCount = 0;
        cancelCount = 0;
        rewardPoints = 0;
    }

    // Cancel product
    @Override
    public String cancelProduct(String artworkName, String cancellationReason) {
        if (cancelCount >= cancelLimit) {
            terminateVisitor();
            return "Cancellation limit reached. Visitor account terminated.";
        }
        if (buyCount > 0) {
            if (this.artworkName.equals(artworkName)) {
                this.artworkName = null;
                isBought = false;
                refundableAmount = artworkPrice - (artworkPrice * 0.10);
                rewardPoints -= finalPrice * 5;
                cancelCount++;
                buyCount--;
                this.cancellationReason = cancellationReason;
                return "Product cancelled. Refund: " + refundableAmount;
            } else {
                return "Artwork name does not match the purchased product.";
            }
        }
        return "No product to cancel.";
    }

    // Display method
    @Override
    public void display() {
        super.display();
        System.out.println("Eligible for Discount Upgrade: " + isEligibleForDiscountUpgrade);
        System.out.println("Visit Limit: " + visitLimit);
        System.out.println("Discount Percent: " + discountPercent);
    }
    
}



