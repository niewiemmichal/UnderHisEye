package pl.niewiemmichal.underhiseye.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Visit {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @Column(nullable = false, length = 8000)
    @Size(max = 8000)
    private String description;

    @Column(length = 8000)
    @Size(max = 8000)
    private String diagnosis;

    @NonNull
    @Column(nullable = false)
    private VisitStatus status;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @NonNull
    @Column(nullable = false)
    private LocalDate date;

    @Valid
    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Patient patient;

    @Valid
    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Registrant registrationSpecialist;

    @Valid
    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Doctor doctor;

    @PrePersist
    private void setDate() {
        registrationDate = LocalDate.now();
    }

}
