import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; 

public class Customer {
    // String username;
    String password;
    String con_pass;// confirm password
    int year, mon, date, chosse;
    ArrayList<String> security_Que = new ArrayList<String>(
            Arrays.asList("First School name", "Childhood hero", "favorite sport", "Birth place"));
    String gender, e_mail;
    long moblie_no;
    Connection con;
    LocalDate d;
    String security_ans;

    public Customer(Connection con) {
        this.con = con;
    }

    Scanner sc = new Scanner(System.in);

    public void registration() {
        try {

            // adding username to login and registration table
            String sql1 = "insert into login values(?,?);";
            String sql2 = "insert into registration values(?,?,?,?,?,?,?);";
            PreparedStatement pst1 = con.prepareStatement(sql1);
            PreparedStatement pst2 = con.prepareStatement(sql2);
            System.out.println("=====Basic Details=====");

            getEmail();
            System.out.print("Enter Password:");
            password = sc.next();
            // sc.nextLine();
            System.out.print("Enter confirm Password:");
            con_pass = sc.next();
            if (password.equals(con_pass)) {

                // adding password to login and registration table

                System.out.println("chosse Security Question");
                System.out.println("1 First School name");
                System.out.println("2 Childhood hero");
                System.out.println("3 favorite sport");
                System.out.println("4 Birth place");
                chosse = sc.nextInt();
                sc.nextLine();
                security_ans = sc.nextLine();

                // adding security que and ans to registration table

                System.out.println("=====Personal Details=====");
                System.out.print("Enter Birthyear:");
                year = sc.nextInt();
                System.out.print("Enter month:");
                mon = sc.nextInt();
                System.out.print("Enter day:");
                date = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Gender:");
                gender = sc.nextLine();
                System.out.print("Enter moblie no:");
                getMobileNo();
                sc.nextLine();
                String dob = year + "-" + mon + "-" + date;

                d = LocalDate.parse(dob);

                // add dob,gender and mobile no to registration table
                pst1.setString(1, e_mail);
                pst2.setString(1, e_mail);
                pst1.setString(2, con_pass);
                pst2.setString(2, con_pass);
                pst2.setString(3, security_Que.get(chosse - 1));
                pst2.setString(4, security_ans);
                pst2.setDate(5, java.sql.Date.valueOf(d));
                pst2.setString(6, gender);
                pst2.setLong(7, moblie_no);
                int i1 = pst1.executeUpdate();
                int i2 = pst2.executeUpdate();
                if (i1 > -1 && i2 > -1) {
                    System.out.println("customer created");
                }
            } else {
                System.out.println("Plz check your password and confirm password again");
                registration();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }// end of registration

    public void getEmail() {
        System.out.print("Enter User Name(e_mail):");
        String e_mail = sc.next();
        if (e_mail.toLowerCase().equals(e_mail)) {
            if (e_mail.length() >= 10) {
                if (e_mail.contains("@gmail.com") || e_mail.contains("@outlook.com") || e_mail.contains("@yahoo.com")) {
                    char e[] = e_mail.toCharArray();
                    int count = 0;
                    for (int i = 0; i < e.length; i++) {
                        if (e[i] == '@') {
                            count++;
                        }
                    }
                    if (count < 2) {
                        this.e_mail = e_mail;
                    } else {
                        System.out.println(":( Email cannot have more than one @");
                        getEmail();
                    }
                } else {
                    System.out.println(":( Invalid Email Formate");
                    System.out.println("Re Enter email");
                    getEmail();
                }
            } else {
                System.out.println();
                System.out.println(":( Email must be of length >= 28");
                System.out.println();
                System.out.println("Re Enter email");
                getEmail();
            }
        } else {
            System.out.println();
            System.out.println(":( Email must be in lower case");
            System.out.println();
            System.out.println("Re Enter email");
            getEmail();
        }
    }// end of email validation method

    public void getMobileNo() {
        try {

            long moblie_no = sc.nextLong();
            long temp = moblie_no;
            int digit = 0;
            long checkFirst = 0;
            while (temp > 0) {
                temp = temp / 10l;
                digit++;
            }
            if (digit == 10) {
                temp = moblie_no;
                while (temp > 0) {
                    checkFirst = temp % 10;
                    temp /= 10;
                }
                if (checkFirst > 7) {
                    this.moblie_no = moblie_no;
                } else {
                    System.out.println("Mobile no should to starting with 8 or 9");
                    getMobileNo();
                }
            } else {
                System.out.println();
                System.out.println(":( Mobile number should be of 10 digits");
                System.out.println();
                System.out.println("Re Enter Mobile number");
                getMobileNo();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }// end of getMobileNo

    public boolean customerLogin(String username, String password) {
        try {
            String sql = "select * from registration;";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            Boolean checkUser = false;
            while (rs.next()) {
                if (rs.getString(1).equals(username)) {
                    String tempUserName, tempUserPassword;
                    sql = "select * from registration where username = " + username + ";";
                    tempUserName = rs.getString(1);
                    tempUserPassword = rs.getString(2);
                    if (username.equals(tempUserName) && password.equals(tempUserPassword)) {
                        return true;
                    } else {
                        System.out.println("1->to re login");
                        System.out.println("2->forgot password");
                        int count = sc.nextInt();
                        switch (count) {
                            case 1:
                                customerLogin(sc.next(), sc.next());
                                return true;
                            case 2:
                                System.out.println("Enter security ans");
                                if (forgetPassword(username, sc.next())) {
                                    return true;
                                }
                                break;
                        }
                        checkUser = true;
                    }
                }
                if (!checkUser) {
                    System.out.println("user doesnot exist");
                    return false;
                }

            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;

        // return false;
    }// end of customerLogin

    boolean forgetPassword(String userName, String ans) {
        try {
            String sql2 = "select * from registration;";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(sql2);
            while (rs2.next()) {
                if (rs2.getString(1).equals(userName)) {
                    String tableAns = rs2.getString(4);
                    if (tableAns.equals(ans)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public void showCustomerMenue() {
        System.out.println("1->Book Ticket");
        System.out.println("2->Cancle Ticket");
        System.out.println("3->Search Trains");
        System.out.println("0->Exit");
    }
}