package pl.niewiemmichal.underhiseye.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Visit {

    @Id @GeneratedValue
    private Integer id;

    @Lob
    private String description;

    @Lob
    private String diagnosis;

    @NonNull
    @Column(nullable = false)
    private Integer status;

    @NonNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @NonNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Patient patient;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private PatientRegistrationSpecialist registrationSpecialist;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Doctor doctor;

}
