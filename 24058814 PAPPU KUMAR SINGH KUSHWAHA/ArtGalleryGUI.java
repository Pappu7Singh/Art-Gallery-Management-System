import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class ArtGalleryGUI extends JFrame {
    private ArrayList<ArtGalleryVisitor> visitors = new ArrayList<>();

    private JTextField txtVisitorID, txtFullName, txtContact, txtTicketPrice;
    private JComboBox<String> comboTicketType;
    private JRadioButton rbMale, rbFemale, rbOther;
    private ButtonGroup genderGroup;

    private JTextField txtArtworkName, txtArtworkPrice;
    private JTextArea txtCancelReason;
    private JComboBox<String> cbDay, cbMonth, cbYear;

    private JButton btnClear, btnAddVisitor, btnLogVisit;
    private JButton btnBuy, btnCancel, btnBill;
    private JButton btnSaveFile, btnReadFile, btnCalculateDiscount, btnCalculateRewardPoints;
    private JButton btnCheckUpgrade, btnDisplayDetails, btnAssignAdvisor;
    private JTextArea txtDisplayArea;

    public ArtGalleryGUI() {
        setTitle("Art Gallery System");
        setSize(800, 700);
        setLayout(new GridLayout(3, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel 1: Visitor Details
        JPanel visitorPanel = new JPanel(null);
        visitorPanel.setBorder(BorderFactory.createTitledBorder("Visitor Details"));

        JLabel lblVisitorID = new JLabel("Visitor ID");
        txtVisitorID = new JTextField();
        JLabel lblTicketType = new JLabel("Ticket Type");
        comboTicketType = new JComboBox<>(new String[]{"Select","Standard", "Elite"});
        JLabel lblFullName = new JLabel("Full Name");
        txtFullName = new JTextField();
        JLabel lblTicketPrice = new JLabel("Ticket Price");
        txtTicketPrice = new JTextField();
        txtTicketPrice.setEditable(false);
        JLabel lblContact = new JLabel("Contact");
        txtContact = new JTextField();
        JLabel lblGender = new JLabel("Gender");
        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");
        rbOther = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);
        genderGroup.add(rbOther);

        JLabel lblDate = new JLabel("Registration Date");
        cbDay = new JComboBox<>();
        for(int i=1;i<=31;i++) cbDay.addItem(String.valueOf(i));
        cbMonth = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        cbYear = new JComboBox<>();
        for(int i=2020;i<=2030;i++) cbYear.addItem(String.valueOf(i));

        btnClear = new JButton("Clear");
        btnClear.setBackground(new Color(51, 102, 255));
        btnClear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txtVisitorID.setText("");
                    txtFullName.setText("");
                    txtContact.setText("");
                    txtTicketPrice.setText("");
                    comboTicketType.setSelectedIndex(0);
                    genderGroup.clearSelection();
                    txtArtworkName.setText("");
                    txtCancelReason.setText("Write your reason");
                    txtArtworkPrice.setText("");

                }
            });

        btnAddVisitor = new JButton("Add Visitor");
        btnAddVisitor.setBackground(new Color(51, 102, 255));
        btnAddVisitor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 1. Validate and parse Visitor ID
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Visitor ID is required.");
                            return;
                        }
                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        // Check for duplicate ID
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                JOptionPane.showMessageDialog(null, "Visitor ID already exists.");
                                return;
                            }
                        }

                        // 2. Validate Full Name
                        String fullName = txtFullName.getText().trim();
                        if (fullName.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Full Name is required.");
                            return;
                        }
                        if (!fullName.matches("[a-zA-Z ]+")) {
                            JOptionPane.showMessageDialog(null, "Full Name must contain only letters.");
                            return;
                        }

                        // 3. Validate Gender
                        String gender = null;
                        if (rbMale.isSelected()) gender = "Male";
                        else if (rbFemale.isSelected()) gender = "Female";
                        else if (rbOther.isSelected()) gender = "Other";
                        else {
                            JOptionPane.showMessageDialog(null, "Please select Gender.");
                            return;
                        }

                        // 4. Validate Contact Number
                        String contact = txtContact.getText().trim();
                        if (contact.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Contact Number is required.");
                            return;
                        }
                        if (!contact.matches("\\d{10}")) {
                            JOptionPane.showMessageDialog(null, "Contact Number must be 10 digits.");
                            return;
                        }

                        // 5. Validate Ticket Type
                        String ticketType = (String) comboTicketType.getSelectedItem();
                        if (ticketType.equals("Select")) {
                            JOptionPane.showMessageDialog(null, "Please select a valid Ticket Type.");
                            return;
                        }

                        // 6. Validate Ticket Price
                        String priceText = txtTicketPrice.getText().trim();
                        if (priceText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Ticket Price is required.");
                            return;
                        }
                        double ticketPrice;
                        try {
                            ticketPrice = Double.parseDouble(priceText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Ticket Price must be a valid number.");
                            return;
                        }

                        // 7. Get Registration Date
                        String day = (String) cbDay.getSelectedItem();
                        String month = (String) cbMonth.getSelectedItem();
                        String year = (String) cbYear.getSelectedItem();
                        String registrationDate = day + "-" + month + "-" + year;

                        // 8. Create Visitor Object
                        ArtGalleryVisitor visitor;
                        if (ticketType.equals("Standard")) {
                            visitor = new StandardVisitor(visitorId, fullName, gender, contact, registrationDate, ticketPrice, ticketType);
                        } else {
                            visitor = new EliteVisitor(visitorId, fullName, gender, contact, registrationDate, ticketPrice, ticketType);
                        }

                        // 9. Add to ArrayList
                        visitors.add(visitor);
                        JOptionPane.showMessageDialog(null, "Visitor added successfully!");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage());
                    }
                }
            });

        btnLogVisit = new JButton("Log Visit");
        btnLogVisit.setBackground(new Color(51, 102, 255));
        btnLogVisit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        boolean found = false;
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                v.logVisit();  // Method from abstract class
                                JOptionPane.showMessageDialog(null, "Visit logged for Visitor ID: " + visitorId);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        //Ticket price based on ticket type
        comboTicketType.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedType = (String) comboTicketType.getSelectedItem();
                        if (selectedType.equals("Standard")) {
                            txtTicketPrice.setText("1000.0");
                        } else if (selectedType.equals("Elite")) {
                            txtTicketPrice.setText("2000.0");
                        }
                    }
                }
            });

        lblVisitorID.setBounds(20, 30, 100, 25);
        txtVisitorID.setBounds(120, 30, 150, 25);
        lblTicketType.setBounds(300, 30, 100, 25);
        comboTicketType.setBounds(400, 30, 150, 25);
        lblFullName.setBounds(20, 70, 100, 25);
        txtFullName.setBounds(120, 70, 150, 25);
        lblTicketPrice.setBounds(300, 70, 100, 25);
        txtTicketPrice.setBounds(400, 70, 150, 25);
        lblContact.setBounds(20, 110, 100, 25);
        txtContact.setBounds(120, 110, 150, 25);
        lblGender.setBounds(300, 110, 100, 25);
        rbMale.setBounds(400, 110, 60, 25);
        rbFemale.setBounds(470, 110, 70, 25);
        rbOther.setBounds(550, 110, 70, 25);

        lblDate.setBounds(300, 150, 150, 25);
        cbDay.setBounds(460, 150, 50, 25);
        cbMonth.setBounds(510, 150, 70, 25);
        cbYear.setBounds(580, 150, 70, 25);

        btnClear.setBounds(120, 250, 100, 30);
        btnAddVisitor.setBounds(230, 250, 130, 30);
        btnLogVisit.setBounds(370, 250, 130, 30);

        visitorPanel.add(lblVisitorID); visitorPanel.add(txtVisitorID);
        visitorPanel.add(lblTicketType); visitorPanel.add(comboTicketType);
        visitorPanel.add(lblFullName); visitorPanel.add(txtFullName);
        visitorPanel.add(lblTicketPrice); visitorPanel.add(txtTicketPrice);
        visitorPanel.add(lblContact); visitorPanel.add(txtContact);
        visitorPanel.add(lblGender); visitorPanel.add(rbMale);
        visitorPanel.add(rbFemale); visitorPanel.add(rbOther);
        visitorPanel.add(lblDate); visitorPanel.add(cbDay);
        visitorPanel.add(cbMonth); visitorPanel.add(cbYear);
        visitorPanel.add(btnClear); visitorPanel.add(btnAddVisitor);
        visitorPanel.add(btnLogVisit);

        // Panel 2: Purchase Section
        JPanel purchasePanel = new JPanel(null);
        purchasePanel.setBorder(BorderFactory.createTitledBorder("Purchase Section"));

        JLabel lblArtworkName = new JLabel("Artwork Name");
        txtArtworkName = new JTextField();
        JLabel lblArtworkPrice = new JLabel("Artwork Price");
        txtArtworkPrice = new JTextField();
        JLabel lblCancelReason = new JLabel("Cancel Reason");
        txtCancelReason = new JTextArea();
        JScrollPane scrollCancel = new JScrollPane(txtCancelReason);

        btnBuy = new JButton("Buy");
        btnBuy.setBackground(new Color(51, 102, 255));
        btnBuy.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 1. Validate Visitor ID
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        // 2. Validate Artwork Name
                        String artworkName = txtArtworkName.getText().trim();
                        if (artworkName.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Artwork name is required.");
                            return;
                        }

                        // 3. Validate Artwork Price
                        String artworkPriceText = txtArtworkPrice.getText().trim();
                        if (artworkPriceText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Artwork price is required.");
                            return;
                        }

                        double artworkPrice;
                        try {
                            artworkPrice = Double.parseDouble(artworkPriceText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Artwork price must be a valid number.");
                            return;
                        }

                        // 4. Search for visitor and buy product
                        boolean found = false;
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                String result = v.buyProduct(artworkName, artworkPrice);
                                JOptionPane.showMessageDialog(null, result);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(51, 102, 255));
        btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 1. Validate Visitor ID
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        // 2. Validate Artwork Name
                        String artworkName = txtArtworkName.getText().trim();
                        if (artworkName.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Artwork Name.");
                            return;
                        }

                        // 3. Validate Cancellation Reason
                        String cancellationReason = txtCancelReason.getText().trim();
                        if (cancellationReason.isEmpty() || cancellationReason.equalsIgnoreCase("Write your reason")) {
                            JOptionPane.showMessageDialog(null, "Please enter Cancellation Reason.");
                            return;
                        }

                        // 4. Find Visitor and Cancel Product
                        boolean found = false;
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                String result = v.cancelProduct(artworkName, cancellationReason);
                                JOptionPane.showMessageDialog(null, result);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        btnBill = new JButton("Bill");
        btnBill.setBackground(new Color(51, 102, 255));
        btnBill.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 1. Get and validate Visitor ID
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        // 2. Search for the visitor
                        boolean found = false;
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                if (!v.getIsBought()) {
                                    JOptionPane.showMessageDialog(null, "No product has been bought by this visitor.");
                                    return;
                                }

                                v.generateBill();  // Console output

                                // 3. Prepare bill content
                                StringBuilder bill = new StringBuilder();
                                bill.append("----- Visitor Bill -----\n");
                                bill.append("Visitor ID: ").append(v.getVisitorId()).append("\n");
                                bill.append("Name: ").append(v.getFullName()).append("\n");
                                bill.append("Artwork Name: ").append(v.getArtworkName()).append("\n");
                                bill.append("Artwork Price: ").append(v.getArtworkPrice()).append("\n");
                                bill.append("Discount: ").append(v.getDiscountAmount()).append("\n");
                                bill.append("Final Price: ").append(v.getFinalPrice()).append("\n");
                                bill.append("------------------------");

                                // 4. Show in dialog
                                JOptionPane.showMessageDialog(null, bill.toString());

                                // 5. Save to file
                                java.io.PrintWriter writer = new java.io.PrintWriter("VisitorBill_" + visitorId + ".txt");
                                writer.println(bill.toString());
                                writer.close();

                                JOptionPane.showMessageDialog(null, "Bill saved as VisitorBill_" + visitorId + ".txt");
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        lblArtworkName.setBounds(20, 30, 100, 25);
        txtArtworkName.setBounds(130, 30, 150, 25);
        lblArtworkPrice.setBounds(20, 70, 100, 25);
        txtArtworkPrice.setBounds(130, 70, 150, 25);
        lblCancelReason.setBounds(350, 30, 120, 25);
        scrollCancel.setBounds(480, 30, 350, 90);

        btnBuy.setBounds(130, 150, 100, 30);
        btnCancel.setBounds(240, 150, 100, 30);
        btnBill.setBounds(350, 150, 100, 30);

        purchasePanel.add(lblArtworkName); purchasePanel.add(txtArtworkName);
        purchasePanel.add(lblArtworkPrice); purchasePanel.add(txtArtworkPrice);
        purchasePanel.add(lblCancelReason); purchasePanel.add(scrollCancel);

        purchasePanel.add(btnBuy); purchasePanel.add(btnCancel); purchasePanel.add(btnBill);

        // Panel 3: Manage Visitors
        JPanel managePanel = new JPanel(new GridLayout(2, 4, 10, 10));
        managePanel.setBorder(BorderFactory.createTitledBorder("Manage Visitors"));

        btnSaveFile = new JButton("Save File");
        btnSaveFile.setBackground(new Color(51, 102, 255));
        btnSaveFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (visitors.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No visitor records to save.");
                        return;
                    }

                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("VisitorDetails.txt"));

                        for (ArtGalleryVisitor visitor : visitors) {
                            writer.write("----- Visitor Details -----");
                            writer.newLine();
                            writer.write("Visitor ID: " + visitor.getVisitorId());
                            writer.newLine();
                            writer.write("Full Name: " + visitor.getFullName());
                            writer.newLine();
                            writer.write("Gender: " + visitor.getGender());
                            writer.newLine();
                            writer.write("Contact: " + visitor.getContactNumber());
                            writer.newLine();
                            writer.write("Registration Date: " + visitor.getRegistrationDate());
                            writer.newLine();
                            writer.write("Ticket Type: " + visitor.getTicketType());
                            writer.newLine();
                            writer.write("Ticket Cost: " + visitor.getTicketCost());
                            writer.newLine();
                            writer.write("Visit Count: " + visitor.getVisitCount());
                            writer.newLine();
                            writer.write("Reward Points: " + visitor.getRewardPoints());
                            writer.newLine();
                            writer.write("Bought: " + visitor.getIsBought());
                            writer.newLine();
                            writer.write("Artwork Name: " + visitor.getArtworkName());
                            writer.newLine();
                            writer.write("Artwork Price: " + visitor.getArtworkPrice());
                            writer.newLine();
                            writer.write("Final Price: " + visitor.getFinalPrice());
                            writer.newLine();
                            writer.write("Discount Amount: " + visitor.getDiscountAmount());
                            writer.newLine();
                            writer.write("Cancelled Count: " + visitor.getCancelCount());
                            writer.newLine();
                            writer.write("Cancellation Reason: " + visitor.getCancellationReason());
                            writer.newLine();

                            if (visitor instanceof StandardVisitor) {
                                StandardVisitor std = (StandardVisitor) visitor;
                                writer.write("Eligible for Discount Upgrade: " + std.getIsEligibleForDiscountUpgrade());
                                writer.newLine();
                                writer.write("Visit Limit: " + std.getVisitLimit());
                                writer.newLine();
                                writer.write("Discount Percent: " + std.getDiscountPercent());
                                writer.newLine();
                            } else if (visitor instanceof EliteVisitor) {
                                EliteVisitor elite = (EliteVisitor) visitor;
                                writer.write("Assigned Art Advisor: " + elite.getAssignedPersonalArtAdvisor());
                                writer.newLine();
                                writer.write("Exclusive Event Access: " + elite.getExclusiveEventAccess());
                                writer.newLine();
                            }

                            writer.write("----------------------------");
                            writer.newLine();
                            writer.newLine();
                        }

                        writer.close();
                        JOptionPane.showMessageDialog(null, "Visitor details saved to VisitorDetails.txt");

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error saving to file: " + ex.getMessage());
                    }
                }
            });

        btnReadFile = new JButton("Read File");
        btnReadFile.setBackground(new Color(51, 102, 255));
        btnReadFile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("VisitorDetails.txt"));
                        StringBuilder content = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        reader.close();

                        // Create new JFrame to show the data
                        JFrame frame = new JFrame("Visitor Records");
                        frame.setSize(600, 500);
                        frame.setLocationRelativeTo(null);

                        JTextArea textArea = new JTextArea(content.toString());
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

                        JScrollPane scroll = new JScrollPane(textArea);
                        frame.add(scroll);

                        frame.setVisible(true);

                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "File not found. Please save data first.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
                    }
                }
            });

        btnCalculateDiscount = new JButton("Calc Discount");
        btnCalculateDiscount.setBackground(new Color(51, 102, 255));
        btnCalculateDiscount.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        boolean found = false;
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                double discount = v.calculateDiscount();
                                JOptionPane.showMessageDialog(null, "Calculated Discount: " + discount);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        btnCalculateRewardPoints = new JButton("Calc Rewards");
        btnCalculateRewardPoints.setBackground(new Color(51, 102, 255));
        btnCalculateRewardPoints.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        boolean found = false;
                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                double reward = v.calculateRewardPoint();
                                JOptionPane.showMessageDialog(null, "Reward Points: " + reward);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        btnCheckUpgrade = new JButton("Check Upgrade");
        btnCheckUpgrade.setBackground(new Color(51, 102, 255));
        btnCheckUpgrade.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        boolean found = false;

                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                found = true;

                                if (v instanceof StandardVisitor) {
                                    StandardVisitor sv = (StandardVisitor) v;
                                    boolean upgraded = sv.checkDiscountUpgrade();

                                    if (upgraded) {
                                        JOptionPane.showMessageDialog(null, "Visitor is eligible for discount upgrade (15%).");
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Visitor is not yet eligible for discount upgrade.");
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(null, "This visitor is not a Standard Visitor.");
                                }

                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        btnDisplayDetails = new JButton("Display Details");
        btnDisplayDetails.setBackground(new Color(51, 102, 255));
        btnDisplayDetails.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        boolean found = false;
                        txtDisplayArea.setText(""); // Clear previous content

                        for (ArtGalleryVisitor v : visitors) {
                            if (v.getVisitorId() == visitorId) {
                                found = true;

                                // Capture the printed output of display() method
                                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                                java.io.PrintStream ps = new java.io.PrintStream(out);
                                java.io.PrintStream oldOut = System.out;
                                System.setOut(ps);

                                v.display(); // This prints to our captured stream

                                System.out.flush();
                                System.setOut(oldOut); // Restore original System.out

                                txtDisplayArea.setText(out.toString()); // Show captured output
                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        btnAssignAdvisor = new JButton("Assign Advisor");
        btnAssignAdvisor.setBackground(new Color(51, 102, 255));
        btnAssignAdvisor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String idText = txtVisitorID.getText().trim();
                        if (idText.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter Visitor ID.");
                            return;
                        }

                        int visitorId;
                        try {
                            visitorId = Integer.parseInt(idText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Visitor ID must be a number.");
                            return;
                        }

                        boolean found = false;
                        for (ArtGalleryVisitor visitor : visitors) {
                            if (visitor.getVisitorId() == visitorId) {
                                found = true;

                                if (visitor instanceof EliteVisitor) {
                                    EliteVisitor elite = (EliteVisitor) visitor;
                                    boolean assigned = elite.assignPersonalArtAdvisor();
                                    if (assigned) {
                                        JOptionPane.showMessageDialog(null, "Personal Art Advisor successfully assigned.");
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Not enough reward points to assign advisor.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Only Elite Visitors can be assigned a Personal Art Advisor.");
                                }

                                break;
                            }
                        }

                        if (!found) {
                            JOptionPane.showMessageDialog(null, "Visitor ID not found.");
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
            });

        managePanel.add(btnSaveFile);
        managePanel.add(btnReadFile);
        managePanel.add(btnCalculateDiscount);
        managePanel.add(btnCalculateRewardPoints);
        managePanel.add(btnCheckUpgrade);
        managePanel.add(btnDisplayDetails);
        managePanel.add(btnAssignAdvisor);

        add(visitorPanel);
        add(purchasePanel);
        add(managePanel);
        // Visitor display area
        txtDisplayArea = new JTextArea(8, 60);
        txtDisplayArea.setEditable(false);
        JScrollPane scrollDisplay = new JScrollPane(txtDisplayArea);
        managePanel.add(scrollDisplay);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ArtGalleryGUI();
    }
}
