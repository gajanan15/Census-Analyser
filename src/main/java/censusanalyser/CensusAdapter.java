package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class CensusAdapter extends RuntimeException {
    public abstract Map<String, CensusCSVDTO> loadCensusData(String... csvFilePath);
        public <E> Map<String,CensusCSVDTO> loadCensusData(Class<E> censusCsvClass, String csvFilePath) {
        Map<String,CensusCSVDTO> censusMap = new HashMap<>();
            try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
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
                    return censusMap;
            } catch (IOException e) {
                throw new CensusAnalyserException(e.getMessage(),
                        CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
            }

        }

    }
