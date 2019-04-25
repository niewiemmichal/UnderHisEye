package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Examination;
import pl.niewiemmichal.underhiseye.repositories.ExaminationRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RequestMapping("icd")
@RestController
public class IcdEndpoint {

    private final ExaminationRepository examinationRepository;

    @Autowired
    IcdEndpoint(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @RolesAllowed({"DOCTOR", "REGISTRANT"})
    @GetMapping("{code}")
    public Examination getExamination(@PathVariable String code){
        return examinationRepository.findById(code).orElseThrow(()
                -> new ResourceDoesNotExistException("Examination","id",code));
    }

    @RolesAllowed({"DOCTOR"})
    @GetMapping
    public List<Examination> getAllExaminations(){
        return examinationRepository.findAll();
    }
}
