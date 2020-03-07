package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class IndianCensusAdapter extends CensusAdapter {
    Map<String,CensusCSVDTO> censusMap = new HashMap<>();

    @Override
    public Map<String, CensusCSVDTO> loadCensusData(String... csvFilePath) {
        Map<String,CensusCSVDTO> censusStateMap = super.loadCensusData(IndiaCensusCSV.class,csvFilePath[0]);
        this.loadIndianStateCode(censusStateMap,csvFilePath[1]);
        return censusStateMap;
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
