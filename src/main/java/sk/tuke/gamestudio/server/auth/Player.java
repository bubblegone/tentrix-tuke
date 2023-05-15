package sk.tuke.gamestudio.server.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
public class Player implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String password;

    public Player(){

    }

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public void hashPassword(){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(this.password.getBytes());
            this.password = new String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
