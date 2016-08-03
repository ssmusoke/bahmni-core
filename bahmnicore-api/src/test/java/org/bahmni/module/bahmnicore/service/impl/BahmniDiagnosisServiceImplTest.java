package org.bahmni.module.bahmnicore.service.impl;

import org.bahmni.test.builder.ConceptBuilder;
import org.bahmni.test.builder.DiagnosisBuilder;
import org.bahmni.test.builder.EncounterBuilder;
import org.bahmni.test.builder.ObsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.openmrs.*;
import org.openmrs.api.*;
import org.openmrs.module.bahmniemrapi.diagnosis.contract.BahmniDiagnosis;
import org.openmrs.module.bahmniemrapi.diagnosis.contract.BahmniDiagnosisRequest;
import org.openmrs.module.bahmniemrapi.diagnosis.helper.BahmniDiagnosisMetadata;
import org.openmrs.module.emrapi.diagnosis.Diagnosis;
import org.openmrs.module.emrapi.diagnosis.DiagnosisService;
import org.openmrs.module.emrapi.encounter.DiagnosisMapper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.util.LocaleUtility;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@PrepareForTest(LocaleUtility.class)
@RunWith(PowerMockRunner.class)
public class BahmniDiagnosisServiceImplTest {
    @Mock
    private EncounterService encounterService;
    @Mock
    private ObsService obsService;
    @Mock
    private PatientService patientService;
    @Mock
    private VisitService visitService;
    @Mock
    private BahmniDiagnosisMetadata bahmniDiagnosisMetadata;
    @Mock
    private ConceptService conceptService;
    @Mock
    private DiagnosisMapper diagnosisMapper;
    @Mock
    private DiagnosisService diagnosisService;

    @InjectMocks
    private BahmniDiagnosisServiceImpl bahmniDiagnosisService = new BahmniDiagnosisServiceImpl(encounterService, obsService, visitService, patientService, diagnosisMapper, diagnosisService, bahmniDiagnosisMetadata, conceptService);

    private String initialDiagnosisObsUUID = "initialDiagnosisObsUUID";
    private String modifiedDiagnosisObsUUID = "modifiedDiagnosisObsUUID";
    private String initialEncounterUUID = "initialEncounterUUID";
    private Obs initialVisitDiagnosesObs;
    private Obs modifiedVisitDiagnosis;
    private Encounter initialEncounter;
    private Encounter modifiedEncounter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(LocaleUtility.class);
        PowerMockito.when(LocaleUtility.getLocalesInOrder()).thenReturn(new HashSet<>(Arrays.asList(Locale.getDefault())));
    }

    @Test
    public void deleteADiagnosis() throws Exception {
        String diagnosisObsUUID = "diagnosisObsUUID";

        Obs visitDiagnosisObs = new DiagnosisBuilder().withUuid(diagnosisObsUUID).withDefaults().withFirstObs(diagnosisObsUUID).build();
        Set<Obs> allObsForDiagnosisEncounter = new HashSet<>();
        allObsForDiagnosisEncounter.add(new DiagnosisBuilder().withUuid("someOtherDiagnosisUUID").withDefaults().withFirstObs("initialDiagnosisObsUUID").build());
        allObsForDiagnosisEncounter.add(visitDiagnosisObs);
        allObsForDiagnosisEncounter.add(new ObsBuilder().withUUID("nonDiagnosisUuid").withConcept("Some Concept", Locale.getDefault()).build());

        Encounter diagnosisEncounter = new EncounterBuilder().withDatetime(new Date()).build();
        visitDiagnosisObs.setEncounter(diagnosisEncounter);
        diagnosisEncounter.setObs(allObsForDiagnosisEncounter);

        when(obsService.getObsByUuid(diagnosisObsUUID)).thenReturn(visitDiagnosisObs);
        when(obsService.getObservationsByPersonAndConcept(visitDiagnosisObs.getPerson(), visitDiagnosisObs.getConcept())).thenReturn(Arrays.asList(visitDiagnosisObs));
        when(encounterService.saveEncounter(diagnosisEncounter)).thenReturn(diagnosisEncounter);
        when(bahmniDiagnosisMetadata.findInitialDiagnosisUuid(visitDiagnosisObs)).thenReturn(diagnosisObsUUID);

        bahmniDiagnosisService.delete(diagnosisObsUUID);
        ArgumentCaptor<Encounter> argToCapture = ArgumentCaptor.forClass(Encounter.class);
        verify(encounterService).saveEncounter(argToCapture.capture());
        assertVoided(argToCapture.getValue(), diagnosisObsUUID);
    }

    @Test
    public void initialDiagnosisIsDeletedOnDeletingADiagnosis() throws Exception {
        setUpInitialVisitDiagnosis();
        setUpModifiedVisitDiagnosis();

        when(obsService.getObsByUuid(modifiedDiagnosisObsUUID)).thenReturn(modifiedVisitDiagnosis);
        when(obsService.getObservationsByPersonAndConcept(modifiedVisitDiagnosis.getPerson(), modifiedVisitDiagnosis.getConcept())).
                thenReturn(Arrays.asList(modifiedVisitDiagnosis, initialVisitDiagnosesObs));
        when(encounterService.saveEncounter(initialEncounter)).thenReturn(initialEncounter);
        when(encounterService.saveEncounter(modifiedEncounter)).thenReturn(modifiedEncounter);
        when(bahmniDiagnosisMetadata.findInitialDiagnosisUuid(modifiedVisitDiagnosis)).thenReturn(initialDiagnosisObsUUID);

        bahmniDiagnosisService.delete(modifiedDiagnosisObsUUID);

        ArgumentCaptor<Encounter> argToCapture = ArgumentCaptor.forClass(Encounter.class);
        verify(encounterService, times(2)).saveEncounter(argToCapture.capture());

        assertVoided(argToCapture.getAllValues().get(0), modifiedDiagnosisObsUUID);
        assertVoided(argToCapture.getAllValues().get(1), initialDiagnosisObsUUID);
    }

    @Test
    public void otherDiagnosisWithSameInitialDiagnosisIsDeletedOnDeletingADiagnosis() throws Exception {
        setUpInitialVisitDiagnosis();
        setUpModifiedVisitDiagnosis();
        String anotherDiagnosisUuid = "anotherDiagnosisUuid";


        Obs anotherVisitDiagnosis = new DiagnosisBuilder().withUuid(anotherDiagnosisUuid).withDefaults().withFirstObs(initialDiagnosisObsUUID).build();
        Encounter anotherEncounter = new EncounterBuilder().withDatetime(new Date()).withUUID("anotherEncounterUuid").build();
        anotherEncounter.addObs(anotherVisitDiagnosis);
        anotherVisitDiagnosis.setEncounter(anotherEncounter);

        when(obsService.getObsByUuid(modifiedDiagnosisObsUUID)).thenReturn(modifiedVisitDiagnosis);
        when(obsService.getObservationsByPersonAndConcept(modifiedVisitDiagnosis.getPerson(), modifiedVisitDiagnosis.getConcept())).
                thenReturn(Arrays.asList(modifiedVisitDiagnosis, initialVisitDiagnosesObs, anotherVisitDiagnosis));
        when(encounterService.saveEncounter(initialEncounter)).thenReturn(initialEncounter);
        when(encounterService.saveEncounter(modifiedEncounter)).thenReturn(modifiedEncounter);
        when(bahmniDiagnosisMetadata.findInitialDiagnosisUuid(modifiedVisitDiagnosis)).thenReturn(initialDiagnosisObsUUID);

        bahmniDiagnosisService.delete(modifiedDiagnosisObsUUID);

        ArgumentCaptor<Encounter> argToCapture = ArgumentCaptor.forClass(Encounter.class);
        verify(encounterService, times(3)).saveEncounter(argToCapture.capture());

        assertVoided(argToCapture.getAllValues().get(0), modifiedDiagnosisObsUUID);
        assertVoided(argToCapture.getAllValues().get(1), initialDiagnosisObsUUID);
        assertVoided(argToCapture.getAllValues().get(2), anotherDiagnosisUuid);
    }

    @Test
    public void shouldGetBahmniDiagnosisByPatientAndVisit() {
        Patient patient = mock(Patient.class);
        Visit visit = new Visit();
        visit.addEncounter(new Encounter());
        Concept diagnosisSetConcept = new ConceptBuilder().withUUID("uuid").build();

        BahmniDiagnosisRequest bahmniDiagnosisRequest = new BahmniDiagnosisRequest();
        BahmniDiagnosis bahmniDiagnosis = new BahmniDiagnosis();
        bahmniDiagnosis.setExistingObs("existing");
        bahmniDiagnosisRequest.setFirstDiagnosis(bahmniDiagnosis);

        Diagnosis diagnosis = getDiagnosis();
        Diagnosis updatedDiagnosis = getUpdatedDiagnosis();

        when(visitService.getVisitByUuid("visitId")).thenReturn(visit);
        when(patientService.getPatientByUuid("patientId")).thenReturn(patient);
        when(bahmniDiagnosisMetadata.getDiagnosisSetConcept()).thenReturn(diagnosisSetConcept);
        when(obsService.getObservations(eq(Arrays.asList((Person) patient)), eq(new ArrayList<>(visit.getEncounters())), eq(Arrays.asList(diagnosisSetConcept)), anyListOf(Concept.class), anyList(), anyList(), anyList(),
                anyInt(), anyInt(), Matchers.any(Date.class), Matchers.any(Date.class), eq(false)))
                .thenReturn(Arrays.asList(diagnosis.getExistingObs()));
        when(bahmniDiagnosisMetadata.buildDiagnosisFromObsGroup(diagnosis.getExistingObs())).thenReturn(diagnosis);
        when(diagnosisMapper.convert(any(Diagnosis.class))).thenReturn(null);
        when(bahmniDiagnosisMetadata.findInitialDiagnosisUuid(diagnosis.getExistingObs())).thenReturn("firstDiagnosisObsId");
        when(bahmniDiagnosisMetadata.findInitialDiagnosis(updatedDiagnosis.getExistingObs())).thenReturn(diagnosis.getExistingObs());
        when(bahmniDiagnosisMetadata.mapBahmniDiagnosis(any(EncounterTransaction.Diagnosis.class), any(EncounterTransaction.Diagnosis.class), eq(true), eq(false))).thenReturn(bahmniDiagnosisRequest);

        List<BahmniDiagnosisRequest> bahmniDiagnosisRequests = bahmniDiagnosisService.getBahmniDiagnosisByPatientAndVisit("patientId", "visitId");

        assertEquals(1, bahmniDiagnosisRequests.size());
        assertEquals(bahmniDiagnosisRequest, bahmniDiagnosisRequests.get(0));
    }


    @Test
    public void ShouldNotReturnDiagnosisIfNoEncounterExists() throws Exception {

        String visitId = "visitId";
        Visit visit = new Visit();
        when(visitService.getVisitByUuid(visitId)).thenReturn(visit);

        List<BahmniDiagnosisRequest> bahmniDiagnosisRequests = bahmniDiagnosisService.getBahmniDiagnosisByPatientAndVisit("patientId", visitId);

        assertEquals(0, bahmniDiagnosisRequests.size());
    }

    @Test
    public void shouldReturnEmptyListIfNoVisitFound() throws Exception {
        String visitId = "visitId";
        when(visitService.getVisitByUuid(visitId)).thenReturn(null);

        List<BahmniDiagnosisRequest> bahmniDiagnosisRequests = bahmniDiagnosisService.getBahmniDiagnosisByPatientAndVisit("patientId", visitId);

        assertEquals(0, bahmniDiagnosisRequests.size());
    }

    private Diagnosis getDiagnosis() {
        Diagnosis diagnosis = new Diagnosis();
        Obs diagnosisObs = new DiagnosisBuilder()
                .withDefaults()
                .withFirstObs("firstDiagnosisObsId")
                .withUuid("firstDiagnosisObsId")
                .build();
        diagnosis.setExistingObs(diagnosisObs);

        return diagnosis;
    }

    private Diagnosis getUpdatedDiagnosis() {
        Diagnosis diagnosis = new Diagnosis();

        Obs updatedDiagnosisObs = new DiagnosisBuilder()
                .withDefaults()
                .withFirstObs("firstDiagnosisObsId")
                .withUuid("finalDiagnosisUuid")
                .build();

        diagnosis.setExistingObs(updatedDiagnosisObs);
        return diagnosis;
    }


    private void setUpModifiedVisitDiagnosis() {
        modifiedVisitDiagnosis = new DiagnosisBuilder().withUuid(modifiedDiagnosisObsUUID).withDefaults().withFirstObs(initialDiagnosisObsUUID).build();
        modifiedEncounter = new EncounterBuilder().withDatetime(new Date()).withUUID("modifiedEncounterUUID").build();
        modifiedEncounter.addObs(modifiedVisitDiagnosis);
        modifiedVisitDiagnosis.setEncounter(modifiedEncounter);
    }

    private void setUpInitialVisitDiagnosis() {
        initialVisitDiagnosesObs = new DiagnosisBuilder().withUuid(initialDiagnosisObsUUID).withDefaults().withFirstObs(initialDiagnosisObsUUID).build();
        initialEncounter = new EncounterBuilder().withDatetime(new Date()).withUUID(initialEncounterUUID).build();
        initialEncounter.addObs(initialVisitDiagnosesObs);
        initialVisitDiagnosesObs.setEncounter(initialEncounter);
    }


    private void assertVoided(Encounter encounter, String observationUuid) {
        Obs visitDiagnosesObsToSave = getAllObsFor(encounter, observationUuid);
        assertTrue("Parent Diagnosis Obs should be voided", visitDiagnosesObsToSave.isVoided());
        for (Obs childObs : visitDiagnosesObsToSave.getGroupMembers(true)) {
            assertTrue("Child Diagnosis Obs should be voided", childObs.isVoided());
        }
    }

    private Obs getAllObsFor(Encounter encounterToSave, String visitDiagnosisUuid) {
        Set<Obs> allObs = encounterToSave.getAllObs(true);
        for (Obs anObs : allObs) {
            if (anObs.getUuid().equals(visitDiagnosisUuid))
                return anObs;
        }
        return null;
    }
}