package Admin;
import Reservation_System.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class admin {
    Connection con;
    Scanner sc;
    custom_methods userList;

    public admin(Connection con, custom_methods userList) {
        this.con = con;
        this.sc = new Scanner(System.in);
        this.userList = userList;
    }

    public void adminMenu() throws Exception {
        int choice = 0;
        do{
            System.out.println("\n====== ADMIN MENU ======");
            System.out.println("1. Add New Train");
            System.out.println("2. View All Trains");
            System.out.println("3. Search Train");
            System.out.println("4. View All Tickets Booked");
            System.out.println("5. View User Login History");
            System.out.println("6. Return to Main Menu");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1 -> addNewTrain();
                    case 2 -> viewAllTrains();
                    case 3 -> searchTrain();
                    case 4 -> viewAllTickets();
                    case 5 -> viewUserLoginHistory();
                    case 6 -> {
                        System.out.println("Returning to main menu...");
                        return;
                    }
                    case 7 -> {
                        System.out.println("Exiting program...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
            catch (NumberFormatException e){
                System.out.println("please enter a digit between 1 t0 7 ");
            }
        }while (choice!=7);
    }

    public void addNewTrain() throws Exception {
        int tnum;
        while (true) {
            System.out.print("Enter Train Number (integer): ");
            String input = sc.nextLine().trim();
            try {
                tnum = Integer.parseInt(input);
                // Check for duplicate train number
                String checkTrain = "SELECT tnum FROM train WHERE tnum = ?";
                PreparedStatement checkPs = con.prepareStatement(checkTrain);
                checkPs.setInt(1, tnum);
                ResultSet checkRs = checkPs.executeQuery();
                if (checkRs.next()) {
                    System.out.println("Train number already exists! Please use a unique train number.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Train Number must be an integer.");
            }
        }

        String tname;
        while (true) {
            System.out.print("Enter Train Name (string): ");
            tname = sc.nextLine().trim();
            if (tname.isEmpty()) {
                System.out.println("Invalid! Train Name cannot be empty.");
            } else {
                break;
            }
        }

        String bp;
        while (true) {
            System.out.print("Enter Boarding Point (string): ");
            bp = sc.nextLine().trim();
            if (bp.isEmpty()) {
                System.out.println("Invalid! Boarding Point cannot be empty.");
            } else {
                break;
            }
        }

        String dp;
        while (true) {
            System.out.print("Enter Destination Point (string): ");
            dp = sc.nextLine().trim();
            if (dp.isEmpty()) {
                System.out.println("Invalid! Destination Point cannot be empty.");
            } else {
                break;
            }
        }

        String dtime;
        while (true) {
            System.out.print("Enter Departure Time (HH:mm): ");
            dtime = sc.nextLine().trim();
            if (dtime.isEmpty()) {
                System.out.println("Invalid! Departure Time cannot be empty.");
            } else {
                break;
            }
        }

        String atime;
        while (true) {
            System.out.print("Enter Arrival Time (HH:mm): ");
            atime = sc.nextLine().trim();
            if (atime.isEmpty()) {
                System.out.println("Invalid! Arrival Time cannot be empty.");
            } else {
                break;
            }
        }

        int fAC;
        while (true) {
            System.out.print("Enter First AC Fare (integer): ");
            String input = sc.nextLine().trim();
            try {
                fAC = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Fare must be an integer.");
            }
        }

        int sAC;
        while (true) {
            System.out.print("Enter Second AC Fare (integer): ");
            String input = sc.nextLine().trim();
            try {
                sAC = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Fare must be an integer.");
            }
        }

        int tAC;
        while (true) {
            System.out.print("Enter Third AC Fare (integer): ");
            String input = sc.nextLine().trim();
            try {
                tAC = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Fare must be an integer.");
            }
        }

        int seats;
        while (true) {
            System.out.print("Enter Number of Seats (integer): ");
            String input = sc.nextLine().trim();
            try {
                seats = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Seats must be an integer.");
            }
        }

        java.sql.Date doj;
        while (true) {
            System.out.print("Enter Date of Journey (YYYY-MM-DD): ");
            String dojStr = sc.nextLine().trim();
            try {
                doj = java.sql.Date.valueOf(dojStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid! Date must be in YYYY-MM-DD format.");
            }
        }

        String stations;
        while (true) {
            System.out.print("Enter Stations (comma separated, e.g. Station1, Station2, Station3): ");
            stations = sc.nextLine().trim();
            if (stations.isEmpty()) {
                System.out.println("Invalid! Stations cannot be empty.");
                continue;
            }
            // Validate that at least two stations are entered
            String[] stationArr = stations.split(",");
            if (stationArr.length < 2) {
                System.out.println("Invalid! Please enter at least two stations, separated by commas.");
                continue;
            }
            boolean validStations = true;
            for (String s : stationArr) {
                if (s.trim().isEmpty()) {
                    validStations = false;
                    break;
                }
            }
            if (!validStations) {
                System.out.println("Invalid! Station names cannot be empty.");
                continue;
            }
            break;
        }

        String insertTrain = "INSERT INTO train (tnum, tname, bp, dp, dtime, atime, fAC, sAC, tAC, seats, doj, stations) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(insertTrain);
        ps.setInt(1, tnum);
        ps.setString(2, tname);
        ps.setString(3, bp);
        ps.setString(4, dp);
        ps.setString(5, dtime);
        ps.setString(6, atime);
        ps.setInt(7, fAC);
        ps.setInt(8, sAC);
        ps.setInt(9, tAC);
        ps.setInt(10, seats);
        ps.setDate(11, doj);
        ps.setString(12, stations);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("New train added successfully.");

            // Enqueue train info to queue
            custom_methods.TrainData td = new custom_methods.TrainData(tnum, tname, bp, dp, dtime, atime, fAC, sAC, tAC, seats, doj);
            userList.enqueuy(td);
        } else {
            System.out.println("Failed to add train.");
        }
    }

    public void viewAllTrains() throws Exception {
        String sql = "SELECT * FROM train ORDER BY tnum";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.printf("%-5s %-25s %-12s %-12s %-10s %-10s %-10s %-10s %-10s %-10s %-12s\n",
                "No", "Name", "Boarding", "Dest.", "Departure", "Arrival", "1AC", "2AC", "3AC", "Seats", "Date");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

        boolean found=false;
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
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

        if (!found) {
            System.out.println("No trains found.");
        }

        System.out.println("\nRecently Added Trains:");
        userList.displayQueue();
    }

    public void searchTrain() throws Exception {
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

    public void printTrains(ResultSet rs) throws Exception {
        boolean found = false;
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
            System.out.println("No trains found.");
        }
    }

    public void viewAllTickets() throws Exception {
        String sql = "SELECT * FROM ticket_info ORDER BY date_of_journey";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s %-15s %-10s %-10s %-10s %-12s %-10s\n",
                "PNR No", "Username", "Train No", "Class", "Seats", "Journey Date", "Total Price");
        System.out.println("------------------------------------------------------------------------------------------------");

        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.printf("%-12s %-15s %-10d %-10s %-10d %-12s %-10d\n",
                    rs.getString("pnr_no"),
                    rs.getString("username"),
                    rs.getInt("train_no"),
                    rs.getString("class"),
                    rs.getInt("no_of_seats"),
                    rs.getDate("date_of_journey").toString(),
                    rs.getInt("total_price"));
        }
        System.out.println("------------------------------------------------------------------------------------------------");

        if (!found) {
            System.out.println("No tickets booked yet.");
        }
    }

    public void viewUserLoginHistory() {
        System.out.println("\nUsers Login History (from Linked List):");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-20s\n", "ID", "Name", "Email");
        System.out.println("-----------------------------------------------------------------");

        userList.display();

        System.out.println("-----------------------------------------------------------------");
    }
}