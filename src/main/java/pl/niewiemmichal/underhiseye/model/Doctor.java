package pl.niewiemmichal.underhiseye.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor {

    @Id @GeneratedValue
    private Integer id;

    @NonNull
    @NotBlank
    @Column(nullable = false, length =  50)
    @Size(max = 50)
    private String name;

    @NonNull
    @NotBlank
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    private String surname;

    @NonNull
    @NotBlank
    @Column(nullable = false, length = 7)
    @Size(min = 7, max = 7)
    private String gmcNumber;

}
