package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) {
        Iterator<IndiaCensusCSV> censusCSVIterator;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            List<IndiaCensusCSV> censusCSV = openCsvBuilder.getCsvFileList(reader,IndiaCensusCSV.class);
            return censusCSV.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(String stateCsvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvFilePath));) {
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            Iterator<IndianStateCodeCsv> stateCodeIterator = openCsvBuilder.getCsvFileIterator(reader,IndianStateCodeCsv.class);
            return getCount(stateCodeIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

    }
    private <E>int getCount(Iterator<E> stateCodeIterator) {
        Iterable<E> stateCodeCsvIterable = () -> stateCodeIterator;
        int numOfEnteries = (int) StreamSupport.stream(stateCodeCsvIterable.spliterator(), false).count();
        return numOfEnteries;
    }
}
