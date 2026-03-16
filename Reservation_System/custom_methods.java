package Reservation_System;

public class custom_methods {

    public static class UserData {
        int id;
        String name;
        public String password;
        public String emailid;

        public UserData(int id, String name, String password, String emailid) {
            this.id = id;
            this.name = name;
            this.password = password;
            this.emailid = emailid;
        }
    }

    public class Node {
        public custom_methods.UserData data;
        custom_methods.Node prev;
        public custom_methods.Node next;

        Node(custom_methods.UserData data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    public custom_methods.Node first = null;
    custom_methods.Node last = null;
    int size = 0;

    // Add at the end
    public void addLast(custom_methods.UserData data) {
        custom_methods.Node newNode = new custom_methods.Node(data);
        if (last == null) {
            first = last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        size++;
    }

    // Display all users
    public void display() {
        custom_methods.Node temp = first;
        //System.out.println("Users in Linked List:");
        while (temp != null) {
            custom_methods.UserData d = temp.data;
            System.out.printf("ID: %d, Name: %s, Email: %s\n", d.id, d.name, d.emailid);
            temp = temp.next;
        }
    }

    // Search user by name and password (for login validation)
    public boolean contains(String name, String password) {
        custom_methods.Node temp = first;
        while (temp != null) {
            if (temp.data.name.equals(name) && temp.data.password.equals(password)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public boolean emailExists(String email) {
        custom_methods.Node temp = first;
        while (temp != null) {
            if (temp.data.emailid.equalsIgnoreCase(email)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public static class TrainData {
        int tnum;
        String tname;
        String bp;
        String dp;
        String dtime;
        String atime;
        int fAC, sAC, tAC, seats;
        java.sql.Date doj;

        public TrainData(int tnum, String tname, String bp, String dp, String dtime, String atime, int fAC, int sAC, int tAC, int seats, java.sql.Date doj) {
            this.tnum = tnum;
            this.tname = tname;
            this.bp = bp;
            this.dp = dp;
            this.dtime = dtime;
            this.atime = atime;
            this.fAC = fAC;
            this.sAC = sAC;
            this.tAC = tAC;
            this.seats = seats;
            this.doj = doj;
        }
    }

    public class QueueNode {
        TrainData train;
        QueueNode next;
        QueueNode(TrainData train) { this.train = train; this.next = null; }
    }

    QueueNode queueFront = null;
    QueueNode queueRear = null;

    // Enqueue train data
    public void enqueuy(TrainData train) {
        QueueNode newNode = new QueueNode(train);
        if (queueRear == null) {
            queueFront = queueRear = newNode;
        } else {
            queueRear.next = newNode;
            queueRear = newNode;
        }
    }

    // Display queue contents
    public void displayQueue() {
        QueueNode temp = queueFront;
        if (temp == null) {
            System.out.println("Queue is empty.");
            return;
        }
        System.out.println("Queued Trains:");
        while (temp != null) {
            TrainData t = temp.train;
            System.out.printf("Train No: %d, Name: %s, BP: %s, DP: %s, Seats: %d, Date: %s\n",
                    t.tnum, t.tname, t.bp, t.dp, t.seats, t.doj);
            temp = temp.next;
        }
    }
}