package pl.niewiemmichal.underhiseye.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhysicalExamination {

    @Id @GeneratedValue
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String resukt;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Examination examination;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Visit visit;

}
