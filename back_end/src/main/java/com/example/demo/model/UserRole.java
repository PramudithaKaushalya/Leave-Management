// package com.example.demo.model;

// import javax.persistence.*;
// import javax.validation.constraints.NotBlank;

// @Entity
// @Table(name = "user_roles")
// public class UserRole {

//     @Id
//     @GeneratedValue(strategy= GenerationType.IDENTITY)
//     @Column(name = "id")
//     private Long id;

//     @NotBlank
//     @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.EAGER)
//     @JoinColumn(name = "user_id")
//     private User user;

//     @NotBlank
//     @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.EAGER)
//     @JoinColumn(name = "role_id")
//     private Role role;

//     public UserRole() {

//     }

//     public UserRole(User user, Role role) {
//         this.user = user;
//         this.role = role;
//     }

//     public User getUser() {
//         return user;
//     }

//     public void setUser(User user) {
//         this.user = user;
//     }

//     public Role getRole() {
//         return role;
//     }

//     public void setRole(Role role) {
//         this.role = role;
//     }  
// }