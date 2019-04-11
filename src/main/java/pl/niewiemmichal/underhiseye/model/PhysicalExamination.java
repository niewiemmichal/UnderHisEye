package pl.niewiemmichal.underhiseye.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhysicalExamination {

    @Id @GeneratedValue
    private Integer id;

    @NonNull
    @NotEmpty
    @Column(nullable = false, length = 8000)
    @Size(max = 8000)
    private String result;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Examination examination;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Visit visit;

}
