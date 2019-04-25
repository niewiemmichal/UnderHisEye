package pl.niewiemmichal.underhiseye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
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

    @JoinColumn(nullable = false)
    @ManyToOne
    private LaboratorySupervisor supervisor;

    @JoinColumn(nullable = false)
    @ManyToOne
    private LaboratoryAssistant assistant;

    @JsonIgnore
    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Visit visit;

    @PrePersist
    private void calculateOrderDate() {
        orderDate = LocalDate.now();
    }

}
