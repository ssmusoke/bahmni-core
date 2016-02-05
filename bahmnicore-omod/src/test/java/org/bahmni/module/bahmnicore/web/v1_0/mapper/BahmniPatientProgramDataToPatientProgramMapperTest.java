package org.bahmni.module.bahmnicore.web.v1_0.mapper;

import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramData;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Program;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class BahmniPatientProgramDataToPatientProgramMapperTest {

    private BahmniPatientProgramDataToPatientProgramMapper mapper = new BahmniPatientProgramDataToPatientProgramMapper();

    @Test
    public void shouldMapBahmniPatientProgramDataToOpenMRSPatientProgram() throws Exception {
        String patientUuid = "patient uuid";
        String programUuid = "program uuid";
        Date dateEnrolled = new Date();
        BahmniPatientProgramData bahmniPatientProgramData = new BahmniPatientProgramData(patientUuid, programUuid, dateEnrolled, null);
        Patient patient = new Patient();
        patient.setUuid(patientUuid);
        Program program = new Program();
        program.setUuid(programUuid);
        Set<PatientProgramAttribute> attributes = new HashSet<>();
        attributes.add(new PatientProgramAttribute());

        BahmniPatientProgram patientProgram = mapper.map(bahmniPatientProgramData, patient, program, attributes);

        assertNotNull("Patient program should not be null", patientProgram);
        Assert.assertEquals(patient, patientProgram.getPatient());
        Assert.assertEquals(program, patientProgram.getProgram());
        Assert.assertEquals(dateEnrolled, patientProgram.getDateEnrolled());
        Assert.assertEquals(attributes, patientProgram.getAttributes());
    }
}