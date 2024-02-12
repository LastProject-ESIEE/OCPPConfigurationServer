//package fr.uge.chargepointconfiguration.entities;
//
//import jakarta.persistence.*;
////import jakarta.validation.constraints.NotNull;
////import org.jetbrains.annotations.NotNull;
//
//@Entity
//@Table(name = "user")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
//    @Column(name = "contributor_email", nullable = false)
//    private String email;
//   // @NotNull(message = "the lastName of the user is required, can not be null")
//    private String lastName;
//    //@NotNull(message = "the firstName of the user is required, can not be null")
//    private String firstName;
//   // @NotNull(message = "the password of the user is required, can not be null")
//    private String password;
//
//    public User() {
//        lastName = null;
//    }
//
//    private enum Role{
//       VISUALIZER, EDITOR, ADMINISTRATOR;
//    };
//    //@NotNull(message = "the role of the user is required, can not be null")
//    private Role role;
//
//
//
//
//
//    /*public User(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }*/
//
//    //SETTER
//    public void setEmail(String email){
//        this.email = email;
//    }
//    public void setLastName(String lastName){
//        this.lastName = lastName;
//    }
//
//    public void setFirstName(String firstName){
//        this.firstName = firstName;
//    }
//    public void setPassword(String password){
//        this.password= password;
//    }
//
//    private void setRole(Role role){
//        this.role = role;
//    }
//
//    //GETTER
//    public String getEmail(){
//        return email;
//    }
//
//
//    public int getId(){
//        return id;
//    }
//    public String getPassword(){
//        return password;
//    }
//    public String getLastName(){
//        return lastName;
//    }
//
//    public String getFirstName(){
//        return firstName;
//    }
//
//    public Role getRole(){
//        return role;
//    }
//
//    /*public User() {
//        this(null, null);
//    }*/
//
//    @Override
//    public String toString(){
//        return id + " - " + lastName + " " + firstName + " - " + email;
//    }
//
//    // standard constructors / setters / getters / toString
//
//
//
//}