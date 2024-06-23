package src.main.java.com.cafeteria;

public class UserSession {
    private String employeeId;
    private String loginTime;
    private String logoutTime;

    public UserSession(String employeeId, String loginTime, String logoutTime) {
        this.employeeId = employeeId;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public String getLogoutTime() {
        return logoutTime;
    }
}
