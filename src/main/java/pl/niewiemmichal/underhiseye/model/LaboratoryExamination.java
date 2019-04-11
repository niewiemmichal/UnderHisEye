package pl.niewiemmichal.underhiseye.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @NonNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Temporal(TemporalType.DATE)
    private Date completionDate;

    @Temporal(TemporalType.DATE)
    private Date approvalDate;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Examination examination;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private LaboratorySupervisor supervisor;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private LaboratoryAssistant assistant;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Visit visit;

}
