

public class EliteVisitor extends ArtGalleryVisitor {
    private boolean assignedPersonalArtAdvisor;
    private boolean exclusiveEventAccess;

    // Constructor
    public EliteVisitor(int visitorId, String fullName, String gender, String contactNumber,
                        String registrationDate, double ticketCost, String ticketType) {
        super(visitorId, fullName, gender, contactNumber, registrationDate, ticketCost, ticketType);
        this.assignedPersonalArtAdvisor = false;
        this.exclusiveEventAccess = false;
    }

    // Accessor methods
    public boolean getAssignedPersonalArtAdvisor() {
        return assignedPersonalArtAdvisor;
    }

    public boolean getExclusiveEventAccess() {
        return exclusiveEventAccess;
    }

    // Assign Personal Art Advisor
    public boolean assignPersonalArtAdvisor() {
        if (rewardPoints > 5000) {
            assignedPersonalArtAdvisor = true;
        }
        return assignedPersonalArtAdvisor;
    }

    // Check Exclusive Event Access
    public boolean exclusiveEventAccess() {
        if (assignedPersonalArtAdvisor) {
            exclusiveEventAccess = true;
        }
        return exclusiveEventAccess;
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
            isBought = true;
            buyCount++;
            return "Purchase successful for artwork: " + artworkName;
        }
        return "You have already purchased this artwork.";
    }

    // Calculate Discount
    @Override
    public double calculateDiscount() {
        if (!isBought) return 0;
        discountAmount = artworkPrice * 0.40;
        finalPrice = artworkPrice - discountAmount;
        return discountAmount;
    }

    // Calculate Reward Points
    @Override
    public double calculateRewardPoint() {
        if (!isBought) return 0;
        rewardPoints += finalPrice * 10;
        return rewardPoints;
    }

    // Generate Bill
    @Override
    public void generateBill() {
        if (!isBought) {
            System.out.println("No purchase made. Cannot generate bill.");
            return;
        }
        System.out.println("----- Elite Visitor Bill -----");
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
        assignedPersonalArtAdvisor = false;
        exclusiveEventAccess = false;
        visitCount = 0;
        cancelCount = 0;
        rewardPoints = 0;
    }

    // Cancel Product
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
                refundableAmount = artworkPrice - (artworkPrice * 0.05);
                rewardPoints -= finalPrice * 10;
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
        System.out.println("Assigned Personal Art Advisor: " + assignedPersonalArtAdvisor);
        System.out.println("Exclusive Event Access: " + exclusiveEventAccess);
    }
}

