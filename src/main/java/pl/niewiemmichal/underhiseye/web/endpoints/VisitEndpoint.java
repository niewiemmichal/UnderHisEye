package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitWithExaminationsDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.services.VisitService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RequestMapping ("visits")
@RestController
public class VisitEndpoint {

    private final VisitService visitService;

    @Autowired
    VisitEndpoint(VisitService visitService) {
        this.visitService = visitService;
    }

    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping
    public List<Visit> getAll() {
        return visitService.getAll();
    }

    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping("/{id}")
    public VisitWithExaminationsDto get(@PathVariable Long id) {
        return visitService.getFatVisit(id);
    }

    @RolesAllowed({"REGISTRANT"})
    @PostMapping
    public Visit registerVisit(@Valid @RequestBody VisitRegistrationDto dto){
        return visitService.register(dto);
    }

    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @PatchMapping("/cancel/{id}")
    public void cancelVisit(@PathVariable Long id, @RequestParam String reason) {
        visitService.cancel(id, reason);
    }

    @RolesAllowed({"DOCTOR"})
    @PatchMapping("/end/{id}")
    public void endVisit(@PathVariable Long id, @Valid @RequestBody VisitClosureDto dto) {
        visitService.end(id, dto);
    }
}
