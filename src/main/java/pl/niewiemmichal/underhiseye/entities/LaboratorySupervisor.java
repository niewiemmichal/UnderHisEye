package pl.niewiemmichal.underhiseye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LaboratorySupervisor {

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
    @OneToOne
    private User user;
}
