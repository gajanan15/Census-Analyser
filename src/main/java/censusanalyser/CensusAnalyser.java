package censusanalyser;

import com.google.gson.Gson;
import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    public enum Country{INDIA,US};

    List<CensusCSVDTO> censusList = null;
    Map<String, CensusCSVDTO> censusMap = null;
    List<CensusCSVDTO> collectList = null;

    CensusLoader censusLoader = new CensusLoader();

    public CensusAnalyser() {
        this.censusList = new ArrayList<>();
        this.censusMap = new HashMap<>();
    }

    public int loadCensusData(Country country,String... csvFilePath) {
        censusMap = censusLoader.loadCensusData(country, csvFilePath);
        return censusMap.size();
    }

    public int loadUsCensusData(Country country,String... usCsvFilePath) {
        censusMap = censusLoader.loadCensusData(country, usCsvFilePath);
        return censusMap.size();
    }

    public String getStateWiseSortedCensusData() {
        collectList = censusMap.values().stream().collect(Collectors.toList());
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(collectList);
        return sortedStateCensus;
    }

    private void sort(Comparator<CensusCSVDTO> censusComparator) {
        for (int i = 0; i < collectList.size() - 1; i++) {
            for (int j = 0; j < collectList.size() - i - 1; j++) {
                CensusCSVDTO census1 = collectList.get(j);
                CensusCSVDTO census2 = collectList.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    collectList.set(j, census2);
                    collectList.set(j + 1, census1);
                }
            }
        }
    }

    public String getStateWisePopulationCensusData() {
        if (collectList == null || collectList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.population);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(collectList);
        return sortedStateCensus;
    }

    public String getStateWisePopulationDensityCensusData() {
        if (collectList == null || collectList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.populationDensity);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(collectList);
        return sortedStateCensus;
    }
}
