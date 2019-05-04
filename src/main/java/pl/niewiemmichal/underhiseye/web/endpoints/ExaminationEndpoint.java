package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.services.ExaminationService;

import javax.annotation.security.RolesAllowed;
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

    @RolesAllowed({"ASSISTANT"})
    @PatchMapping("/finish/{id}")
    public LaboratoryExamination finish(@PathVariable Long id, @Valid @RequestBody AssistantClosureDto closure) {
        return examinationService.finish(id, closure);
    }

    @RolesAllowed({"ASSISTANT"})
    @PatchMapping("/cancel/{id}")
    public LaboratoryExamination cancel(@PathVariable Long id, @Valid @RequestBody AssistantClosureDto closure) {
        return examinationService.cancel(id, closure);
    }

    @RolesAllowed({"SUPERVISOR"})
    @PatchMapping("/reject/{id}")
    public LaboratoryExamination reject(@PathVariable Long id, @Valid @RequestBody SupervisorClosureDto closure) {
        return examinationService.reject(id, closure);
    }

    @RolesAllowed({"SUPERVISOR"})
    @PatchMapping("/approve/{id}")
    public LaboratoryExamination approve(@PathVariable Long id, @RequestParam Long supervisorId) {
        return examinationService.approve(id, supervisorId);
    }
}

