package org.bahmni.module.bahmnicore.contract.patient.search;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PatientProgramAttributeQueryHelper {
	protected String patientProgramAttributeValue;
	protected Integer programAttributeTypeId;

	public PatientProgramAttributeQueryHelper(String patientProgramAttributeValue, Integer programAttributeTypeId) {
		this.patientProgramAttributeValue = patientProgramAttributeValue;
		this.programAttributeTypeId = programAttributeTypeId;
	}

	public String selectClause(String select){
		return select + ", " +
				"concat('{',group_concat(DISTINCT (coalesce(concat('\"',ppt.name,'\":\"', ppa.value_reference,'\"'))) SEPARATOR ','),'}') AS patientProgramAttributeValue";
	}

	public String appendToJoinClause(String join){
		return join + " left outer join patient_program pp on pat.patient_id = pp.patient_id and pp.voided=0"
				+ " left outer join patient_program_attribute ppa on pp.patient_program_id = ppa.patient_program_id and ppa.voided=0"
				+ " left outer join program_attribute_type ppt on ppa.attribute_type_id = ppt.program_attribute_type_id and ppa.attribute_type_id ="+programAttributeTypeId.intValue();
	}

	public String appendToWhereClause(String where){
		if(StringUtils.isEmpty(patientProgramAttributeValue)){
			return where;
		}

		return combine(where, "and", enclose(" ppa.value_reference like "+ "'%" + StringEscapeUtils.escapeSql(patientProgramAttributeValue) + "%' and ppa.attribute_type_id =" + programAttributeTypeId.intValue()));
	}

	public Map<String,Type> addScalarQueryResult(){
		Map<String,Type> scalarQueryResult = new HashMap<>();
		scalarQueryResult.put("patientProgramAttributeValue", StandardBasicTypes.STRING);
		return scalarQueryResult;
	}

	protected String combine(String query, String operator, String condition) {
		return String.format("%s %s %s", query, operator, condition);
	}

	protected String enclose(String value) {
		return String.format("(%s)", value);
	}

	public String appendToGroupByClause(String fieldName) {
		String groupByClause = "p.person_id, p.uuid , pi.identifier , pn.given_name , pn.middle_name , pn.family_name , p.gender , p.birthdate , p.death_date , p.date_created , v.uuid";
		return isEmpty(fieldName)?fieldName+groupByClause: fieldName+","+ groupByClause;
	}

}
