package org.bahmni.module.dataintegrity.db.impl;

import org.bahmni.module.dataintegrity.db.DataintegrityDAO;
import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.openmrs.PatientProgram;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

@Component("DataintegrityDAO")
public class DataintegrityDAOImpl implements DataintegrityDAO {

    private SessionFactory sessionFactory;

    @Override
    public List<RuleResult<PatientProgram>> getAllByObsAndDrugs(List<String> drugsList, Map<String, List<String>> codedObs) {

        StringBuilder queryString =
                new StringBuilder("SELECT epp.patient_program_id as entity");

        queryString.append((codedObs != null && codedObs.size() > 0 ? " , obs1.comments " : " NULL") + " AS notes ");
        //TODO: get action url
        queryString.append((codedObs != null && codedObs.size() > 0 ? " , obs1.comments " : " NULL ") + " AS actionUrl ");

        queryString.append(
                " FROM" +
                "    episode_patient_program epp" +
                "    JOIN episode_encounter ee ON ee.episode_id = epp.episode_id" );

        if(drugsList!=null && drugsList.size() > 0)
            queryString.append(
                "    JOIN orders     ON ee.encounter_id = orders.encounter_id AND orders.order_action != 'DISCONTINUE' AND orders.voided = 0" +
                "    JOIN drug_order ON orders.order_id = drug_order.order_id" +
                "    JOIN drug       ON drug_order.drug_inventory_id = drug.drug_id" +
                "    JOIN concept_name cn ON 	drug.concept_id = cn.concept_id AND cn.concept_name_type = 'FULLY_SPECIFIED' AND drug.retired = 0 AND" +
                "						        cn.name IN (:drugs)" );

        if(codedObs!=null)
            for( Entry<String, List<String>> obsConcept : codedObs.entrySet())
                queryString.append(getObsQuery(obsConcept));

        queryString.append(" GROUP BY epp.patient_program_id");
        queryString.append(" limit " + 10);
        Query queryToGetObs = sessionFactory.getCurrentSession().createSQLQuery(queryString.toString());

        if(drugsList!=null && drugsList.size() > 0)
            queryToGetObs.setParameterList("drugs", drugsList);

        return queryToGetObs.setResultTransformer(new AliasToBeanResultTransformer(RuleResult.class)).list();
    }

    private String getObsQuery(Entry<String, List<String>> obsConcept) {
        String concpetValues = StringUtils.join(obsConcept.getValue(), "','");
        return "   INNER JOIN (" +
        "   SELECT  o.comments, o.encounter_id, o.uuid " +
        "   FROM    obs o" +
        "           JOIN concept_view cv ON     o.concept_id = cv.concept_id AND o.voided = 0 AND" +
        "                                       cv.concept_full_name = '" + obsConcept.getKey() + "'" +
        "           JOIN concept_view cv_value ON   o.value_coded = cv_value.concept_id  AND " +
        "                                           cv_value.concept_full_name IN ('" + concpetValues + "') " +
        "   ) obs1 ON obs1.encounter_id = ee.encounter_id"; //TODO: modify for multiple obs
    }

    @Override
    public List<DataintegrityRule> getRules() {
        return sessionFactory.getCurrentSession().createCriteria(DataintegrityRule.class).list();
    }

    @Override
    public void saveResults(List<DataintegrityResult> results) {
        for(DataintegrityResult result: results) saveResult(result);
    }

    @Override
    public void clearAllResults(){
        final Session session = sessionFactory.openSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery("delete from dataintegrity_result where result_id > 0").executeUpdate();
                transaction.commit();
                session.flush();
            } catch (Exception ex) {
                transaction.rollback();
                throw ex;
            }

        } finally {
            session.close();
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private void saveResult(DataintegrityResult result) {
        final Session session = sessionFactory.openSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                session.save(result);
                transaction.commit();
                session.flush();
            } catch (Exception ex) {
                transaction.rollback();
                throw ex;
            }

        } finally {
            session.close();
        }
    }
}
