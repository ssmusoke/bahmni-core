package org.bahmni.module.bahmnicore.web.v1_0.controller;

import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramAttributeData;
import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramData;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.ProgramAttributeType;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.bahmni.module.bahmnicore.web.v1_0.mapper.BahmniPatientProgramDataToPatientProgramMapper;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/bahmnicore/bahmniprogramenrollment")
public class BahmniProgramEnrollmentController extends BaseRestController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private BahmniPatientProgramDataToPatientProgramMapper mapper;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BahmniPatientProgramData save(@RequestBody BahmniPatientProgramData bahmniPatientProgramData) {
        validateInput(bahmniPatientProgramData);
        Patient patientByUuid = patientService.getPatientByUuid(bahmniPatientProgramData.getPatient());
        Program programByUuid = Context.getService(BahmniProgramWorkflowService.class).getProgramByUuid(bahmniPatientProgramData.getProgram());
        HashSet<PatientProgramAttribute> patientProgramAttributes = new HashSet<>();
        for (BahmniPatientProgramAttributeData attributeData : bahmniPatientProgramData.getAttributes()) {
            ProgramAttributeType programAttributeType =Context.getService(BahmniProgramWorkflowService.class).getProgramAttributeTypeByUuid(attributeData.getAttributeType().getUuid());
            PatientProgramAttribute attribute = new PatientProgramAttribute();
            attribute.setAttributeType(programAttributeType);
            patientProgramAttributes.add(attribute);
        }
        BahmniPatientProgram patientProgram = mapper.map(bahmniPatientProgramData, patientByUuid, programByUuid, new HashSet<PatientProgramAttribute>());
        Context.getService(BahmniProgramWorkflowService.class).savePatientProgram(patientProgram);
        return bahmniPatientProgramData;
    }

    private void validateInput(BahmniPatientProgramData bahmniPatientProgramData) {
        if (bahmniPatientProgramData.getPatient() == null) {
            throw new IllegalArgumentException("Patient should not be null");
        }

        if (bahmniPatientProgramData.getProgram() == null) {
            throw new IllegalArgumentException("Program should not be null");
        }
    }
}
