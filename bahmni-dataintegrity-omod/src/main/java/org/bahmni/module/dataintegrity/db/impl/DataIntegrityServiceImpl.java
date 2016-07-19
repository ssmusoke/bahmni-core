package org.bahmni.module.dataintegrity.db.impl;

import org.bahmni.module.dataintegrity.db.DataIntegrityResult;
import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.db.DataIntegrityService;

import java.util.List;

public class DataIntegrityServiceImpl implements DataIntegrityService {

    // read all rules from dataintegrity_rule table
    @Override
    public List<DataIntegrityRule> getRules() {
        return null;
    }

    // save all rules from dataintegrity_rule table
    @Override
    public void saveResults(List<DataIntegrityResult> results) {

    }
}
