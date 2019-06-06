package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SupervisorClosureDto {

    @Size(max = 8000)
    private String note;

    @NonNull
    private Long id;

}
