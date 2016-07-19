package org.bahmni.module.dataintegrity.db;

import java.util.List;
import java.util.Map;

public interface DataintegrityDAO {
    List<String> getAllByObsAndDrugs(List<String> drugsList, Map<String, List<String>> codedObs);
}
