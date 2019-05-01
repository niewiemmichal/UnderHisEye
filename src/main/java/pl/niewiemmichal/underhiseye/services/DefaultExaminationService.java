package pl.niewiemmichal.underhiseye.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.niewiemmichal.underhiseye.commons.dto.AssistantClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.LaboratoryExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.PhysicalExaminationDto;
import pl.niewiemmichal.underhiseye.commons.dto.SupervisorClosureDto;
import pl.niewiemmichal.underhiseye.commons.dto.mappers.ExaminationMapper;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamination;
import pl.niewiemmichal.underhiseye.entities.PhysicalExamination;
import pl.niewiemmichal.underhiseye.entities.LaboratoryExamStatus;
import pl.niewiemmichal.underhiseye.commons.exceptions.*;
import pl.niewiemmichal.underhiseye.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultExaminationService implements ExaminationService {


    private final ExaminationRepository examinationRepository;
    private final LaboratoryExaminationRepository laboratoryExaminationRepository;
    private final PhysicalExaminationRepository physicalExaminationRepository;
    private final LaboratoryAssistantRepository laboratoryAssistantRepository;
    private final LaboratorySupervisorRepository laboratorySupervisorRepository;
    private final VisitRepository visitRepository;
    private final ExaminationMapper examinationMapper;

    @Autowired
    public DefaultExaminationService(ExaminationRepository examinationRepository,
                                     LaboratoryExaminationRepository laboratoryExaminationRepository,
                                     PhysicalExaminationRepository physicalExaminationRepository,
                                     LaboratoryAssistantRepository laboratoryAssistantRepository,
                                     LaboratorySupervisorRepository laboratorySupervisorRepository,
                                     VisitRepository visitRepository,
                                     ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.laboratoryExaminationRepository = laboratoryExaminationRepository;
        this.physicalExaminationRepository = physicalExaminationRepository;
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
        this.examinationMapper = examinationMapper;
        this.visitRepository = visitRepository;
    }

    @Override
    public List<PhysicalExamination> createPhysicalExaminations(@NonNull List<PhysicalExaminationDto> physicalExaminations) {

        List<PhysicalExamination> physicalExaminationList = new ArrayList<>();

        try {
            physicalExaminationList = physicalExaminations.stream().map(examinationMapper::toEntity).collect(Collectors.toList());

        }catch (ResourceDoesNotExistException e)
        {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }

        physicalExaminationRepository.saveAll(physicalExaminationList);
        return physicalExaminationList;
    }

    @Override
    public List<LaboratoryExamination> createLaboratoryExaminations(@NonNull List<LaboratoryExaminationDto> laboratoryExaminations) {

        List<LaboratoryExamination> laboratoryExaminationList = new ArrayList<>();

        try {
            laboratoryExaminationList = laboratoryExaminations.stream().map(examinationMapper::toEntity).collect(Collectors.toList());
        }catch (ResourceDoesNotExistException e)
        {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }

        laboratoryExaminationRepository.saveAll(laboratoryExaminationList);
        return laboratoryExaminationList;
    }

    @Override
    public LaboratoryExamination finish(@NonNull Long id, @NonNull AssistantClosureDto assistantClosureDto) {

        LaboratoryExamination laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                -> new BadRequestException("LaboratoryExamination", "id", id.toString(), " does not exist"));

        try{
            laboratoryExamination = examinationMapper.toEntity(assistantClosureDto, laboratoryExamination);
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }

        if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.FINISHED) {
            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.ORDERED)
                throw new BadRequestException("LaboratoryExamination", "status",
                        laboratoryExamination.getStatus().toString(), " should equal ORDERED");
            laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
            laboratoryExaminationRepository.save(laboratoryExamination);
        }
        return laboratoryExamination;
    }

    @Override
    public LaboratoryExamination cancel(@NonNull Long id, @NonNull AssistantClosureDto assistantClosureDto)
    {
        LaboratoryExamination laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                -> new BadRequestException("LaboratoryExamination", "id", id.toString(), " does not exist"));

        try{
            laboratoryExamination = examinationMapper.toEntity(assistantClosureDto, laboratoryExamination);
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }

        if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.CANCELED) {
            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.ORDERED)
                throw new BadRequestException("LaboratoryExamination", "status",
                        laboratoryExamination.getStatus().toString(), " should equal ORDERED");
            laboratoryExamination.setStatus(LaboratoryExamStatus.CANCELED);
            laboratoryExaminationRepository.save(laboratoryExamination);
        }
        return laboratoryExamination;
    }

    @Override
    public LaboratoryExamination reject(@NonNull Long id, @NonNull SupervisorClosureDto supervisorClosureDto) {

        LaboratoryExamination laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                -> new BadRequestException("LaboratoryExamination", "id", id.toString(), " does not exist"));

        try{
            laboratoryExamination = examinationMapper.toEntity(supervisorClosureDto, laboratoryExamination);
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }

        if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.REJECTED) {
            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.FINISHED)
                throw new BadRequestException("LaboratoryExamination", "status",
                        laboratoryExamination.getStatus().toString(), "should equal FINISHED");
            laboratoryExamination.setStatus(LaboratoryExamStatus.REJECTED);
            laboratoryExaminationRepository.save(laboratoryExamination);
        }
        return laboratoryExamination;
    }

    @Override
    public LaboratoryExamination approve(@NonNull Long id,@NonNull Long supervisorId) {

        LaboratoryExamination laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                -> new BadRequestException("LaboratoryExamination", "id", id.toString(), " does not exist"));

        try{
            laboratoryExamination = examinationMapper.toEntity(supervisorId, laboratoryExamination);
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }

        if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.APPROVED) {

            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.FINISHED)
                throw new BadRequestException("LaboratoryExamination", "status",
                        laboratoryExamination.getStatus().toString(), "should equal FINISHED");

            laboratoryExamination.setStatus(LaboratoryExamStatus.APPROVED);
            laboratoryExaminationRepository.save(laboratoryExamination);
        }
        return laboratoryExamination;
    }

    @Override
    public List<LaboratoryExamination> getAllLaboratoryExaminationsByVisit(@NonNull Long visitId)
    {
        return laboratoryExaminationRepository.findAllByVisit_Id(visitId);
    }

    @Override
    public List<PhysicalExamination> getAllPhysicalExaminationsByVisit(@NonNull Long visitId)
    {
        return physicalExaminationRepository.findAllByVisit_Id(visitId);
    }
}
