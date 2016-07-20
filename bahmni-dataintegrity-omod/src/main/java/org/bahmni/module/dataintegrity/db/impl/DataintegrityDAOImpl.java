package org.bahmni.module.dataintegrity.db.impl;

import org.bahmni.module.dataintegrity.db.DataintegrityDAO;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.PatientProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataintegrityDAOImpl implements DataintegrityDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<RuleResult<PatientProgram>> getAllByObsAndDrugs(List<String> drugsList, Map<String, List<String>> codedObs) {

        StringBuilder queryString = new StringBuilder("SELECT epp.patient_program_id, ");

        queryString.append(codedObs != null && codedObs.size() > 0 ? "obs1.comments AS notes" : "NULL AS notes");

        queryString.append(
                "FROM" +
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
            for( Map.Entry<String, List<String>> obsConcept : codedObs.entrySet())
                queryString.append(getObsQuery(obsConcept));

        queryString.append(" GROUP BY epp.patient_program_id");
        queryString.append(" limit " + 10);
        Query queryToGetObs = sessionFactory.getCurrentSession().createSQLQuery(queryString.toString());

        if(drugsList!=null && drugsList.size() > 0)
            queryToGetObs.setParameterList("drugs", drugsList);

        List ruleOutput = queryToGetObs.list();
        List<String> newList = new ArrayList<>(ruleOutput.size());
        for (Object id : ruleOutput) newList.add(id.toString());

        return null;
    }

    private String getObsQuery(Map.Entry<String, List<String>> obsConcept) {
        String concpetValues = StringUtils.join(obsConcept.getValue(), "','");
        return "   INNER JOIN (" +
        "   SELECT  o.comments, o.encounter_id" +
        "   FROM    obs o" +
        "           JOIN concept_view cv ON     o.concept_id = cv.concept_id AND o.voided = 0 AND" +
        "                                       cv.concept_full_name = '" + obsConcept.getKey() + "'" +
        "           JOIN concept_view cv_value ON   o.value_coded = cv_value.concept_id  AND " +
        "                                           cv_value.concept_full_name IN ('" + concpetValues + "') " +
        "   ) obs1 ON obs1.encounter_id = e.encounter_id"; //TODO: modify for multiple obs
    }
}
