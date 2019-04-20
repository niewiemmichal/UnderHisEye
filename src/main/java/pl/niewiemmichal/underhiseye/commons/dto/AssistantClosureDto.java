package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class AssistantClosureDto {

    @NonNull
    @Size(max = 8000)
    private String note;

    @NonNull
    @Size(max = 8000)
    private String result;

    @NonNull
    private Integer assistantId;

}
