
public class Admin {
    private String adminUserName;
    private String adminUserPass;

    public Admin() {
        this.adminUserName = "admin";
        this.adminUserPass = "admin@123";

    }

    public boolean adminlogin(String u, String p) {
        if (u.equals(adminUserName) && p.equals(adminUserPass)) {
            return true;
        }
        return false;
    }

    public void showAdminMenue() {
        System.out.println("1-->Add Train");
        System.out.println("2-->Show All Train");
        System.out.println("3-->Exit");
    }
}
