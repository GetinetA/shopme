package com.jabirinc.shopmecommon.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Getinet on 9/12/21
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "firstName", "lastName", "roles"})

@Entity
@Table(name = "users")
public class User {

    public static final String DEFAULT_USER_IMAGE_PATH = "/images/default-user.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false) // encoded password length is 64
    private String password;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(length = 64)
    private String photos;

    private boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Transient
    public String getPhotosImagePath() {

        if (id == null || photos == null) {
            return DEFAULT_USER_IMAGE_PATH;
        }
        return "/user-photos/" + this.id + "/" + this.photos;
    }

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
