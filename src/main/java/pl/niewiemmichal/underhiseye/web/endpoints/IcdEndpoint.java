package pl.niewiemmichal.underhiseye.web.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.Examination;
import pl.niewiemmichal.underhiseye.repositories.ExaminationRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Api(tags = {"icd"})
@RequestMapping(value = "icd", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class IcdEndpoint {

    private final ExaminationRepository examinationRepository;

    @Autowired
    IcdEndpoint(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @ApiOperation("Get medical procedure details by ICD-9 code")
    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping("{code}")
    public Examination getExamination(@ApiParam(value = "ICD-9 code", required = true) @PathVariable String code) {
        return examinationRepository.findById(code).orElseThrow(()
                -> new ResourceDoesNotExistException("Examination","id",code));
    }

    @ApiOperation("Get all ICD-9 medical procedures")
    @RolesAllowed({"ADMINISTRATOR"})
    @GetMapping
    public List<Examination> getAllExaminations() {
        return examinationRepository.findAll();
    }
}
