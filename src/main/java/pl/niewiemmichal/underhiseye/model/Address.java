package pl.niewiemmichal.underhiseye.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    private Long id;

    @NonNull
    private String city;

    @NonNull
    private String street;

    @NonNull
    private String house_number;

    private String apartment;
}
