package pl.niewiemmichal.underhiseye.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class Visit {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 8000)
    @Size(max = 8000)
    private String description;

    @Column(length = 8000)
    @Size(max = 8000)
    private String diagnosis;

    @NonNull
    @Column(nullable = false)
    private VisitStatus status;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime date;

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
        registrationDate = LocalDateTime.now();
    }

}
