package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.repositories.DoctorRepository;
import pl.niewiemmichal.underhiseye.repositories.RegistrantRepository;
import pl.niewiemmichal.underhiseye.repositories.PatientRepository;
import pl.niewiemmichal.underhiseye.repositories.VisitRepository;

import java.time.LocalDate;
import java.util.List;


@Service
public class DefaultVisitService implements VisitService
{
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RegistrantRepository registrantRepository;
    private final VisitRepository visitRepository;
    private final ExaminationService examinationService;

    private final VisitStateValidator visitStateValidator;

    @Autowired
    public DefaultVisitService(final DoctorRepository doctorRepository, final PatientRepository patientRepository,
                               final RegistrantRepository registrantRepository, final ExaminationService examinationService,
                               final VisitRepository visitRepository)
    {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.registrantRepository = registrantRepository;
        this.visitRepository = visitRepository;
        this.examinationService = examinationService;

        visitStateValidator = new VisitStateValidator();
}

    @Override
    public void cancel(Long visitId, String reason) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
        if(!visitStateValidator.cancelableVisit(visit.getStatus()))
            throw new BadRequestException();
        visit.setStatus(VisitStatus.CANCELED);
        visit.setDescription(reason);
        visitRepository.save(visit);
    }

    @Override
    public Visit register(VisitRegistrationDto visitRegistration) {
        Doctor doctor = doctorRepository.findById(visitRegistration.getDoctorId()).orElseThrow(()
                -> new ResourceDoesNotExistException("Doctor", "id", visitRegistration.getDoctorId().toString()));
        Patient patient = patientRepository.findById(visitRegistration.getPatientId()).orElseThrow(()
                -> new ResourceDoesNotExistException("Patient", "id", visitRegistration.getPatientId().toString()));
        Registrant registrant = registrantRepository.findById(visitRegistration.getRegistrantId()).orElseThrow(()
                -> new ResourceDoesNotExistException("Registration Specialist", "id", visitRegistration.getRegistrantId().toString()));

        if(visitRegistration.getDate().isBefore(LocalDate.now()))
            throw new BadRequestException();
        Visit visit = new Visit(
                "",
                VisitStatus.REGISTERED,
                visitRegistration.getDate(),
                patient,
                registrant,
                doctor);
        visit.setStatus(VisitStatus.REGISTERED);
        return visitRepository.save(visit);
    }

    @Override
    public void end(Long visitId, VisitClosureDto visitClosure) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
        if(!visitStateValidator.finishableVisit(visit.getStatus()))
            throw new BadRequestException();
        if(visitClosure.getPhysicalExaminations() != null)
        {
            examinationService.createPhysicalExaminations(visitClosure.getPhysicalExaminations());
        }

        if(visitClosure.getLaboratoryExaminationCodes() != null)
        {
            examinationService.createLaboratoryExaminations(visitClosure.getLaboratoryExaminationCodes());
        }

        if(visitClosure.getDiagnosis() != null)
            visit.setDiagnosis(visitClosure.getDiagnosis());
        visit.setStatus(VisitStatus.FINISHED);
        visit.setDescription(visitClosure.getDescription());
        visitRepository.save(visit);
    }

    @Override
    public Visit get(Long id) {
        return visitRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", id.toString()));
    }

    @Override
    public List<Visit> getAll() {
        return visitRepository.findAll();
    }
}
