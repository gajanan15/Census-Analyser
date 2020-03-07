package censusanalyser;

import com.google.gson.Gson;
import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

    public enum Country{INDIA,US};

    List<CensusCSVDTO> censusList = null;
    Map<String, CensusCSVDTO> censusMap = null;
    IndianCensusAdapter censusLoader = new IndianCensusAdapter();
    Map<SortedField,Comparator<CensusCSVDTO>> sortedMap = null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<>();
        this.censusMap = new HashMap<>();
        this.sortedMap = new HashMap<>();

        this.sortedMap.put(SortedField.STATE,Comparator.comparing(census -> census.state));
        this.sortedMap.put(SortedField.POPULATION,Comparator.comparing(census -> census.population));
        this.sortedMap.put(SortedField.POPULATIONDENSITY,Comparator.comparing(census -> census.populationDensity));
        this.sortedMap.put(SortedField.TOTALAREA,Comparator.comparing(census -> census.totalArea));
        this.sortedMap.put(SortedField.STATECODE,Comparator.comparing(census -> census.stateCode));
        this.sortedMap.put(SortedField.STATEID,Comparator.comparing(census -> census.stateId));
    }

    public int loadCensusData(Country country,String... csvFilePath) {
        censusMap = CensusAdapterFactory.getCensusData(country,csvFilePath);
        return censusMap.size();
    }

    public String getSortedCensusData(SortedField sortedField) {
        censusList = censusMap.values().stream().collect(Collectors.toList());
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        this.sort(censusList,this.sortedMap.get(sortedField));
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    public String getReverseCensusData(SortedField sortedField) {
        censusList = censusMap.values().stream().collect(Collectors.toList());
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        this.sort(censusList,this.sortedMap.get(sortedField));
        Collections.reverse(censusList);
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    private void sort(List<CensusCSVDTO> censusCSVDTOList,Comparator<CensusCSVDTO> censusComparator) {
        for (int i = 0; i < censusCSVDTOList.size() - 1; i++) {
            for (int j = 0; j < censusCSVDTOList.size() - i - 1; j++) {
                CensusCSVDTO census1 = censusCSVDTOList.get(j);
                CensusCSVDTO census2 = censusCSVDTOList.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    censusCSVDTOList.set(j, census2);
                    censusCSVDTOList.set(j + 1, census1);
                }
            }
        }
    }
}
