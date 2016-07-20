package org.bahmni.module.dataintegrity.db;


import java.util.List;

public interface DataIntegrityService {

    List<DataintegrityRule> getRules();

    void saveResults(List<DataintegrityResult> results);
}
