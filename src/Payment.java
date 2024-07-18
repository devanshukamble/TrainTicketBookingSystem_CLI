import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Payment {
    Connection con;

    Payment(Connection con) {
        this.con = con;
    }

    Scanner sc = new Scanner(System.in);

    void onlinePayment(int pnr, int price,int tno,String tname,String ctype,String ddate,String dtime,String source,String destination) {
        try {
            System.out.println("1=>from UPI Id");
            System.out.println("2=>from Mobile Number");
            int opchoose = sc.nextInt();
            switch (opchoose) {
                case 1:
                    System.out.print("Enter UPI Id-:");
                    getUpi(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
                    break;
                case 2:
                    System.out.print("Enter Mobile no-:");
                    getPayMobileNo(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }// end of onlinePayment

    void getUpi(int pnr, int price,int tno,String tname,String ctype,String ddate,String dtime,String source,String destination) {
        try {
            String upi = sc.next();
            if (upi.toLowerCase().equals(upi)) {
                if (upi.length() >= 10) {
                    if (upi.contains("@hdfc") || upi.contains("@bob") || upi.contains("@sbi")
                            || upi.contains("@axis")||upi.contains("@paytm")) {
                        String sql = "insert into payment_details values(?,?,?,?,?,?)";
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, pnr);
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
                        String formattedDateTime = currentDateTime.format(formatter);
                        int payment_id = Integer.parseInt(formattedDateTime);
                        pst.setInt(2, payment_id);
                        pst.setString(3, "online");
                        pst.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                        pst.setTime(5, java.sql.Time.valueOf(LocalTime.now()));
                        pst.setInt(6, price);
                        int i = pst.executeUpdate();
                        if (i > -1) {
                            FileWriter fw = new FileWriter("D://sem 2 projects//Java/"+pnr+".txt");
                            fw.write("PNR No-:"+pnr+"\nTrain No-:"+tno+"\nTrain Name-:"+tname+"\nCoach-:"+ctype+"\nDeparture Date-:"+ddate+"\nDeparture Time-:"+dtime+"\nSource-:"+source+"\nDestination-:"+destination+"\nPrice-:"+price+"\nPayment_id-:"+payment_id);
                            fw.flush();
                            fw.close();
                            System.out.println("Your Ticket is Booked");
                        } else {
                            System.out.println("Payment cancled");
                        }
                    } else {
                        System.out.println(":( Invalid upi Formate");
                        System.out.println("Re Enter upi");
                        getUpi(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
                    }
                } else {
                    System.out.println();
                    System.out.println(":( upi must be of length >= 10");
                    System.out.println();
                    System.out.println("Re Enter upi");
                    getUpi(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
                }
            } else {
                System.out.println();
                System.out.println(":( upi must be in lower case");
                System.out.println();
                System.out.println("Re Enter upi");
                getUpi(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }// end of getUpi

    void getPayMobileNo(int pnr, int price,int tno,String tname,String ctype,String ddate,String dtime,String source,String destination) {
        try {
            long moblie_no = sc.nextLong();
            long temp = moblie_no;
            int digit = 0;
            while (temp > 0) {
                temp = temp / 10l;
                digit++;
            }
            if (digit == 10) {
                String sql = "insert into payment_details values(?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, pnr);
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
                String formattedDateTime = currentDateTime.format(formatter);
                int payment_id = Integer.parseInt(formattedDateTime);
                pst.setInt(2, payment_id);
                pst.setString(3, "online");
                pst.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                pst.setTime(5, java.sql.Time.valueOf(LocalTime.now()));
                pst.setInt(6, price);
                FileWriter fw = new FileWriter("D://sem 2 projects//Java/"+pnr+".txt");
                fw.write("PNR No-:"+pnr+"\nTrain No-:"+tno+"\nTrain Name-:"+tname+"\nCoach-:"+ctype+"\nDeparture Date-:"+ddate+"\nDeparture Time-:"+dtime+"\nSource-:"+source+"\nDestination-:"+destination+"\nPrice-:"+price+"\nPayment_id-:"+payment_id);
                fw.flush();
                fw.close();
                System.out.println("Your Ticket Is Book");
            } else {
                System.out.println();
                System.out.println(":( Mobile number should be of 10 digits");
                System.out.println();
                System.out.println("Re Enter Mobile number");
                getPayMobileNo(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }// end of getPayMobileNo

    void getDebitCard(int pnr, int price,int tno,String tname,String ctype,String ddate,String dtime,String source,String destination) {
        try {
            System.out.print("Enter Debit Card No-:");
            long debitNo = sc.nextLong();
            String expDate;
            long temp = debitNo;
            int digit = 0, cvv;
            while (temp > 0) {
                temp = temp / 10l;
                digit++;
            }
            if (digit == 16) {
                System.out.print("Enter Expiery Date-:");
                expDate = sc.next();
                System.out.print("Enter cvv no-:");
                cvv = sc.nextInt();
                digit = 0;
                while (temp > 0) {
                    temp = temp / 10l;
                    digit++;
                } 
                if (digit == 3) {
                    String sql = "insert into payment_details values(?,?,?,?,?,?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setInt(1, pnr);
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
                    String formattedDateTime = currentDateTime.format(formatter);
                    int payment_id = Integer.parseInt(formattedDateTime);
                    pst.setInt(2, payment_id);
                    pst.setString(3, "card");
                    pst.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                    pst.setTime(5, java.sql.Time.valueOf(LocalTime.now()));
                    pst.setInt(6, price);
                    FileWriter fw = new FileWriter("D://sem 2 projects//Java/"+pnr+".txt");
                    fw.write("PNR No-:"+pnr+"\nTrain No-:"+tno+"\nTrain Name-:"+tname+"\nCoach-:"+ctype+"\nDeparture Date-:"+ddate+"\nDeparture Time-:"+dtime+"\nSource-:"+source+"\nDestination-:"+destination+"\nPrice-:"+price+"\nPayment_id-:"+payment_id);
                    fw.flush();
                    fw.close();
                    System.out.println("Your Ticket Is Book");
                } else {
                    System.out.println();
                    System.out.println(":( cvv  number should be of 3 digits");
                    System.out.println();
                    System.out.println("Re Enter Debit Card number details");
                    getDebitCard(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
                }
            } else {
                System.out.println();
                System.out.println(":( Debit Card number should be of 16 digits");
                System.out.println();
                System.out.println("Re Enter Debit Card number details");
                getDebitCard(pnr, price,tno,tname,ctype,ddate,dtime,source,destination);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }// end of getDebitCard
}