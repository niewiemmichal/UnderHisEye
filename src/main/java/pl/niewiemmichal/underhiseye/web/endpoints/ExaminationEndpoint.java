package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.annotations.IsAssistant;
import pl.niewiemmichal.underhiseye.commons.annotations.IsDoctor;
import pl.niewiemmichal.underhiseye.commons.annotations.IsSupervisor;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Examination;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.repositories.ExaminationRepository;
import pl.niewiemmichal.underhiseye.services.ExaminationService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping ("examinations")
@RestController
public class ExaminationEndpoint {

    private final ExaminationService examinationService;

    @Autowired
    public ExaminationEndpoint(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @IsAssistant
    @PatchMapping("/finish/{id}")
    public LaboratoryExamination finish(@PathVariable Long id, @Valid @RequestBody AssistantClosureDto dto) {
        return examinationService.finish(id, dto);
    }

    @IsAssistant
    @PatchMapping("/cancel/{id}")
    public LaboratoryExamination cancel(@PathVariable Long id, @Valid @RequestBody AssistantClosureDto dto) {
        return examinationService.cancel(id, dto);
    }

    @IsSupervisor
    @PatchMapping("/reject/{id}")
    public LaboratoryExamination reject(@PathVariable Long id, @Valid @RequestBody SupervisorClosureDto dto) {
        return examinationService.reject(id, dto);
    }

    @IsSupervisor
    @PatchMapping("/approve/{id}")
    public LaboratoryExamination approve(@PathVariable Long id, @RequestParam Long supervisorId) {
        return examinationService.approve(id, supervisorId);
    }
}

