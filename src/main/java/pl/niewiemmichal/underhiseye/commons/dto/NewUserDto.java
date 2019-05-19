package pl.niewiemmichal.underhiseye.commons.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewUserDto {
    @NonNull
    @NotBlank
    private String username;

    @NonNull
    @NotBlank
    private String password;

    @NonNull
    @NotBlank
    private String firstName;

    @NonNull
    @NotBlank
    private String lastName;

    private String gmcNumber;
}
