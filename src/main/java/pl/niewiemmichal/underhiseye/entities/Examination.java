package pl.niewiemmichal.underhiseye.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Examination {

    @Id
    @NonNull
    @NotEmpty
    private String code;

    @NonNull
    @NotEmpty
    @Column(nullable = false, length = 512)
    @Size(max = 512)
    private String name;

}
