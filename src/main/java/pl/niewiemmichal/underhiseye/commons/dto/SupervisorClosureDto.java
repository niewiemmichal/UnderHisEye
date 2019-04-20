package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class SupervisorClosureDto {

    @NonNull
    @Size(max = 8000)
    private String supervisorNote;

    @NonNull
    private Integer supervisorId;

}
