package org.openmrs.module.bahmniemrapi.builder;

import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Visit;

import java.util.Date;
import java.util.HashSet;

public class VisitBuilder {

    private final Visit visit;

    public VisitBuilder() {
        visit = new Visit();
    }

    public VisitBuilder withPerson(Person person) {
        visit.setPatient(new Patient(person));
        return this;
    }

    public VisitBuilder withUUID(String uuid) {
        visit.setUuid(uuid);
        return this;
    }

    public VisitBuilder withStartDatetime(Date startDatetime) {
        visit.setStartDatetime(startDatetime);
        return this;
    }

    public VisitBuilder withLocation(Location location) {
        visit.setLocation(location);
        return this;
    }

    public Visit build() {
        return visit;
    }

    public VisitBuilder withEncounter(Encounter encounter) {
        HashSet<Encounter> encounters = new HashSet<>();
        encounters.add(encounter);
        visit.setEncounters(encounters);
        return this;
    }
}
