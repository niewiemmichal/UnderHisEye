package pl.niewiemmichal.underhiseye.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LaboratoryExamination {

    @Id @GeneratedValue
    private Integer id;

    @Column(length = 8000)
    @Size(max = 8000)
    private String note;

    @Column(length = 8000)
    @Size(max = 8000)
    private String result;

    @NonNull
    @Column(nullable = false)
    private LaboratoryExamStatus status;

    @Column(length = 8000)
    @Size(max = 8000)
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
