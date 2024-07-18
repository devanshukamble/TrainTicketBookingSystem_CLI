// import src.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Irctc {
    public static void showMainMenue() {
        System.out.println("Welcome To IRCTC\n");
        System.out.println("1->Admin Login");
        System.out.println("2->Customer Login");
        System.out.println("3->Create New Account");
        System.out.println("0->Exit");
    }

    
    public static void main(String[] args) throws Exception {
        // database connection
        String dburl = "jdbc:mysql://localhost:3306/irctc";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.jdbc.Driver";
        Class.forName(drivername);
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        if (con != null) {
            System.out.println("Connection sucessful");
        } else {
            System.out.println("Connection unsucessful");
        }
        Scanner sc = new Scanner(System.in);
        Customer objCustomer = new Customer(con);
        Admin objAdmin = new Admin();
        Train objTrain = new Train(con);
        int choiceMain, choiceMain1, choiceMain2;
        boolean exitInnerCase;
        while (true) {
            System.out.print("\033[H\033[2J");
            showMainMenue();
            choiceMain = sc.nextInt();
            switch (choiceMain) {
                case 1:
                    exitInnerCase = true;
                    System.out.print("\033[H\033[2J");
                    System.out.println("\nEnter username and password:");
                    if (objAdmin.adminlogin(sc.next(), sc.next())) {
                        do {
                            objAdmin.showAdminMenue();
                            choiceMain1 = sc.nextInt();
                            switch (choiceMain1) {
                                case 1:
                                    System.out.print("\033[H\033[2J");
                                    objTrain.addTrain();
                                    break;
                                case 2:
                                    System.out.print("\033[H\033[2J");
                                    objTrain.showAllTrain();
                                case 0:
                                    exitInnerCase = false;
                                    break;
                                default:
                                    break;
                            }
                            System.out.println("Press Enter To Proced");
                            sc.nextLine();
                            sc.nextLine();
                            System.out.print("\033[H\033[2J");
                        } while (exitInnerCase);
                        break;
                    } else {
                        System.out.println("Invalid Username or Password");
                        Thread.sleep(3000);
                    }
                    break;
                case 2:
                    exitInnerCase = true;
                    System.out.print("\033[H\033[2J");
                    System.out.println("\nEnter username and password:");
                    if (objCustomer.customerLogin(sc.next(), sc.next())) {
                        do {
                            System.out.print("\033[H\033[2J");
                            objCustomer.showCustomerMenue();
                            choiceMain2 = sc.nextInt();
                            switch (choiceMain2) {
                                case 1:
                                    System.out.print("\033[H\033[2J");
                                    objTrain.bookTicket();
                                    break;
                                case 2:
                                    objTrain.cancleTicket();
                                    break;
                                case 3:
                                    System.out.print("\033[H\033[2J");
                                    objTrain.searchTrain();
                                    break;
                                case 0:
                                    exitInnerCase = false;
                                    break;
                                default:
                                    break;
                            }
                            System.out.println("Press Enter To Proced");
                            sc.nextLine();
                            sc.nextLine();
                            System.out.print("\033[H\033[2J");
                        } while (exitInnerCase);
                    }
                    break;
                case 3:
                    System.out.print("\033[H\033[2J");
                    objCustomer.registration();
                case 0:
                    System.exit(0);
                    break;
                default:
                    break;
            }
            System.out.print("\033[H\033[2J");
        }
    }
}