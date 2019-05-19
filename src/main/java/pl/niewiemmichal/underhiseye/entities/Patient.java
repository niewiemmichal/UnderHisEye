package pl.niewiemmichal.underhiseye.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    private String name;

    @NonNull
    @NotBlank
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    private String surname;

    @NonNull
    @NotBlank
    @Column(nullable = false,length = 11)
    @Size(min = 11, max = 11)
    private String personalIdentityNumber;

    @NonNull
    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;
}
