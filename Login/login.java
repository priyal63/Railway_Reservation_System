package Login;
import Reservation_System.*;
import java.sql.*;

public class login {
    String passengerName;
    String emailId;
    String password;

    public login(String name, String email, String pass) {
        this.passengerName = name;
        this.emailId = email;
        this.password = pass;
    }

    public int newAccount(Connection con, String passengerName, String password, String emailId, custom_methods dll) throws SQLException {
        // Check if email already exists in DLL or DB
        if (dll.emailExists(emailId)) {
            System.out.println(" Account already exists for this email.");
            return -1;
        }

        String checkQuery = "SELECT PassengerId FROM passengers WHERE EmailId = ?";
        PreparedStatement checkPs = con.prepareStatement(checkQuery);
        checkPs.setString(1, emailId);
        ResultSet rsCheck = checkPs.executeQuery();
        if (rsCheck.next()) {
            System.out.println(" Account already exists for this email.");
            return -1;
        }

        // Insert if not exists
        String q = "INSERT INTO passengers (PassengerName, EmailId, Password) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, passengerName);
        ps.setString(2, emailId);
        ps.setString(3, password);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            System.out.println(" Account created! Your Passenger ID is: " + id);
            dll.addLast(new custom_methods.UserData(id, passengerName, password, emailId));
            return id;
        }
        return -1;
    }

    public static boolean loginByName(Connection con, String name, String pass, custom_methods dll) throws Exception {
        // Check from DLL first for quick validation (optional)
        if (dll.contains(name, pass)) return true;

        // Else check DB
        String q = "select * from passengers where passengerName=? and password=?";
        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, name);
        ps.setString(2, pass);

        ResultSet rs1 = ps.executeQuery();

        return rs1.next();
    }

    public static boolean loginByEmail(Connection con, String emailId, String pass, custom_methods dll) throws Exception {
        // First check from DLL for quick validation (optional)
        custom_methods.Node temp = dll.first;
        while (temp != null) {
            if (temp.data.emailid.equalsIgnoreCase(emailId) && temp.data.password.equals(pass)) {
                return true;
            }
            temp = temp.next;
        }

        // Else check DB
        String q = "SELECT * FROM passengers WHERE EmailId=? AND Password=?";
        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, emailId);
        ps.setString(2, pass);

        ResultSet rs1 = ps.executeQuery();
        return rs1.next();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasLetter && hasDigit && hasSpecial;
    }

    public static String forget_pass(Connection con,String email)throws Exception{
        String quary="select password from passengers where emailid=?";
        PreparedStatement pst=con.prepareStatement(quary);
        pst.setString(1,email);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            String pass=rs.getString(1);
            return pass;
        }
        else {
            return null;
        }
    }
}