package pl.niewiemmichal.underhiseye.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.VisitClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.VisitRegistrationDto;
import pl.niewiemmichal.underhiseye.commons.dto.mappers.VisitMapper;
import pl.niewiemmichal.underhiseye.commons.exceptions.BadRequestException;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.commons.dto.VisitWithExaminationsDto;
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
public class DefaultVisitService implements VisitService
{
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RegistrantRepository registrantRepository;
    private final VisitRepository visitRepository;
    private final ExaminationService examinationService;
    private final PhysicalExaminationRepository physicalExaminationRepository;
    private final LaboratoryExaminationRepository laboratoryExaminationRepository;
    private final VisitMapper visitMapper;

    @Autowired
    public DefaultVisitService(final DoctorRepository doctorRepository, final PatientRepository patientRepository,
                               final RegistrantRepository registrantRepository, final ExaminationService examinationService,
                               final VisitRepository visitRepository, final VisitMapper visitMapper,
                               final PhysicalExaminationRepository physicalExaminationRepository,
                               final LaboratoryExaminationRepository laboratoryExaminationRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.registrantRepository = registrantRepository;
        this.visitRepository = visitRepository;
        this.examinationService = examinationService;
        this.visitMapper = visitMapper;
        this.physicalExaminationRepository = physicalExaminationRepository;
        this.laboratoryExaminationRepository = laboratoryExaminationRepository;
    }

    @Override
    public void cancel(Long visitId, String reason) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
        if(changeState(visit.getStatus(), VisitStatus.CANCELED)) {
            visit.setStatus(changeState(visit, VisitStatus.CANCELED));
            visit.setDescription(reason);
            visitRepository.save(visit);
        }
    }

    @Override
    public Visit register(VisitRegistrationDto visitRegistration) {
        if(visitRegistration.getDate().isBefore(LocalDate.now()))
            throw new BadRequestException("Visit", "Date", visitRegistration.getDate().toString(), "date can't be from the past");
        doctorRepository.findById(visitRegistration.getDoctorId()).orElseThrow(()
                -> new BadRequestException("Doctor", "id", visitRegistration.getDoctorId().toString(), "Doctor does not exist"));
        patientRepository.findById(visitRegistration.getPatientId()).orElseThrow(()
                -> new BadRequestException("Doctor", "id", visitRegistration.getPatientId().toString(), "Patient does not exist"));
        registrantRepository.findById(visitRegistration.getRegistrantId()).orElseThrow(()
                -> new BadRequestException("Doctor", "id", visitRegistration.getRegistrantId().toString(), "Registrant does not exist"));
        Visit visit = visitMapper.toEntity(visitRegistration);
        //visit = visitMapper.toEntity(visitRegistration);
        /*
        Visit visit = new Visit(VisitStatus.REGISTERED, visitRegistration.getDate(),
                patientRepository.getOne(visitRegistration.getPatientId()),
                registrantRepository.getOne(visitRegistration.getRegistrantId()),
                doctorRepository.getOne(visitRegistration.getDoctorId()));
        */
        //visit = visitMapper.toEntity(visitRegistration);
        //Visit visit = wrapToEntity(() -> visitMapper.toEntity(visitRegistration));
        return visitRepository.save(visit);
    }

    @Override
    public void end(Long visitId, VisitClosureDto visitClosure) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
        if(visitClosure.getPhysicalExaminations() != null && !visitClosure.getPhysicalExaminations().isEmpty())
            examinationService.createPhysicalExaminations(visitClosure.getPhysicalExaminations());
        if(visitClosure.getLaboratoryExaminations() != null && !visitClosure.getLaboratoryExaminations().isEmpty())
            examinationService.createLaboratoryExaminations(visitClosure.getLaboratoryExaminations());
        if(visitClosure.getDiagnosis() != null && !visitClosure.getDiagnosis().isEmpty())
            visit.setDiagnosis(visitClosure.getDiagnosis());
        if(changeState(visit.getStatus(), VisitStatus.FINISHED)) {
            visit.setStatus(VisitStatus.FINISHED);
            visit.setDescription(visitClosure.getDescription());
            visitRepository.save(visit);
        }
    }

    @Override
    public Visit get(Long id) {
        return visitRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id",
                id.toString()));
    }

    @Override
    public VisitWithExaminationsDto getFatVisit(Long id) {
        Visit visit = visitRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id",
                id.toString()));

        VisitWithExaminationsDto visitWithExaminationsDto = new VisitWithExaminationsDto(visit,
                laboratoryExaminationRepository.findAllByVisit_Id(visit.getId()),
                physicalExaminationRepository.findAllByVisit_Id(visit.getId()));
        return visitWithExaminationsDto;
    }

    @Override
    public List<Visit> getAll() {
        return visitRepository.findAll();
    }

    private <T> T wrapToEntity(Supplier<T> toEntity) {
        try {
            return toEntity.get();
        } catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    private VisitStatus changeState(Visit visit, VisitStatus endStatus) {
        if(visit.getStatus() == VisitStatus.REGISTERED || visit.getStatus().equals(endStatus)) return endStatus;
        else throw new BadRequestException("Visit", "status", visit.getStatus().toString(), "can't be canceled");
    }

    private boolean changeState(VisitStatus current, VisitStatus desired) {
        if(current == VisitStatus.REGISTERED) return true;
        else if(current.equals(desired)) return false;
        else throw new BadRequestException("Visit", "status", current.toString(), "visit is currently" + current.toString());
    }
}
