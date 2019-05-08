package pl.niewiemmichal.underhiseye.services;

import lombok.NonNull;
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
import java.util.stream.Collectors;

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
    public void cancel(@NonNull Long visitId,@NonNull String reason) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
        if(changeState(visit.getStatus(), VisitStatus.CANCELED)) {
            visit.setStatus(changeState(visit, VisitStatus.CANCELED));
            visit.setDescription(reason);
            visitRepository.save(visit);
        }
    }

    @Override
    public Visit register(@NonNull VisitRegistrationDto visitRegistration) {
        if(visitRegistration.getDate().isBefore(LocalDate.now()))
            throw new BadRequestException("Visit", "Date", visitRegistration.getDate().toString(), "date can't be from the past");
        Visit visit;
        try {
            visit = visitMapper.toEntity(visitRegistration);
        } catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
        return visitRepository.save(visit);
    }

    @Override
    public void end(@NonNull Long visitId,@NonNull VisitClosureDto visitClosure) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", visitId.toString()));
        if(visitClosure.getPhysicalExaminations() != null && !visitClosure.getPhysicalExaminations().isEmpty())
            examinationService.createPhysicalExaminations(visitClosure.getPhysicalExaminations().stream().collect(Collectors.toList()));
        if(visitClosure.getLaboratoryExaminations() != null && !visitClosure.getLaboratoryExaminations().isEmpty())
            examinationService.createLaboratoryExaminations(visitClosure.getLaboratoryExaminations().stream().collect(Collectors.toList()));
        if(visitClosure.getDiagnosis() != null && !visitClosure.getDiagnosis().isEmpty())
            visit.setDiagnosis(visitClosure.getDiagnosis());

        if(changeState(visit.getStatus(), VisitStatus.FINISHED)) {
            visit.setStatus(changeState(visit, VisitStatus.FINISHED));
            visit.setDescription(visitClosure.getDescription());
            visitRepository.save(visit);
        }
    }

    @Override
    public Visit get(@NonNull Long id) {
        return visitRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id",
                id.toString()));
    }

    @Override
    public VisitWithExaminationsDto getFatVisit(@NonNull Long id) {
        Visit visit = visitRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id",
                id.toString()));

        return new VisitWithExaminationsDto(visit,
                examinationService.getAllLaboratoryExaminationsByVisit(visit.getId()),
                examinationService.getAllPhysicalExaminationsByVisit(visit.getId()));
    }

    @Override
    public List<Visit> getAll() {
        return visitRepository.findAll();
    }

    private VisitStatus changeState(@NonNull Visit visit, @NonNull VisitStatus endStatus) {
        if(visit.getStatus() == VisitStatus.REGISTERED || visit.getStatus().equals(endStatus)) return endStatus;
        else throw new BadRequestException("Visit", "status", visit.getStatus().toString(), "can't be canceled");
    }

    private boolean changeState(@NonNull VisitStatus current, @NonNull VisitStatus desired) {
        if(current == VisitStatus.REGISTERED) return true;
        else if(current.equals(desired)) return false;
        else throw new BadRequestException("Visit", "status", current.toString(), "visit is currently" + current.toString());
    }
}
