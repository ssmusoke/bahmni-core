package org.bahmni.module.bahmnicore.web.v1_0.controller;

import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramAttributeData;
import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramAttributeTypeData;
import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramData;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.ProgramAttributeType;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.bahmni.module.bahmnicore.web.v1_0.mapper.BahmniPatientProgramDataToPatientProgramMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class BahmniProgramEnrollmentControllerTest {
    @Mock
    private PatientService patientService;

    @Mock
    private BahmniProgramWorkflowService programWorkflowService;

    @Mock
    private BahmniPatientProgramDataToPatientProgramMapper mapper;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @InjectMocks
    private BahmniProgramEnrollmentController bahmniProgramEnrollmentController = new BahmniProgramEnrollmentController();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(Context.class);
        when(Context.getService(BahmniProgramWorkflowService.class)).thenReturn(programWorkflowService);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfThePatienIsNull() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Patient should not be null");

        bahmniProgramEnrollmentController.save(new BahmniPatientProgramData());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfTheProgramIsNull() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Program should not be null");

        BahmniPatientProgramData bahmniPatientProgramData = new BahmniPatientProgramData();
        bahmniPatientProgramData.setPatient("patient uuid");

        bahmniProgramEnrollmentController.save(bahmniPatientProgramData);
    }

    @Test
    public void shouldEnrollProgram() throws Exception {
        BahmniPatientProgramData patientProgramToSave = new BahmniPatientProgramData();
        String patientUuid = "patient uuid";
        String programUuid = "program uuid";
        String attributeTypeUuid = "attribute type uuid";
        patientProgramToSave.setPatient(patientUuid);
        patientProgramToSave.setProgram(programUuid);
        ArrayList<BahmniPatientProgramAttributeData> attributes = new ArrayList<>();
        attributes.add(new BahmniPatientProgramAttributeData(new BahmniPatientProgramAttributeTypeData(attributeTypeUuid), "300"));
        patientProgramToSave.setAttributes(attributes);
        Patient patient = new Patient();
        Program program = new Program();

        when(patientService.getPatientByUuid(patientUuid)).thenReturn(patient);
        when(programWorkflowService.getProgramByUuid(programUuid)).thenReturn(program);
        when(programWorkflowService.getProgramAttributeTypeByUuid(attributeTypeUuid)).thenReturn(new ProgramAttributeType());
        BahmniPatientProgram patientProgram = new BahmniPatientProgram();
        PatientProgram expectedProgram = new PatientProgram();
        when(mapper.map(patientProgramToSave, patient, program, new HashSet<PatientProgramAttribute>())).thenReturn(patientProgram);
        when(programWorkflowService.savePatientProgram(patientProgram)).thenReturn(expectedProgram);

        BahmniPatientProgramData actualProgram = bahmniProgramEnrollmentController.save(patientProgramToSave);

        verify(programWorkflowService, times(1)).savePatientProgram(patientProgram);
        verify(patientService, times(1)).getPatientByUuid(patientUuid);
        verify(programWorkflowService, times(1)).getProgramByUuid(programUuid);
        verify(programWorkflowService, times(1)).getProgramAttributeTypeByUuid(attributeTypeUuid);
        verify(mapper, times(1)).map(patientProgramToSave, patient, program, new HashSet<PatientProgramAttribute>());
    }
}