package pl.niewiemmichal.underhiseye.model;

import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Patient {

    @Id
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String surname;

    @NonNull
    private String personalIdentityNumber;

    @ManyToOne
    private Address address;
}
