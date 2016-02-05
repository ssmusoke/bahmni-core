package org.bahmni.module.bahmnicore.web.v1_0.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.*;
import org.openmrs.api.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(Context.class)
@RunWith(PowerMockRunner.class)

public class VisitFormsSearchHandlerTest {

    private VisitFormsSearchHandler visitFormsSearchHandler;
    @Mock
    RequestContext context;
    @Mock
    PatientService patientService;
    @Mock
    ConceptService conceptService;
    @Mock
    EncounterService encounterService;
    @Mock
    VisitService visitService;
    @Mock
    ObsService obsService;

    @Mock
    Encounter encounter;
    Patient patient;
    Concept concept;
    Visit visit;
    Obs obs;


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

    public void shouldReturnConceptSpecificObsIfConceptNameIsSpecified() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(context.getLimit()).thenReturn(1);
        when(context.getRequest()).thenReturn(req);
        when(context.getRequest().getParameter("patient")).thenReturn("patientUuid");
        when(context.getRequest().getParameter("numberOfVisits")).thenReturn("10");
        String[] conceptNames={"Vitals"};
        when(context.getRequest().getParameterValues("conceptNames")).thenReturn(conceptNames);

        patient = new Patient();
        patient.setId(1);
        patient.setUuid("patient-uuid");
        PowerMockito.mockStatic(Context.class);
        PowerMockito.when(Context.getPatientService()).thenReturn(patientService);
        when(patientService.getPatientByUuid("patientUuid")).thenReturn(patient);

        PowerMockito.when(Context.getConceptService()).thenReturn(conceptService);
        concept = new Concept();
        concept.setFullySpecifiedName(new ConceptName("Vitals", new Locale("English")));

        PowerMockito.when(conceptService.getConcept("All Observation Templates")).thenReturn(concept);
        PowerMockito.when(conceptService.getConceptByName("Vitals")).thenReturn(concept);

        visit = new Visit();
        PowerMockito.when(Context.getVisitService()).thenReturn(visitService);
        PowerMockito.when(Context.getVisitService().getVisitsByPatient(patient)).thenReturn(Arrays.asList(visit));

        PowerMockito.when(Context.getEncounterService()).thenReturn(encounterService);
        encounter = mock(Encounter.class);
        PowerMockito.when(encounterService.getEncounters(any(Patient.class), any(Location.class), any(Date.class), any(Date.class), any(Collection.class), any(Collection.class), any(Collection.class), any(Collection.class), any(Collection.class), eq(false))).thenReturn(Arrays.asList(encounter));

        PowerMockito.when(Context.getObsService()).thenReturn(obsService);
        obs=new Obs();
        obs.setConcept(concept);

        PowerMockito.when(obsService.getObservations(any(List.class), any(List.class), any(List.class), any(List.class), any(List.class), any(List.class), any(List.class), any(Integer.class), any(Integer.class), any(Date.class), any(Date.class), eq(false))).thenReturn(Arrays.asList(obs));
        NeedsPaging<Obs> searchResults = (NeedsPaging<Obs>) visitFormsSearchHandler.search(context);
        assertThat(searchResults.getPageOfResults().size(), is(equalTo(1)));
    }
    @Test
    public void shouldReturnAllObsIfConceptNameIsNotSpecified() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(context.getLimit()).thenReturn(2);
        when(context.getRequest()).thenReturn(req);
        when(context.getRequest().getParameter("patient")).thenReturn("patientUuid");
        when(context.getRequest().getParameter("numberOfVisits")).thenReturn("10");
        when(context.getRequest().getParameterValues("conceptNames")).thenReturn(null);

        patient = new Patient();
        patient.setId(1);
        patient.setUuid("patient-uuid");
        PowerMockito.mockStatic(Context.class);
        PowerMockito.when(Context.getPatientService()).thenReturn(patientService);
        when(patientService.getPatientByUuid("patientUuid")).thenReturn(patient);

        PowerMockito.when(Context.getConceptService()).thenReturn(conceptService);
        Concept parentConcept = new Concept();
        concept = new Concept();
        concept.setFullySpecifiedName(new ConceptName("Vitals", new Locale("English")));
        parentConcept.addSetMember(concept);
        Concept historyConcept = new Concept();
        historyConcept.setFullySpecifiedName(new ConceptName("History and Examination", new Locale("English")));
        parentConcept.addSetMember(historyConcept);

        PowerMockito.when(conceptService.getConcept("All Observation Templates")).thenReturn(parentConcept);
        PowerMockito.when(conceptService.getConceptByName("Vitals")).thenReturn(concept);

        visit = new Visit();
        PowerMockito.when(Context.getVisitService()).thenReturn(visitService);
        PowerMockito.when(Context.getVisitService().getVisitsByPatient(patient)).thenReturn(Arrays.asList(visit));

        PowerMockito.when(Context.getEncounterService()).thenReturn(encounterService);
        encounter = mock(Encounter.class);
        PowerMockito.when(encounterService.getEncounters(any(Patient.class), any(Location.class), any(Date.class), any(Date.class), any(Collection.class), any(Collection.class), any(Collection.class), any(Collection.class), any(Collection.class), eq(false))).thenReturn(Arrays.asList(encounter));

        PowerMockito.when(Context.getObsService()).thenReturn(obsService);
        obs=new Obs();
        obs.setConcept(concept);
        Obs obs2=new Obs();
        obs2.setConcept(historyConcept);

        PowerMockito.when(obsService.getObservations(any(List.class), any(List.class), any(List.class), any(List.class), any(List.class), any(List.class), any(List.class), any(Integer.class), any(Integer.class), any(Date.class), any(Date.class), eq(false))).thenReturn(Arrays.asList(obs,obs2));
        NeedsPaging<Obs> searchResults = (NeedsPaging<Obs>) visitFormsSearchHandler.search(context);
        assertThat(searchResults.getPageOfResults().size(), is(equalTo(2)));
    }
}