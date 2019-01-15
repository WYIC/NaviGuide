package my.edu.tarc.naviguide;

public class User {
    private String UserName;
    private String Password;
    private String Description;

    public User() {
    }

    public User(String userName, String password, String description) {
        UserName = userName;
        Password = password;
        Description = description;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
