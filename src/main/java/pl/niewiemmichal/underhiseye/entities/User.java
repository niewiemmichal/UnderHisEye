package pl.niewiemmichal.underhiseye.entities;

import lombok.NonNull;

import javax.persistence.Entity;

@Entity
public class User {

    @NonNull
    private String username;

    @NonNull
    private String password;

    //private Set<Role> roles;

}
