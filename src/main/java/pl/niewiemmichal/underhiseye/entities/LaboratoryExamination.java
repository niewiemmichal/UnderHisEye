package pl.niewiemmichal.underhiseye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LaboratoryExamination {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 8000)
    @Size(max = 8000)
    @Type(type="text")
    private String note;

    @Column(length = 8000)
    @Size(max = 8000)
    @Type(type="text")
    private String result;

    @NonNull
    @Column(nullable = false)
    private LaboratoryExamStatus status;

    @Column(length = 8000)
    @Size(max = 8000)
    @Type(type="text")
    private String supervisorNote;

    @Column(nullable = false)
    private LocalDate orderDate;

    private LocalDate completionDate;

    private LocalDate approvalDate;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Examination examination;

    @ManyToOne
    private LaboratorySupervisor supervisor;

    @ManyToOne
    private LaboratoryAssistant assistant;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Visit visit;

    @PrePersist
    private void calculateOrderDate() {
        orderDate = LocalDate.now();
    }

}
