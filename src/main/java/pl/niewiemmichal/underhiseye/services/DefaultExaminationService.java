package pl.niewiemmichal.underhiseye.services;

import com.google.common.collect.Lists;
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
import pl.niewiemmichal.underhiseye.entities.Visit;
import pl.niewiemmichal.underhiseye.repositories.*;

import java.util.List;

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
    public List<PhysicalExamination> createPhysicalExaminations(List<PhysicalExaminationDto> physicalExaminations) {
        if ( physicalExaminations == null ) {
            return null;
        }

        List<PhysicalExamination> physicalExaminationList = Lists.newArrayList();
        try {
            for(PhysicalExaminationDto physicalExaminationDto : physicalExaminations)
            {
                PhysicalExamination physicalExamination = examinationMapper.toEntity(physicalExaminationDto);
                physicalExaminationList.add(physicalExamination);
            }
            physicalExaminationRepository.saveAll(physicalExaminationList);
            return physicalExaminationList;
        }catch (ResourceDoesNotExistException e)
        {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    @Override
    public List<LaboratoryExamination> createLaboratoryExaminations(List<LaboratoryExaminationDto> laboratoryExaminations) {

        if ( laboratoryExaminations == null ) {
            return null;
        }

        List<LaboratoryExamination> laboratoryExaminationList = Lists.newArrayList();

        try {
        for(LaboratoryExaminationDto laboratoryExaminationDto : laboratoryExaminations)
        {

            LaboratoryExamination laboratoryExamination = examinationMapper.toEntity(laboratoryExaminationDto);
            laboratoryExaminationList.add(laboratoryExamination);
        }
        laboratoryExaminationRepository.saveAll(laboratoryExaminationList);
        return laboratoryExaminationList;
        }catch (ResourceDoesNotExistException e)
        {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    @Override
    public LaboratoryExamination finish(Long id, AssistantClosureDto assistantClosureDto) {

        LaboratoryExamination laboratoryExamination;
        try{
            laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                    -> new ResourceDoesNotExistException("LaboratoryExamination", "id", id.toString()));

            examinationMapper.toEntity(assistantClosureDto, laboratoryExamination);


            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.FINISHED) {

                if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.ORDERED)
                    throw new BadRequestException("LaboratoryExamination", "status",
                            laboratoryExamination.getStatus().toString(), " should equals ORDERED");

                laboratoryExamination.setStatus(LaboratoryExamStatus.FINISHED);
                laboratoryExaminationRepository.save(laboratoryExamination);
            }
            return laboratoryExamination;
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    @Override
    public LaboratoryExamination cancel(Long id, AssistantClosureDto assistantClosureDto)
    {
        LaboratoryExamination laboratoryExamination;
        try{
            laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                    -> new ResourceDoesNotExistException("LaboratoryExamination", "id", id.toString()));

            examinationMapper.toEntity(assistantClosureDto, laboratoryExamination);


            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.CANCELED) {

                if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.ORDERED)
                    throw new BadRequestException("LaboratoryExamination", "status",
                            laboratoryExamination.getStatus().toString(), " should equals ORDERED");

                laboratoryExamination.setStatus(LaboratoryExamStatus.CANCELED);
                laboratoryExaminationRepository.save(laboratoryExamination);
            }
            return laboratoryExamination;
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    @Override
    public LaboratoryExamination reject(Long id, SupervisorClosureDto supervisorClosureDto) {

        LaboratoryExamination laboratoryExamination;
        try{
            laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                    -> new ResourceDoesNotExistException("LaboratoryExamination", "id", id.toString()));

            examinationMapper.toEntity(supervisorClosureDto, laboratoryExamination);


            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.REJECTED) {

                if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.FINISHED)
                    throw new BadRequestException("LaboratoryExamination", "status",
                        laboratoryExamination.getStatus().toString(), "should equals FINISHED");

                laboratoryExamination.setStatus(LaboratoryExamStatus.REJECTED);
                laboratoryExaminationRepository.save(laboratoryExamination);
            }
            return laboratoryExamination;
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    @Override
    public LaboratoryExamination approve(Long id, Long supervisorId) {

        LaboratoryExamination laboratoryExamination;
        try{
            laboratoryExamination = laboratoryExaminationRepository.findById(id).orElseThrow(()
                    -> new ResourceDoesNotExistException("LaboratoryExamination", "id", id.toString()));

            examinationMapper.toEntity(supervisorId, laboratoryExamination);

            if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.APPROVED) {

                if(laboratoryExamination.getStatus()!=LaboratoryExamStatus.FINISHED)
                    throw new BadRequestException("LaboratoryExamination", "status",
                            laboratoryExamination.getStatus().toString(), "should equals FINISHED");

                laboratoryExamination.setStatus(LaboratoryExamStatus.APPROVED);
                laboratoryExaminationRepository.save(laboratoryExamination);
            }
            return laboratoryExamination;
        }catch (ResourceDoesNotExistException e) {
            throw new BadRequestException(e.getResource(), e.getField(), e.getValue(), " does not exist");
        }
    }

    @Override
    public List<LaboratoryExamination> getAllLaboratoryExaminationsByVisit(Long visitId)
    {
        return laboratoryExaminationRepository.findAllByVisit_Id(visitId);
    }

    @Override
    public List<PhysicalExamination> getAllPhysicalExaminationsByVisit(Long visitId)
    {
        return physicalExaminationRepository.findAllByVisit_Id(visitId);
    }
}
