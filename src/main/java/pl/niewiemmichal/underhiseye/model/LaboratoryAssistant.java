package pl.niewiemmichal.underhiseye.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

}
