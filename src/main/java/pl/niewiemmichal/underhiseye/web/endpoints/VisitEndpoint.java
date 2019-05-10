package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitWithExaminationsDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.services.VisitService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Api(tags = {"visits"})
@RequestMapping (value = "visits", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class VisitEndpoint {

    private final VisitService visitService;

    @Autowired
    VisitEndpoint(VisitService visitService) {
        this.visitService = visitService;
    }

    @ApiOperation("Get all visits")
    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping
    public List<Visit> getAll() {
        return visitService.getAll();
    }

    @ApiOperation("Get visit's details")
    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping("/{id}")
    public VisitWithExaminationsDto get(@ApiParam(value = "Visit's id", required = true) @PathVariable Long id)
    {
        return visitService.getFatVisit(id);
    }

    @ApiOperation("Register visit")
    @RolesAllowed({"REGISTRANT"})
    @PostMapping
    public Visit registerVisit(
            @ApiParam(value = "Visit's details", required = true) @Valid @RequestBody VisitRegistrationDto dto)
    {
        return visitService.register(dto);
    }

    @ApiOperation("Cancel registered visit")
    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @PatchMapping("/cancel/{id}")
    public void cancelVisit(@ApiParam(value = "Registered visit's id", required = true) @PathVariable Long id,
            @ApiParam(value = "Reason for canceling visit", required = true) @RequestParam String reason)
    {
        visitService.cancel(id, reason);
    }

    @ApiOperation("End registered visit")
    @RolesAllowed({"DOCTOR"})
    @PatchMapping("/end/{id}")
    public void endVisit(@ApiParam(value = "Registered visit's id", required = true) @PathVariable Long id,
            @ApiParam(name = "details", value = "Visit ending details", required = true)
            @Valid @RequestBody VisitClosureDto dto)
    {
        visitService.end(id, dto);
    }
}
