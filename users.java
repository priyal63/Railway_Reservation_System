package Users;
import Reservation_System.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;

public class users {
    custom_methods dll;
    Scanner sc = new Scanner(System.in);
    Connection con;
    String username;

    public users(Connection con, custom_methods dll, String username) {
        this.con = con;
        this.dll = dll;
        this.username = username;
    }

    public void userMenu() throws Exception {
        do {
            int choice = 0;
            while (true) {
                System.out.println("\n====== USER MENU ======");
                System.out.println("1. Book a Ticket");
                System.out.println("2. Cancel Ticket");
                System.out.println("3. Enquiry");
                System.out.println("4. Return to Main Menu");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                try {
                    choice = sc.nextInt();
                    sc.nextLine(); // clear buffer
                    if (choice < 1 || choice > 5) {
                        System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                    } else {
                        break; // valid choice entered
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid integer (1-5).");
                    sc.nextLine(); // clear invalid input
                }
            }

            switch (choice) {
                case 1 -> bookTicket();
                case 2 -> cancelTicket();
                case 3 -> enquiryMenu();
                case 4 -> {
                    System.out.println("Returning to main menu...");
                    return;
                }
                case 5 -> {
                    System.out.println("Exiting program...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }

            System.out.print("Do you want to continue? (y/n): ");
        } while (sc.nextLine().equalsIgnoreCase("y"));
    }

    // Uses sp_book_ticket procedure
    public void bookTicket() throws Exception {
        System.out.print("Enter Boarding Station: ");
        String board = sc.nextLine().trim();
        System.out.print("Enter Destination Station: ");
        String dest = sc.nextLine().trim();

        boolean checkDate = true;
        Date date = null;
        do {
            System.out.print("Enter Date of journey yyyy-mm-dd: ");
            try {
                date = Date.valueOf(sc.nextLine().trim());
                checkDate = false;
            } catch (Exception e) {
                System.out.println("Invalid date! Try again.");
            }
        } while (checkDate);

        // Find available trains for route and date
        String sql = "SELECT * FROM train WHERE doj=? AND stations LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, date);
        ps.setString(2, "%" + board + "%");
        ResultSet rs = ps.executeQuery();

        LinkedList<Integer> trainNums = new LinkedList<>();
        boolean foundTrain = false;

        System.out.println("\nAvailable Trains:");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-10s %-10s %-10s %-10s %-10s %-10s %-12s %-30s\n",
                "No", "Name", "Departure", "Arrival", "1AC", "2AC", "3AC", "Seats", "Journey Date", "Stations");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            String allStops = rs.getString("stations");
            String[] stops = allStops.split(",");

            int boardIndex = -1, destIndex = -1;
            for (int i = 0; i < stops.length; i++) {
                if (stops[i].trim().equalsIgnoreCase(board)) boardIndex = i;
                if (stops[i].trim().equalsIgnoreCase(dest)) destIndex = i;
            }

            if (boardIndex != -1 && destIndex != -1 && boardIndex < destIndex) {
                foundTrain = true;
                int tnum = rs.getInt("tnum");
                trainNums.add(tnum);
                System.out.printf("%-5d %-25s %-10s %-10s %-10d %-10d %-10d %-10d %-12s %-30s\n",
                        tnum,
                        rs.getString("tname"),
                        rs.getString("dtime"),
                        rs.getString("atime"),
                        rs.getInt("fAC"),
                        rs.getInt("sAC"),
                        rs.getInt("tAC"),
                        rs.getInt("seats"),
                        rs.getDate("doj").toString(),
                        rs.getString("stations"));
            }
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        if (!foundTrain) {
            System.out.println("No trains found for given route & date!");
            return;
        }

        int trainNo = 0;
        while (true) {
            System.out.print("Enter Train Number to Book: ");
            try {
                trainNo = sc.nextInt();
                sc.nextLine();
                if (!trainNums.contains(trainNo)) {
                    System.out.println("Train is not available! Choose again.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid! Train Number must be an integer.");
                sc.nextLine();
            }
        }

        // Get available seats
        String s = "SELECT seats FROM train WHERE tnum=? AND doj=?";
        PreparedStatement pst = con.prepareStatement(s);
        pst.setInt(1, trainNo);
        pst.setDate(2, date);
        ResultSet rs1 = pst.executeQuery();

        int seats = 0;
        if (rs1.next()) {
            seats = rs1.getInt("seats");
        }

        int noOfSeats = 0;
        while (true) {
            System.out.print("Enter number of seats: ");
            try {
                noOfSeats = sc.nextInt();
                sc.nextLine();
                if (noOfSeats <= 0) {
                    System.out.println("Number of seats must be greater than zero.");
                    continue;
                }
                if (seats < noOfSeats) {
                    System.out.println("Not enough seats available!");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
                sc.nextLine();
            }
        }

        String cls = null;
        while (true) {
            System.out.println("\nSelect Class:");
            System.out.println("1. First AC");
            System.out.println("2. Second AC");
            System.out.println("3. Third AC");
            System.out.print("Enter option: ");
            try {
                int clsChoice = sc.nextInt();
                sc.nextLine(); // clear buffer
                if (clsChoice == 1) {
                    cls = "first";
                    break;
                } else if (clsChoice == 2) {
                    cls = "second";
                    break;
                } else if (clsChoice == 3) {
                    cls = "third";
                    break;
                } else {
                    System.out.println("Invalid class option. Please enter 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer (1, 2, or 3).");
                sc.nextLine();
            }
        }

        // Call stored procedure
        CallableStatement cs = con.prepareCall("{CALL sp_book_ticket(?, ?, ?, ?, ?)}");
        cs.setString(1, username);
        cs.setInt(2, trainNo);
        cs.setString(3, cls);
        cs.setInt(4, noOfSeats);
        cs.setDate(5, date);

        cs.execute();

        System.out.println("\nTicket booked successfully! Please check your ticket details.");
        // Optionally, show ticket_info for this user/train/date
        String q = "SELECT * FROM ticket_info WHERE username=? AND train_no=? AND date_of_journey=? ORDER BY pnr_no DESC LIMIT 1";
        PreparedStatement ps1 = con.prepareStatement(q);
        ps1.setString(1, username);
        ps1.setInt(2, trainNo);
        ps1.setDate(3, date);
        ResultSet rs2 = ps1.executeQuery();
        if (rs2.next()) {
            System.out.println("---- Ticket Details ----");
            System.out.println("PNR No: " + rs2.getString("pnr_no"));
            System.out.println("Username: " + rs2.getString("username"));
            System.out.println("Train No: " + rs2.getInt("train_no"));
            System.out.println("Class: " + rs2.getString("class"));
            System.out.println("No of Seats: " + rs2.getInt("no_of_seats"));
            System.out.println("Date of Journey: " + rs2.getDate("date_of_journey"));
            System.out.println("Total Price: ₹" + rs2.getInt("total_price"));
            System.out.println("--------------------------");
        }
    }

    // Uses sp_cancel_ticket procedure
    public void cancelTicket() throws Exception {
        String getTickets = "SELECT * FROM ticket_info WHERE username=?";
        PreparedStatement ps = con.prepareStatement(getTickets);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        LinkedList<String> userPNRs = new LinkedList<>();
        System.out.println("\nYour Booked Tickets:");
        boolean found = false;
        while (rs.next()) {
            found = true;
            String pnr = rs.getString("pnr_no");
            userPNRs.add(pnr);
            System.out.printf("PNR: %s | Train No: %d | Class: %s | Seats: %d | Date: %s%n",
                    pnr,
                    rs.getInt("train_no"),
                    rs.getString("class"),
                    rs.getInt("no_of_seats"),
                    rs.getDate("date_of_journey"));
        }
        if (!found) {
            System.out.println("No tickets found for user.");
            return;
        }

        System.out.print("Enter PNR Number to Cancel: ");
        String pnrToCancel = sc.nextLine();

        if (!userPNRs.contains(pnrToCancel)) {
            System.out.println("Invalid PNR number.");
            return;
        }

        System.out.print("Confirm cancellation? (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Cancellation aborted.");
            return;
        }

        // Call stored procedure
        CallableStatement cs = con.prepareCall("{CALL sp_cancel_ticket(?, ?)}");
        cs.setString(1, pnrToCancel);
        cs.setString(2, username);

        cs.execute();

        System.out.println("Ticket cancelled successfully.");
    }

    // ... Rest of enquiryMenu stays the same ...
    public void enquiryMenu() throws Exception {
        int ch = 0;
        while (true) {
            System.out.println("\n--- Enquiry Menu ---");
            System.out.println("1. By Train Number");
            System.out.println("2. By Train Name");
            System.out.println("3. By Boarding & Destination");
            System.out.print("Enter choice: ");
            try {
                ch = sc.nextInt();
                sc.nextLine(); // clear buffer
                if (ch < 1 || ch > 3) {
                    System.out.println("Invalid choice! Please enter a number between 1 and 3.");
                } else {
                    break; // valid choice entered
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer (1-3).");
                sc.nextLine(); // clear invalid input
            }
        }

        PreparedStatement ps;
        ResultSet rs;
        switch (ch) {
            case 1 -> {

                int tno = 0;
                while (true) {
                    System.out.print("Enter Train Number: ");
                    try {
                        tno = sc.nextInt();
                        sc.nextLine();
                        if (tno <= 0) {
                            System.out.println("Train Number must be a positive integer.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid! Train Number must be an integer.");
                        sc.nextLine();
                    }
                }

                ps = con.prepareStatement("SELECT * FROM train WHERE tnum=?");
                ps.setInt(1, tno);
                rs = ps.executeQuery();

                boolean found=false;

                    // Print header
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf("%-5s %-25s %-12s %-12s %-10s %-10s %-10s %-10s %-10s %-10s %-12s\n",
                            "No", "Name", "Boarding", "Dest.", "Departure", "Arrival", "1AC", "2AC", "3AC", "Seats", "Date");
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
                    while (rs.next()) {
                        found=true;
                        System.out.printf("%-5d %-25s %-12s %-12s %-10s %-10s %-10d %-10d %-10d %-10d %-12s\n",
                                rs.getInt("tnum"),
                                rs.getString("tname"),
                                rs.getString("bp"),
                                rs.getString("dp"),
                                rs.getString("dtime"),
                                rs.getString("atime"),
                                rs.getInt("fAC"),
                                rs.getInt("sAC"),
                                rs.getInt("tAC"),
                                rs.getInt("seats"),
                                rs.getDate("doj").toString());
                    }
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
                if (!found) {
                    System.out.println("Train not found.");
                }
            }
            case 2 -> {

                String tname = "";
                while (true) {
                    System.out.print("Enter Train Name (or part): ");
                    tname = sc.nextLine().trim();
                    if (tname.isEmpty()) {
                        System.out.println("Train name cannot be empty! Please enter a valid train name or part of it.");
                    } else {
                        break;
                    }
                }

                ps = con.prepareStatement("SELECT * FROM train WHERE tname LIKE ?");
                ps.setString(1, "%" + tname + "%");
                rs = ps.executeQuery();

                boolean found = false;

                System.out.println("--------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%-5s %-25s %-12s %-12s %-10s %-10s %-10s %-10s %-10s %-10s %-12s\n",
                        "No", "Name", "Boarding", "Dest.", "Departure", "Arrival", "1AC", "2AC", "3AC", "Seats", "Date");
                System.out.println("--------------------------------------------------------------------------------------------------------------------------");
                while (rs.next()) {
                    found=true;
                    System.out.printf("%-5d %-25s %-12s %-12s %-10s %-10s %-10d %-10d %-10d %-10d %-12s\n",
                            rs.getInt("tnum"),
                            rs.getString("tname"),
                            rs.getString("bp"),
                            rs.getString("dp"),
                            rs.getString("dtime"),
                            rs.getString("atime"),
                            rs.getInt("fAC"),
                            rs.getInt("sAC"),
                            rs.getInt("tAC"),
                            rs.getInt("seats"),
                            rs.getDate("doj").toString());
                }
                System.out.println("--------------------------------------------------------------------------------------------------------------------------");

                if (!found) {
                    System.out.println("Train not found.");
                }
            }
            case 3 -> {

                String bp = "";
                while (true) {
                    System.out.print("Enter Boarding Station: ");
                    bp = sc.nextLine().trim();
                    if (bp.isEmpty()) {
                        System.out.println("Boarding station cannot be empty! Please enter a valid station name.");
                    } else {
                        break;
                    }
                }

                String dp = "";
                while (true) {
                    System.out.print("Enter Destination Station: ");
                    dp = sc.nextLine().trim();
                    if (dp.isEmpty()) {
                        System.out.println("Destination station cannot be empty! Please enter a valid station name.");
                    } else {
                        break;
                    }
                }

                boolean found = false;

                String sql = "SELECT * FROM train ";
                PreparedStatement ps1 = con.prepareStatement(sql);
                ResultSet rs1 = ps1.executeQuery();

                System.out.println("\nAvailable Trains:");
                System.out.println("---------------------------------------------------------------------------------------------------------------");
                System.out.printf("%-5s %-25s %-10s %-10s %-10s %-10s %-10s %-10s %-12s %-30s\n",
                        "No", "Name", "Departure", "Arrival", "1AC", "2AC", "3AC", "Seats", "Journey Date", "Stations");
                System.out.println("---------------------------------------------------------------------------------------------------------------");

                while (rs1.next()) {

                    String allStops = rs1.getString("stations");
                    String[] stops = allStops.split(",");

                    int boardIndex = -1, destIndex = -1;
                    for (int i = 0; i < stops.length; i++) {
                        if (stops[i].trim().equalsIgnoreCase(bp)) boardIndex = i;
                        if (stops[i].trim().equalsIgnoreCase(dp)) destIndex = i;
                    }

                    if (boardIndex != -1 && destIndex != -1 && boardIndex < destIndex) {
                        found = true;

                        System.out.printf("%-5d %-25s %-10s %-10s %-10d %-10d %-10d %-10d %-12s %-30s\n",
                                rs1.getInt("tnum"),
                                rs1.getString("tname"),
                                rs1.getString("dtime"),
                                rs1.getString("atime"),
                                rs1.getInt("fAC"),
                                rs1.getInt("sAC"),
                                rs1.getInt("tAC"),
                                rs1.getInt("seats"),
                                rs1.getDate("doj").toString(),
                                rs1.getString("stations"));
                    }
                }

                System.out.println("---------------------------------------------------------------------------------------------------------------");

                if (!found) {
                    System.out.println("No trains found for this route.");
                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}