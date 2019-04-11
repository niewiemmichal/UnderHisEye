package pl.niewiemmichal.underhiseye.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Visit {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 8000)
    @Size(max = 8000)
    private String description;

    @Column(nullable = false, length = 8000)
    @Size(max = 8000)
    private String diagnosis;

    @NonNull
    @Column(nullable = false)
    private VisitStatus status;

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
