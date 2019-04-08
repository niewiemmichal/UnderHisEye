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
public class LaboratoryExamination {

    @Id @GeneratedValue
    private Integer id;

    @Lob
    private String note;

    @Lob
    private String result;

    @NonNull
    @Column(nullable = false)
    private Integer status;

    @Lob
    private String supervisorNote;

    @NonNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date completionDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date approvalDate;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private PhysicalExamination examination;

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
