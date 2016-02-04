package org.bahmni.module.bahmnicore.web.v1_0.search;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VisitFormsSearchHandlerTest {

    private VisitFormsSearchHandler visitFormsSearchHandler;
    @Mock
    RequestContext requestContext;

    @Before
    public void before() {
        initMocks(this);
        visitFormsSearchHandler = new VisitFormsSearchHandler();
    }
    @Test
    public void testGetSearchConfig() throws Exception {
        SearchConfig searchConfig = visitFormsSearchHandler.getSearchConfig();
        assertThat(searchConfig.getId(), is(equalTo("byPatientUuid")));

    }

    @Test
    public void shouldSupportVersions1_10To1_12() {
        SearchConfig searchConfig = visitFormsSearchHandler.getSearchConfig();
        assertTrue(searchConfig.getSupportedOpenmrsVersions().contains("1.10.*"));
        assertTrue(searchConfig.getSupportedOpenmrsVersions().contains("1.11.*"));
        assertTrue(searchConfig.getSupportedOpenmrsVersions().contains("1.12.*"));
    }

    @Test
    public void testSearch() throws Exception {

        when(requestContext.getParameter("patient")).thenReturn("patientUuid");
        when(requestContext.getParameter("numberOfVisits")).thenReturn("10");
        when(requestContext.getParameter("conceptNames")).thenReturn("Vitals");



    }
}