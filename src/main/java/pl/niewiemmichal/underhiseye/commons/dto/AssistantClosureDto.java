package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public class AssistantClosureDto {

    @NonNull
    @Size(max = 8000)
    private String result;

    @NonNull
    private Long assistantId;

}
