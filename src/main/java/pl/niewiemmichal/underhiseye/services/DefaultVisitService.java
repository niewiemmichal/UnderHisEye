package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitWithExaminationsDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;

import java.util.List;


@Service
public class DefaultVisitService implements VisitService
{
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RegistrantRepository registrantRepository;
    private final ExaminationService examinationService;

    @Autowired
    public DefaultVisitService(final DoctorRepository doctorRepository, final PatientRepository patientRepository,
                               final RegistrantRepository registrantRepository, final ExaminationService examinationService)
    {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.registrantRepository = registrantRepository;
        this.examinationService = examinationService;
    }

    @Override
    public void cancel(Long visitId, String reason) {}

    @Override
    public Visit register(VisitRegistrationDto visitRegistration) {

        /**
         * VisitRegistrationDto can be mapped to Visit using VisitMapper class
         */

        return null;
    }

    @Override
    public void end(Long visitId, VisitClosureDto visitClosure) {
        /**
         * LaboratoryExaminationDto can be mapped to LaboratoryExamination using ExaminationMapper class
         */
    }

    @Override
    public Visit get(Long id) {
        return null;
    }

    @Override
    public VisitWithExaminationsDto getFatVisit(Long id) {
        return null;
    }

    @Override
    public List<Visit> getAll() {
        return null;
    }
}
