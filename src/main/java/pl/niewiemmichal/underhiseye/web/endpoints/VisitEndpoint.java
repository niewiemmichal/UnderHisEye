package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.annotations.IsDoctor;
import pl.niewiemmichal.underhiseye.commons.annotations.IsRegistrant;
import pl.niewiemmichal.underhiseye.commons.annotations.IsVisitViewer;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitWithExaminationsDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.repositories.VisitRepository;
import pl.niewiemmichal.underhiseye.services.VisitService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

@RequestMapping ("visits")
@RestController
public class VisitEndpoint {

    private final VisitService visitService;

    @Autowired
    VisitEndpoint(VisitService visitService) {
        this.visitService = visitService;
    }

    @IsVisitViewer
    @GetMapping
    public List<Visit> getAll() {
        return visitService.getAll();
    }

    @IsVisitViewer
    @GetMapping("/{id}")
    public VisitWithExaminationsDto get(@PathVariable Long id) {
        return visitService.getFatVisit(id);
    }

    @IsRegistrant
    @PostMapping
    public Visit registerVisit(@Valid @RequestBody VisitRegistrationDto dto){
        return visitService.register(dto);
    }

    @IsVisitViewer
    @PatchMapping("/cancel/{id}")
    public void cancelVisit(@PathVariable Long id, @RequestParam String reason) {
        visitService.cancel(id, reason);
    }

    @IsDoctor
    @PatchMapping("/end/{id}")
    public void endVisit(@PathVariable Long id, @Valid @RequestBody VisitClosureDto dto) {
        visitService.end(id, dto);
    }
}
