package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewUserDto {
    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String gmcNumber;
}
