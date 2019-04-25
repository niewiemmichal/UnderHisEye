package pl.niewiemmichal.underhiseye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class PhysicalExamination {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @NotEmpty
    @Column(nullable = false, length = 8000)
    @Size(max = 8000)
    private String result;

    @Valid
    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Examination examination;

    @JsonIgnore
    @Valid
    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Visit visit;
}
