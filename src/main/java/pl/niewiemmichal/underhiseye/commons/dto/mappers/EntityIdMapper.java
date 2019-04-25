package pl.niewiemmichal.underhiseye.commons.dto.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.niewiemmichal.underhiseye.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.underhiseye.entities.*;
import pl.niewiemmichal.underhiseye.repositories.*;

@Component
public class EntityIdMapper {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private RegistrantRepository registrantRepository;
    private VisitRepository visitRepository;
    private ExaminationRepository examinationRepository;
    private LaboratorySupervisorRepository laboratorySupervisorRepository;
    private LaboratoryAssistantRepository laboratoryAssistantRepository;

    @Autowired
    public EntityIdMapper(PatientRepository patientRepository,
                          DoctorRepository doctorRepository,
                          RegistrantRepository registrantRepository,
                          VisitRepository visitRepository,
                          ExaminationRepository examinationRepository,
                          LaboratorySupervisorRepository laboratorySupervisorRepository,
                          LaboratoryAssistantRepository laboratoryAssistantRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.registrantRepository = registrantRepository;
        this.visitRepository = visitRepository;
        this.examinationRepository = examinationRepository;
        this.laboratorySupervisorRepository = laboratorySupervisorRepository;
        this.laboratoryAssistantRepository = laboratoryAssistantRepository;
    }

    Patient toPatient(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Patient", "id", id.toString()));
    }

    Doctor toDoctor(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Doctor", "id", id.toString()));
    }

    Registrant toRegistrant(Long id) {
        return registrantRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Registrant", "id", id.toString()));
    }

    Visit toVisit(Long id) {
        return visitRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Visit", "id", id.toString()));
    }

    Examination toExamination(String code) {
        return examinationRepository.findById(code).orElseThrow(() -> new ResourceDoesNotExistException("Examination", "code", code));
    }

    LaboratoryAssistant toAssistant(Long id) {
        return laboratoryAssistantRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Assistant", "id", id.toString()));
    }

    LaboratorySupervisor toSupervisor(Long id) {
        return laboratorySupervisorRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Supervisor", "id", id.toString()));
    }

}
