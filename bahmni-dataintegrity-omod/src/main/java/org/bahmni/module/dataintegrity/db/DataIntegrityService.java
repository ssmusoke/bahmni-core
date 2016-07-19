package org.bahmni.module.dataintegrity.db;


import java.util.List;

public interface DataIntegrityService {

    List<DataIntegrityRule> getRules();

    void saveResults(List<DataIntegrityResult> results);
}
