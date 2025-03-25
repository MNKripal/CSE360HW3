package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */

//same as TP 1 code short of including an id
public class User {
    private String userName;
    private String password;
    private String role;
    private int id;

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.id =0;
    }
    
    public User(int id, String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.id = id ;
    }
    
    // Sets the role of the user.
    public void setRole(String role) {
    	this.role=role;
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getId() {return id;}
    
    //setter
    public void setId(int id) {
    	this.id = id;
    	return;
    }
}
