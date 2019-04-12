package pl.niewiemmichal.underhiseye.web.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.model.Examination;
import pl.niewiemmichal.underhiseye.repository.ExaminationRepository;

import java.util.List;

@RequestMapping ("/examinations")
@RestController
public class ExaminationEndpoint {

    private final ExaminationRepository examinationRepository;

    ExaminationEndpoint(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @GetMapping ("/{code}")
    public Examination getExamination(@PathVariable String code){
        return examinationRepository.findById(code).orElseThrow(()
                -> new ResourceDoesNotExistException("Examination","id",code));
    }

    @GetMapping
    public List<Examination> getAllExaminations(){
        return examinationRepository.findAll();
    }

}

