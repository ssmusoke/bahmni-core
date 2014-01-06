package org.bahmni.module.bahmnicore.service;

import org.openmrs.Concept;
import org.openmrs.Obs;

import java.util.List;

public interface BahmniPersonObsService {
    public List<Obs> getObsForPerson(String identifier);

    public List<Concept> getNumericConceptsForPerson(String personUUID);
}