package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) {
        Iterator<IndiaCensusCSV> censusCSVIterator;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            censusCSVIterator = getCsvFileIterator(reader,IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> censusIterable = () -> censusCSVIterator;
            int namOfEateries = (int) StreamSupport.stream(censusIterable.spliterator(), false).count();
            return namOfEateries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(String stateCsvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvFilePath));) {
            Iterator<IndianStateCodeCsv> stateCodeIterator = getCsvFileIterator(reader,IndianStateCodeCsv.class);
            Iterable<IndianStateCodeCsv> stateCodeCsvIterable = () -> stateCodeIterator;
            int numOfEnteries = (int) StreamSupport.stream(stateCodeCsvIterable.spliterator(), false).count();
            return numOfEnteries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }

    }

    private <E>Iterator<E> getCsvFileIterator(Reader reader,Class csvClass) {
        CsvToBean<E> csvToBean;
        Iterator<E> censusCSVIterator;
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        csvToBean = csvToBeanBuilder.build();
        censusCSVIterator = csvToBean.iterator();
        return censusCSVIterator;
    }
}
