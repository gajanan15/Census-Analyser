package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<IndiaCensusCSV> censusCSVList=null;
    public int loadIndiaCensusData(String csvFilePath) {
        Iterator<IndiaCensusCSV> censusCSVIterator;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            censusCSVList = openCsvBuilder.getCsvFileList(reader,IndiaCensusCSV.class);
            return censusCSVList.size();
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

    public String getStateWiseSortedCensusData() {
        if(censusCSVList == null || censusCSVList.size() == 0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusCSVList,censusComparator);
        String sortedStateCensus=new Gson().toJson(censusCSVList);
        return sortedStateCensus;
    }

    private void sort (List<IndiaCensusCSV> censusCSVList,Comparator<IndiaCensusCSV> censusComparator) {
        for(int i=0;i<censusCSVList.size()-1;i++){
            for(int j=0;j<censusCSVList.size()-i-1;j++){
                IndiaCensusCSV census1 = censusCSVList.get(j);
                IndiaCensusCSV census2 = censusCSVList.get(j+1);
                if(censusComparator.compare(census1,census2)>0){
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }
    }
}
