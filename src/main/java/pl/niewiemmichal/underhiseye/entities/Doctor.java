package pl.niewiemmichal.underhiseye.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Doctor {

    @Id @GeneratedValue
    private Long id;

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

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}
