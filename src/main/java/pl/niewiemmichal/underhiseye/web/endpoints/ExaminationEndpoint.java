package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.services.ExaminationService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = {"examinations"})
@RequestMapping (value = "examinations", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ExaminationEndpoint {

    private final ExaminationService examinationService;

    @Autowired
    public ExaminationEndpoint(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @ApiOperation(value = "Finish ordered laboratory examination")
    @RolesAllowed({"ASSISTANT"})
    @PatchMapping("/finish/{id}")
    public LaboratoryExamination finish(
            @ApiParam(value = "Laboratory examination's id", required = true) @PathVariable Long id,
            @ApiParam(name = "details", value = "Assistant's id and result of examination", required = true)
            @Valid @RequestBody AssistantClosureDto closure)
    {
        return examinationService.finish(id, closure);
    }

    @ApiOperation(value = "Cancel ordered laboratory examination")
    @RolesAllowed({"ASSISTANT"})
    @PatchMapping("/cancel/{id}")
    public LaboratoryExamination cancel(
            @ApiParam(value = "Laboratory examination's id", required = true) @PathVariable Long id,
            @ApiParam(name = "details", value = "Assistant's id and reason for canceling", required = true)
            @Valid @RequestBody AssistantClosureDto closure)
    {
        return examinationService.cancel(id, closure);
    }

    @ApiOperation(value = "Reject finished laboratory examination")
    @RolesAllowed({"SUPERVISOR"})
    @PatchMapping("/reject/{id}")
    public LaboratoryExamination reject(
            @ApiParam(value = "Laboratory examination's id", required = true) @PathVariable Long id,
            @ApiParam(name = "details", value = "Supervisor's id and reason for rejecting", required = true)
            @Valid @RequestBody SupervisorClosureDto closure)
    {
        return examinationService.reject(id, closure);
    }

    @ApiOperation(value = "Approve finished laboratory examination")
    @RolesAllowed({"SUPERVISOR"})
    @PatchMapping("/approve/{id}")
    public LaboratoryExamination approve(
            @ApiParam(value = "Laboratory examination's id", required = true) @PathVariable Long id,
            @ApiParam(value = "Supervisor's id", required = true) @RequestParam Long supervisorId) {
        return examinationService.approve(id, supervisorId);
    }
}

