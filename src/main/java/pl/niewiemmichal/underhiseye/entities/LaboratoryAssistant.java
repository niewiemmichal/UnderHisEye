package pl.niewiemmichal.underhiseye.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LaboratoryAssistant {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @NotEmpty
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    private String name;

    @NonNull
    @NotEmpty
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    private String surname;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
