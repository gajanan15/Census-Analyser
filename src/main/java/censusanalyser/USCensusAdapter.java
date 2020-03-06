package censusanalyser;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter {
    public Map<String, CensusCSVDTO> loadCensusData(String[] csvFilePath) {
        Map<String,CensusCSVDTO> censusStateMap = super.loadCensusData(UsCensusCSV.class,csvFilePath[0]);
        return censusStateMap;
    }
}
