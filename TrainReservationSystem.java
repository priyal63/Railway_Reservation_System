package Reservation_System;

import Login.*;
import Admin.*;
import Users.*;
import java.sql.*;
import java.util.Hashtable;
import java.util.Scanner;

public class TrainReservationSystem {
    public static void main(String[] args) throws Exception {
        String dburl = "jdbc:mysql://localhost:3306/railway_reservation";
        String dbuser = "root";
        String dbpass = "";

        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        Scanner sc = new Scanner(System.in);

        custom_methods dll = new custom_methods();

        // Load passengers from DB into DLL
        String query1 = "SELECT passengerid, passengername, password, emailid FROM passengers";
        Statement st1 = con.createStatement();
        ResultSet rs1 = st1.executeQuery(query1);
        while (rs1.next()) {
            int id = rs1.getInt("passengerid");
            String name = rs1.getString("passengername");
            String password = rs1.getString("password");
            String emailid = rs1.getString("emailid");
            dll.addLast(new custom_methods.UserData(id, name, password, emailid));
        }

        int choice;
        do {
            System.out.println("\n-------------------------Welcome to the railway reservation system-------------------------");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1 -> {
                        Hashtable<String, AdminData> AdminInfo = new Hashtable<>();
                        String query = "SELECT uname, pass, age, g, timestamp, sno FROM admin";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(query);

                        while (rs.next()) {
                            String name = rs.getString("uname");
                            String pass = rs.getString("pass");
                            int age = rs.getInt("age");
                            String gender = rs.getString("g");
                            String timestamp = rs.getString("timestamp");
                            int sno = rs.getInt("sno");

                            AdminInfo.put(name, new AdminData(name, pass, age, gender, timestamp, sno));
                        }

                        int choice1;
                        do {
                            System.out.println("\n===  Admin Login ===");
                            System.out.println("1. Log in");
                            System.out.println("2. Exit to main menu");
                            System.out.print("Enter choice: ");
                            try {
                                choice1 = Integer.parseInt(sc.nextLine().trim());

                                switch (choice1) {
                                    case 1 -> {
                                        System.out.print("Enter your admin username: ");
                                        String adminName = sc.nextLine();
                                        System.out.print("Enter your password: ");
                                        String adminPassword = sc.nextLine();

                                        String sql = "SELECT * FROM admin WHERE uname = ? AND pass = ?";
                                        PreparedStatement ps = con.prepareStatement(sql);
                                        ps.setString(1, adminName);
                                        ps.setString(2, adminPassword);

                                        ResultSet rs1Admin = ps.executeQuery();

                                        if (rs1Admin.next()) {
                                            System.out.println(" Admin login successful!");
                                            admin adminObj = new admin(con, dll);
                                            adminObj.adminMenu();  // Call your admin menu here
                                            choice1 = 2;  // Exit admin login loop after menu
                                        } else {
                                            System.out.println(" Invalid username or password.");
                                        }
                                    }
                                    case 2 -> System.out.println("Exiting to main menu...");
                                    default -> System.out.println("Enter a valid number!");
                                }
                            } catch (NumberFormatException e) {
                                choice1 = 0;
                                System.out.println("please enter a digit between 1 t0 3 ");
                            }
                        } while (choice1 != 2);
                    }

                    case 2 -> {
                        int choice1;
                        do {
                            System.out.println("\n===  Train Reservation System ===");
                            System.out.println("1. Create a new account");
                            System.out.println("2. Log in");
                            System.out.println("3. forgot password");
                            System.out.println("4. Exit to main menu");
                            System.out.print("Enter choice: ");
                            try {
                                choice1 = Integer.parseInt(sc.nextLine().trim());

                                switch (choice1) {
                                    case 1 -> {
                                        String name;
                                        while (true) {
                                            System.out.print("Enter Train Name (string): ");
                                            name = sc.nextLine().trim();
                                            if (name.isEmpty()) {
                                                System.out.println("Invalid! Name cannot be empty.");
                                            } else {
                                                break;
                                            }
                                        }
                                        boolean pass_check;
                                        String pass;
                                        do {
                                            System.out.print(" Enter password (min 8 chars, must include letters, digits & special chars): ");
                                            pass = sc.nextLine();
                                            pass_check = login.isValidPassword(pass);
                                            if (!pass_check) {
                                                System.out.println(" Password must contain letters, digits, and special characters.");
                                            }
                                        } while (!pass_check);


                                        System.out.println("Enter your email: ");
                                        String email = sc.nextLine();

                                        while (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                                            System.out.println("Invalid email format! Please enter a valid email: ");
                                            email = sc.nextLine();
                                        }
                                        login l1 = new login(name, email, pass);
                                        l1.newAccount(con, name, pass, email, dll);
                                    }
                                    case 2 -> {
                                        System.out.print(" Enter Email Id: ");
                                        String emailId = sc.nextLine();

                                        System.out.println("Enter a user name");
                                        String Username = sc.nextLine();
                                        sc.nextLine();

                                        System.out.print(" Enter Password: ");
                                        String loginPass = sc.nextLine();

                                        if (login.loginByEmail(con, emailId, loginPass, dll) && login.loginByName(con, Username, loginPass, dll)) {
                                            System.out.println(" Login successful.");
                                            users userObj = new users(con, dll, Username);
                                            userObj.userMenu();
                                        } else {
                                            System.out.println(" Invalid Email or Password.");
                                        }
                                    }
                                    case 3 -> {
                                        do {
                                            System.out.println("Enter email id");
                                            String email = sc.nextLine();
                                            if (email.isEmpty()) {
                                                System.out.println("Email can not be empty!");
                                                continue;
                                            }
                                            if (login.forget_pass(con, email) != null) {
                                                do {
                                                    int otp = (int) (Math.random() * 1000000);
                                                    Thread.sleep(2000);
                                                    System.out.print("this is your otp: ");
                                                    System.out.println(otp);
                                                    System.out.println("enter otp");
                                                    int iotp = sc.nextInt();
                                                    sc.nextLine();
                                                    if (iotp == otp) {
                                                        System.out.println("login successfully! ");
                                                        String quary = "select passengername,password from passengers where emailid=?";
                                                        PreparedStatement pst = con.prepareStatement(quary);
                                                        pst.setString(1, email);
                                                        ResultSet rs = pst.executeQuery();
                                                        String name = null, pass = null;
                                                        while (rs.next()) {
                                                            name = rs.getString(1);
                                                            pass = rs.getString(2);
                                                        }
                                                        users userOb = new users(con, dll, name);
                                                        userOb.userMenu();
                                                        break;
                                                    } else {
                                                        System.out.println("invalid otp..");
                                                        break;
                                                    }
                                                } while (true);
                                            } else {
                                                System.out.println("invalid email");
                                                break;
                                            }
                                        } while (true);
                                    }
                                    case 4 -> System.out.println("Returning to main menu...");
                                    default -> System.out.println(" Invalid choice. Try again.");
                                }
                            } catch (NumberFormatException e) {
                                choice1 = 0;
                                System.out.println("please enter a digit between 1 t0 3 ");
                            }
                        } while (choice1 != 4);
                    }

                    case 3 -> System.out.println("Exiting system. Goodbye!");

                    default -> System.out.println("Invalid option! Try again.");
                }
            } catch (NumberFormatException e) {
                choice = 0;
                System.out.println("please enter a digit between 1 t0 3 ");
            }
        } while (choice != 3);

        con.close();
        sc.close();
    }
}