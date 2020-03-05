package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {
    Map<String,CensusCSVDTO> censusMap = new HashMap<>();

    public Map<String, CensusCSVDTO> loadCensusData(CensusAnalyser.Country country, String[] csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return this.loadCensusData(IndiaCensusCSV.class,csvFilePath);
        else if(country.equals(CensusAnalyser.Country.US))
            return this.loadCensusData(UsCensusCSV.class,csvFilePath);
        else
            throw new CensusAnalyserException("Incorrect Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }


    private   <E> Map<String,CensusCSVDTO> loadCensusData(Class<E> censusCsvClass, String... csvFilePath) {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            Iterator<E> censusCSVIterator = openCsvBuilder.getCsvFileIterator(reader, censusCsvClass);
            Iterable<E> censusCSVIterable = () -> censusCSVIterator;
            if(censusCsvClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusCSVDTO(censusCSV)));
            } else if(censusCsvClass.getName().equals("censusanalyser.UsCensusCSV")) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .map(UsCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusCSVDTO(censusCSV)));
            }
            if (csvFilePath.length == 1)
                return censusMap;
            this.loadIndianStateCode(censusMap,csvFilePath[1]);
            return censusMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

    }
    public int loadIndianStateCode(Map<String, CensusCSVDTO> censusMap, String stateCsvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvFilePath));) {
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            Iterator<IndianStateCodeCsv> stateCodeIterator = openCsvBuilder.getCsvFileIterator(reader, IndianStateCodeCsv.class);
            Iterable<IndianStateCodeCsv> csv = () -> stateCodeIterator;
            StreamSupport.stream(csv.spliterator(), false)
                    .filter(indianStateCodeCsv -> censusMap.get(indianStateCodeCsv.stateName) != null)
                    .forEach(indianStateCodeCsv -> censusMap.get(indianStateCodeCsv.stateName).stateCode = indianStateCodeCsv.stateCode);
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

    }

}
