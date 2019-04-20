package pl.niewiemmichal.underhiseye.commons.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VisitDto {

    private Long Id;
    private String description;
    private LocalDate date;
    private Long patientId;
    private Long doctorId;
    private Long registrantId;

}
