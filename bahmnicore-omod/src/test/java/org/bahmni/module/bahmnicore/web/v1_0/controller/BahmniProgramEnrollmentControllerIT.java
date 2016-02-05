package org.bahmni.module.bahmnicore.web.v1_0.controller;

import org.bahmni.module.bahmnicore.contract.program.BahmniPatientProgramData;
import org.bahmni.module.bahmnicore.web.v1_0.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertNotNull;

public class BahmniProgramEnrollmentControllerIT extends BaseIntegrationTest {

    @Before
    public void setUp() throws Exception {
        executeDataSet("programEnrollmentDataSet.xml");
    }

    @Test
    public void shouldEnrollProgram() throws Exception {
        String dataJson = "{\"patient\":\"75e04d42-3ca8-11e3-bf2b-0808633c1b75\",\"program\":\"d7477c21-bfc3-4922-9591-e89d8b9c8efa\",\"dateEnrolled\":\"2015-12-31T18:30:00.000Z\", \"attributes\":[]}";
        MockHttpServletResponse response = handle(newPostRequest("/rest/v1/bahmnicore/bahmniprogramenrollment", dataJson));
        BahmniPatientProgramData patientProgram = deserialize(response, BahmniPatientProgramData.class);

        assertNotNull(patientProgram);
    }
}