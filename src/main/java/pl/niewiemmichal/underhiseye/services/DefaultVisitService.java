package pl.niewiemmichal.underhiseye.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DefaultVisitService implements VisitService
{
    private final VisitRepository visitRepository;
    private final ExaminationService examinationService;
    private final VisitMapper visitMapper;

    private final Logger logger = LoggerFactory.getLogger(DefaultVisitService.class);

    @Autowired
    public DefaultVisitService(final ExaminationService examinationService, final VisitRepository visitRepository,
                               final VisitMapper visitMapper) {
        this.visitRepository = visitRepository;
        this.examinationService = examinationService;
        this.visitMapper = visitMapper;
    }

    @Override
    public void cancel(@NonNull Long visitId, @NonNull String reason) {
        logger.info("Canceling visit with id={}", visitId);
        Visit visit = findVisit(visitId);
        visit.setDescription(reason);
        changeState(VisitStatus.CANCELED, () -> visit);
    }

    @Override
    public Visit register(@NonNull VisitRegistrationDto visitRegistration) {
        logger.info("Registering {}", visitRegistration);
        if(visitRegistration.getDate().isBefore(LocalDateTime.now())) {
            logger.info("Cannot register visit for date={}", visitRegistration.getDate());
            throw new BadRequestException("Visit", "Date", visitRegistration.getDate().toString(),
                    "date can't be from the past");
        }

        Visit visit = wrapToEntity(() -> visitMapper.toEntity(visitRegistration));
        logger.info("Saving {}", visit);
        return visitRepository.save(visit);
    }

    @Override
    public void end(@NonNull Long visitId, @NonNull VisitClosureDto visitClosure) {
        logger.info("Ending visit with id={}", visitId);
        Visit visit = findVisit(visitId);
        changeState(VisitStatus.FINISHED, () -> visitMapper.toEntity(visitClosure, visit));

        if(visitClosure.getPhysicalExaminations() != null && !visitClosure.getPhysicalExaminations().isEmpty())
            examinationService.createPhysicalExaminations(visitClosure.getPhysicalExaminations());

        if(visitClosure.getLaboratoryExaminations() != null && !visitClosure.getLaboratoryExaminations().isEmpty())
            examinationService.createLaboratoryExaminations(visitClosure.getLaboratoryExaminations());

    }

    @Override
    public Visit get(@NonNull Long visitId) {
        return findVisit(visitId);
    }

    @Override
    public VisitWithExaminationsDto getFatVisit(@NonNull Long visitId) {
        Visit visit = findVisit(visitId);

        return new VisitWithExaminationsDto(visit, examinationService.getAllLaboratoryExaminationsByVisit(visit.getId()),
                examinationService.getAllPhysicalExaminationsByVisit(visit.getId()));
    }

    @Override
    public List<Visit> getAll() {
        return visitRepository.findAll();
    }

    @Override
    public List<VisitWithExaminationsDto> getAllFatVisits() {

        return visitRepository.findAll().stream()
                .map(visit -> new VisitWithExaminationsDto(visit,
                        examinationService.getAllLaboratoryExaminationsByVisit(visit.getId()),
                        examinationService.getAllPhysicalExaminationsByVisit(visit.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitWithExaminationsDto> getAllFatVisitsByDoctor(Long doctorId) {
        return visitRepository.findAllByDoctor_Id(doctorId).stream()
                .map(visit -> new VisitWithExaminationsDto(visit,
                        examinationService.getAllLaboratoryExaminationsByVisit(visit.getId()),
                        examinationService.getAllPhysicalExaminationsByVisit(visit.getId())))
                .collect(Collectors.toList());
    }

    private Visit changeState(VisitStatus newState, Supplier<Visit> toEntity) {
        Visit visit = wrapToEntity(toEntity);

        if(visit.getStatus() != newState) {
            if(visit.getStatus() != VisitStatus.REGISTERED) {
                logger.error("Cannot change state from {} to {}", visit.getStatus(), newState);
                throw new BadRequestException("Visit", "status", visit.getStatus().toString(),
                        "should equal " + VisitStatus.REGISTERED.toString());
            }
            visit.setStatus(newState);
            logger.info("Saving {}", visit);
            visitRepository.save(visit);
        }

        return visit;
    }

    private Visit wrapToEntity(Supplier<Visit> toEntity) {
        try {
            return toEntity.get();
        } catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    private Visit findVisit(Long id) {
        return visitRepository.findById(id).orElseThrow(()
                -> new ResourceDoesNotExistException("Visit", "id", id.toString()));
    }
}
