package maus.mausprojekt.model;

import android.arch.persistence.room.Entity;

import java.io.Serializable;
@Entity(tableName = "users")
public class User implements Serializable {
    private String UserEmail;
    private String PasswdUser;
    private static final long serialVersionUID = -7481912314472891511L;

    /**
     * Constructors
     */
    public User(){
    }

    public User(String userEmail, String passwdUser) {
        UserEmail = userEmail;
        PasswdUser = passwdUser;
    }

    /**
     * Getter and setter
     */
    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getPasswdUser() {
        return PasswdUser;
    }

    public void setPasswdUser(String passwdUser) {
        PasswdUser = passwdUser;
    }
}