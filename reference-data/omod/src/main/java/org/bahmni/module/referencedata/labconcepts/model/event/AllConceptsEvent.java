package org.bahmni.module.referencedata.labconcepts.model.event;

import org.ict4h.atomfeed.server.service.Event;
import org.joda.time.DateTime;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.api.context.ServiceContext;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

public class AllConceptsEvent extends ConceptOperationEvent {
    String url;
    String category;
    String title;

    public AllConceptsEvent(String url, String category, String title) {
        this.url = url;
        this.category = category;
        this.title = title;
    }

    @Override
    public boolean isResourceConcept(Concept concept) {
        return true;
    }

    @Override
    public Event asAtomFeedEvent(Object[] arguments) throws URISyntaxException {
        Concept concept = (Concept) arguments[0];
        String url = String.format(this.url, concept.getUuid(), concept.getName().getName().replaceAll(" ", "+"));
        return new Event(UUID.randomUUID().toString(), title, DateTime.now(), url, url, category);
    }

}
