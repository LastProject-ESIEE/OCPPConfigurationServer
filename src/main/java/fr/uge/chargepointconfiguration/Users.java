package fr.uge.chargepointconfiguration;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;

    public Users(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Users() {
        this(null, null);
    }

    // standard constructors / setters / getters / toString


    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    @Override
    public String toString(){
        return id + " - " + name + " - " + email;
    }
}