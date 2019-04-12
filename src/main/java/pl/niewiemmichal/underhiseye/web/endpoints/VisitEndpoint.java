package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.model.Visit;
import pl.niewiemmichal.underhiseye.repository.VisitRepository;

import java.util.List;

@RequestMapping ("/visits")
@RestController
public class VisitEndpoint {

    private final VisitRepository visitRepository;

    VisitEndpoint(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @GetMapping ("/{id}")
    public Visit getVisit(@PathVariable Long id){
        return visitRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit","id",id.toString()));
    }

    @GetMapping
    public List<Visit> getAllVisits(){
        return (List<Visit>) visitRepository.findAll();
    }

    @PostMapping
    public Visit addVisit(@RequestBody Visit newVisit){
        return visitRepository.save(newVisit);
    }

    @PutMapping("/{id}")
    public Visit updateVisit(@RequestBody Visit newVisit, @PathVariable Long id) {
        if(!visitRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Visit", "id", id.toString());
        else if(newVisit.getId() != null && !(id.equals(newVisit.getId())))
            throw new ResourceConflictException("Visit", "id", id.toString(), newVisit.getId().toString());
        else {
            newVisit.setId(id);
            return visitRepository.save(newVisit);
        }
    }
}
