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

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DefaultVisitService implements VisitService
{
    private final VisitRepository visitRepository;
    private final ExaminationService examinationService;
    private final VisitMapper visitMapper;

    private static final Logger LOG = LoggerFactory.getLogger(DefaultVisitService.class);

    @Autowired
    public DefaultVisitService(final ExaminationService examinationService, final VisitRepository visitRepository,
                               final VisitMapper visitMapper) {
        this.visitRepository = visitRepository;
        this.examinationService = examinationService;
        this.visitMapper = visitMapper;
    }

    @Override
    public void cancel(@NonNull Long visitId, @NonNull String reason) {
        LOG.info("Canceling visit with id={}", visitId);
        Visit visit = findVisit(visitId);
        visit.setDescription(reason);
        changeState(VisitStatus.CANCELED, () -> visit);
    }

    @Override
    public Visit register(@NonNull VisitRegistrationDto visitRegistration) {
        LOG.info("Registering {}", visitRegistration);
        if(visitRegistration.getDate().isBefore(LocalDate.now())) {
            LOG.info("Cannot register visit for date={}", visitRegistration.getDate());
            throw new BadRequestException("Visit", "Date", visitRegistration.getDate().toString(),
                    "date can't be from the past");
        }

        Visit visit = wrapToEntity(() -> visitMapper.toEntity(visitRegistration));
        LOG.info("Saving {}", visit);
        return visitRepository.save(visit);
    }

    @Override
    public void end(@NonNull Long visitId, @NonNull VisitClosureDto visitClosure) {
        LOG.info("Ending visit with id={}", visitId);
        Visit visit = findVisit(visitId);
        changeState(VisitStatus.FINISHED, () -> visitMapper.toEntity(visitClosure, visit));

        if(visitClosure.getPhysicalExaminations() != null && !visitClosure.getPhysicalExaminations().isEmpty())
            examinationService.createPhysicalExaminations(visitClosure.getPhysicalExaminations());

        if(visitClosure.getLaboratoryExaminations() != null && !visitClosure.getLaboratoryExaminations().isEmpty())
            examinationService.createLaboratoryExaminations(visitClosure.getLaboratoryExaminations());

    }

    @Override
    public Visit get(@NonNull Long visitId) {
        return visitRepository.findById(visitId).orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id",
                visitId.toString()));
    }

    @Override
    public VisitWithExaminationsDto getFatVisit(@NonNull Long visitId) {
        Visit visit = findVisit(visitId);

        return new VisitWithExaminationsDto(visit, examinationService.getAllLaboratoryExaminationsByVisit(visitId),
                examinationService.getAllPhysicalExaminationsByVisit(visitId));
    }

    @Override
    public List<Visit> getAll() {
        return visitRepository.findAll();
    }

    private Visit changeState(VisitStatus newState, Supplier<Visit> toEntity) {
        Visit visit = wrapToEntity(toEntity);

        if(visit.getStatus() != newState) {
            if(visit.getStatus() != VisitStatus.REGISTERED) {
                LOG.error("Cannot change state from {} to {}", visit.getStatus(), newState);
                throw new BadRequestException("Visit", "status", visit.getStatus().toString(),
                        "should equal " + VisitStatus.REGISTERED.toString());
            }
            visit.setStatus(newState);
            LOG.info("Saving {}", visit);
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
