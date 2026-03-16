package Admin;

import Login.*;
import Admin.*;
import Users.*;

public class AdminData {
    String uname;
    String pass;
    int age;
    String gender;
    String timeStamp;
    int sno;

    public AdminData(String uname, String pass, int age, String gender, String timeStamp, int sno) {
        this.uname = uname;
        this.pass = pass;
        this.age = age;
        this.gender = gender;
        this.timeStamp = timeStamp;
        this.sno = sno;
    }
}