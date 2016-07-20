package org.bahmni.module.dataintegrity.db.impl;

import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.bahmni.module.dataintegrity.db.DataIntegrityService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DataIntegrityServiceImpl implements DataIntegrityService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<DataintegrityRule> getRules() {
        return sessionFactory.getCurrentSession().createCriteria(DataintegrityRule.class).list();
    }

    @Override
    public void saveResults(List<DataintegrityResult> results) {
        sessionFactory.getCurrentSession().save(results);
    }
}
