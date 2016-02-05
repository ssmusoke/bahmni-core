package org.bahmni.module.bahmnicore.web.v1_0.mapper;

import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramData;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.openmrs.Patient;
import org.openmrs.Program;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BahmniPatientProgramDataToPatientProgramMapper {
    public BahmniPatientProgram map(BahmniPatientProgramData patientProgramToSave, Patient patient, Program program, Set<PatientProgramAttribute> patientProgramAttributes) {
        BahmniPatientProgram patientProgram = new BahmniPatientProgram();
        patientProgram.setPatient(patient);
        patientProgram.setProgram(program);
        patientProgram.setDateEnrolled(patientProgramToSave.getDateEnrolled());
        patientProgram.setAttributes(patientProgramAttributes);
        return patientProgram;
    }
}
