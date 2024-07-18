import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;

public class Train {
    private Connection con;
    private Scanner sc = new Scanner(System.in);
    private int noOfCoach = 0, noOfStation = 0;
    private final int EC = 25;
    private final int CC = 15;

    public Train(Connection con) {
        this.con = con;
    }

    public void addTrain() {
        try {
            String sql1 = "insert into train values(?,?,?,?,?,?);";
            PreparedStatement pst1 = con.prepareStatement(sql1);
            System.out.println("\n===========Enter Train Details===========");
            System.out.println("Enter train no");
            pst1.setInt(1, sc.nextInt());
            System.out.println("Enter train name");
            sc.nextLine();
            pst1.setString(2, sc.nextLine());
            System.out.println("Enter train onboarding station name");
            pst1.setString(3, sc.nextLine());
            System.out.println("Enter train last station name");
            pst1.setString(4, sc.nextLine());
            System.out.println("Enter days of weak on which it runs\neg(mon wed fri):");
            pst1.setString(5, sc.nextLine());
            System.out.println("Enter types of coaches\neg(1A 2A SL):");
            pst1.setString(6, sc.nextLine());
            int i1 = pst1.executeUpdate();
            if (i1 > -1) {
                System.out.println("data added in train table");
            }
            System.out.println("\n===========Enter Coach Details===========");
            System.out.println("Enter no of types of coaches for particular train:");
            noOfCoach = sc.nextInt();
            String sql3 = "insert into train_coach_details values(?,?,?);";
            PreparedStatement pst3 = con.prepareStatement(sql3);
            for (int i = 0; i < noOfCoach; i++) {
                System.out.println("For Coach Type " + (i + 1));
                System.out.println("Enter Train No:");
                pst3.setInt(1, sc.nextInt());
                System.out.println("Enter Coach Type:");
                pst3.setString(2, sc.next());
                System.out.println("Enter total no of seats in this type of coach:");
                pst3.setInt(3, sc.nextInt());
                pst3.executeUpdate();
            }

            String sql4 = "insert into stop_details values(?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst4 = con.prepareStatement(sql4);
            System.out.println("Enter no of stations for particular train:");
            noOfStation = sc.nextInt();
            LocalTime t;

            for (int i = 0; i < noOfStation; i++) {
                System.out.println("Enter Train No");
                pst4.setInt(1, sc.nextInt());
                System.out.println("Enter Station No");
                pst4.setInt(2, sc.nextInt());
                System.out.println("Enter Station Code");
                pst4.setString(3, sc.next());
                System.out.println("Enter Station Name");
                pst4.setString(4, sc.next());
                sc.nextLine();
                System.out.println("Enter Arival Time(hh:mM)");
                t = LocalTime.of(sc.nextInt(), sc.nextInt());
                pst4.setTime(5, java.sql.Time.valueOf(t));
                System.out.println("Enter Departure Time(hh and mm)");
                t = LocalTime.of(sc.nextInt(), sc.nextInt());
                pst4.setTime(6, java.sql.Time.valueOf(t));
                System.out.println("Enter Station Hault Time(mm)");
                t = LocalTime.of(0, sc.nextInt());
                pst4.setTime(7, java.sql.Time.valueOf(t));
                System.out.println("Enter Distance");
                pst4.setInt(8, sc.nextInt());
                System.out.println("Enter Day");
                pst4.setInt(9, sc.nextInt());
                pst4.execute();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }// end of add train

    public void bookTicket() {
        try {

            String from = null, to = null, pname = null, dod = null, coachtype = null, status = null;
            String year = null, mon = null, date = null;
            int page = 0;
            LocalDate d = null;

            System.out.println("================Enter Travling Details================");
            System.out.println("Enter Departure Station name");
            from = sc.nextLine();
            sc.nextLine();
            System.out.println("Enter Destination Station name");
            to = sc.nextLine();
            boolean f = false, t = false;
            int trno, sn1 = 0, sn2 = 0;
            String satname;

            System.out.println("\nEnter Departure Date");
            System.out.print("Enter Departure year:");
            year = sc.next();
            System.out.print("Enter Departure month:");
            mon = sc.next();
            System.out.print("Enter Departure Day:");
            date = sc.next();
            dod = year + "-" + mon + "-" + date;
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            d = LocalDate.parse(dod, formatter1);

            String sql1 = "select * from train;";
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(sql1);
            String sql2 = "select * from stop_details where train_no = ?;";
            PreparedStatement pst2 = con.prepareStatement(sql2);
            ResultSet rs2 = null;
            while (rs1.next()) {
                trno = rs1.getInt(1);
                pst2.setInt(1, trno);
                rs2 = pst2.executeQuery();
                f = false;
                t = false;
                while (rs2.next()) {
                    satname = rs2.getString(4);
                    if (from.equalsIgnoreCase(satname)) {
                        sn1 = rs2.getInt(2);
                        f = true;
                    }
                    if (to.equalsIgnoreCase(satname)) {
                        sn2 = rs2.getInt(2);
                        t = true;
                    }
                }
                if ((f && t) && (sn2 > sn1)) {
                    System.out.println("#######################################");
                    System.out.println("Train no-:" + trno);
                    System.out.println("Train name-:" + rs1.getString(2));
                    System.out.println("Source Station-:" + from);
                    System.out.println("Destination Station-:" + to);
                    System.out.println("#######################################");
                    System.out.println("\n");
                }
            }

            System.out.println("Enter Train No In Which You Want To Go:");
            int traino = sc.nextInt();
            String sql8 = "select * from train;";
            Statement st8 = con.createStatement();
            System.out.println(traino);
            ResultSet rs8 = st8.executeQuery(sql8);
            String tname = "";
            while (rs8.next()) {
                if (rs8.getInt(1) == traino) {
                    tname = rs8.getString(2);
                    break;
                }
            }
            // display coach from database and ask
            String sql4 = "select * from train_coach_details where train_no = ?";
            PreparedStatement pst4 = con.prepareStatement(sql4);
            pst4.setInt(1, traino);
            ResultSet rs3 = pst4.executeQuery();

            System.out.println("Type of coaches avaliable in " + traino);

            while (rs3.next()) {
                if (rs3.getInt(3) > 0) {
                    System.out.println(rs3.getString(2));
                }
            }
            System.out.println("Enter Type Of Coach You Want To Book Ticket");
            coachtype = sc.next();
            String sql7 = "update train_coach_details set seat_available = seat_available-1 where coach = ?;";
            PreparedStatement pst7 = con.prepareStatement(sql7);
            pst7.setString(1, coachtype);
            int price = 0;
            String sql5 = "select * from stop_details where sname = ?";
            PreparedStatement pst5 = con.prepareStatement(sql5);
            pst5.setString(1, from);
            ResultSet rs4 = pst5.executeQuery();
            LocalTime time = null;
            int dist1 = 0, dist2 = 0;
            while (rs4.next()) {
                sn1 = rs4.getInt(2);
                time = rs4.getTime(6).toLocalTime();
                // System.out.println(rs4.getInt(2));
                dist1 = rs4.getInt(8);
            }
            String sql6 = "select * from stop_details where sname = ?";
            PreparedStatement pst6 = con.prepareStatement(sql6);
            pst6.setString(1, to);
            ResultSet rs5 = pst6.executeQuery();
            while (rs5.next()) {
                sn2 = rs5.getInt(2);
                dist2 = rs5.getInt(8);
            }
            switch (coachtype) {
                case "EC":
                    price = Math.abs(dist1 - dist2) * EC;
                    break;
                case "CC":
                    price = Math.abs(dist1 - dist2) * CC;
                    break;
                default:
                    break;
            }
            System.out.println("\nPrice = " + price + "\n");
            rs5.close();
            rs4.close();
            rs3.close();
            rs2.close();
            rs1.close();
            System.out.println("================Enter Personal Details================");
            System.out.println("Enter Age ");
            page = sc.nextInt();
            System.out.println("Enter First Name ");
            pname = sc.next();
            sc.nextLine();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
            String formattedDateTime = currentDateTime.format(formatter);
            int pnr = Integer.parseInt(formattedDateTime);
            System.out.println("=============Payment=============");
            Payment objPayment = new Payment(con);
            System.out.println("1->online mode"); 
            System.out.println("2->card mode");
            int pchoice = sc.nextInt();
            switch (pchoice) {
                case 1:
                    objPayment.onlinePayment(pnr, price,traino,tname,coachtype,dod,time.toString(),from,to);
                    break;
                case 2:
                    objPayment.getDebitCard(pnr, price,traino,tname,coachtype,dod,time.toString(),from,to);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
            String sql3 = "insert into ticket_details(pnr_no,train_no,train_name,coach_type,from_sn,destination_sn,departure_date,departure_time,p_name,p_age,ticket_price,status) values(?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst3 = con.prepareStatement(sql3);

            pst3.setInt(1, pnr);
            pst3.setInt(2, traino);
            pst3.setString(3, tname);
            pst3.setString(4, coachtype);
            pst3.setInt(5, sn1);
            pst3.setInt(6, sn2);
            pst3.setDate(7, java.sql.Date.valueOf(dod));
            pst3.setTime(8, java.sql.Time.valueOf(time));
            pst3.setString(9, pname);
            pst3.setInt(10, page);
            pst3.setInt(11, price);
            int i = pst7.executeUpdate();
            if (i > -1) {
                status = "CNF";
            } else {
                System.out.println("Some problem occured");
                return;
            }
            pst3.setString(12, status);
            pst3.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
            try {
                Thread.sleep(5000);
            } catch (Exception ie) {
                // TODO: handle exception
                System.out.println(e);
            }
        }
    }

    public void showAllTrain() {
        try {
            String sql1 = "select * from train;";
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(sql1);
            while (rs1.next()) {
                System.out.println("\n######################");
                System.out.println("Train No -:" + rs1.getInt(1));
                System.out.println("Train Name -:" + rs1.getString(2));
                System.out.println("######################\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchTrain() {
        try {

            String from, to;
            String sql1 = "select * from train;";
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery(sql1);

            String sql2 = "select * from stop_details where train_no = ?;";
            PreparedStatement pst2 = con.prepareStatement(sql2);
            ResultSet rs2 = null;

            System.out.println("================Enter Travling Details================");
            System.out.println("Enter Departure Station name");
            from = sc.nextLine();
            sc.nextLine();
            System.out.println("Enter Destination Station name");
            to = sc.nextLine();
            boolean f = false, t = false;
            int trno, sn1 = 0, sn2 = 0;
            String satname;
            while (rs1.next()) {
                trno = rs1.getInt(1);
                pst2.setInt(1, trno);
                rs2 = pst2.executeQuery();
                f = false;
                t = false;
                while (rs2.next()) {
                    satname = rs2.getString(4);
                    if (from.equalsIgnoreCase(satname)) {
                        sn1 = rs2.getInt(2);
                        f = true;
                    }
                    if (to.equalsIgnoreCase(satname)) {
                        sn2 = rs2.getInt(2);
                        t = true;
                    }
                }
                if ((f && t) && (sn2 > sn1)) {
                    System.out.println("Train no-:" + trno);
                    System.out.println("Train name-:" + rs1.getString(2));
                    System.out.println("Source Station-:" + from);
                    System.out.println("Destination Station-:" + to);
                    System.out.println("\n");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancleTicket() throws Exception {
        int pnr = 0;
        System.out.println("Enter PNR Number");
        pnr = sc.nextInt();
        String sql1 = "delete from ticket_details where pnr_no = ? ;";
        PreparedStatement pst1 = con.prepareStatement(sql1);
        pst1.setInt(1, pnr);
        String sql2 = "delete from payment_details where pnr_no = ? ;";
        PreparedStatement pst2 = con.prepareStatement(sql2);
        pst2.setInt(1, pnr);
        pst1.executeUpdate();
        pst2.executeUpdate();
    }
}