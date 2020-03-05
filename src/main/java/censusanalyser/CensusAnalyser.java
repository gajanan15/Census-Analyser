package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<CensusCSVDTO> censusList=null;
    Map<String, CensusCSVDTO> censusMap=null;
    List<CensusCSVDTO> collectList=null;

    public CensusAnalyser() {
        this.censusList=new ArrayList<>();
        this.censusMap=new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));){
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = openCsvBuilder.getCsvFileIterator(reader,IndiaCensusCSV.class);
            while (censusCSVIterator.hasNext()){
               //this.censusList.add(new IndiaCensusCSVDTO(censusCSVIterator.next()));
               IndiaCensusCSV indiaCensusCSV = censusCSVIterator.next();
               this.censusMap.put(indiaCensusCSV.state,new CensusCSVDTO(indiaCensusCSV));
            }
            collectList = censusMap.values().stream().collect(Collectors.toList());
            return collectList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(String stateCsvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(stateCsvFilePath));) {
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            Iterator<IndianStateCodeCsv> stateCodeIterator = openCsvBuilder.getCsvFileIterator(reader,IndianStateCodeCsv.class);
            Iterable<IndianStateCodeCsv> csv = ()-> stateCodeIterator;
            StreamSupport.stream(csv.spliterator(),false)
                    .filter(indianStateCodeCsv -> censusMap.get(indianStateCodeCsv.stateName)!=null)
                    .forEach(indianStateCodeCsv -> censusMap.get(indianStateCodeCsv.stateName).stateCode=indianStateCodeCsv.stateCode);
            return censusMap.size();
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
        if(censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus=new Gson().toJson(collectList);
        return sortedStateCensus;
    }

    private void sort (Comparator<CensusCSVDTO> censusComparator) {
        for(int i=0;i<collectList.size()-1;i++){
            for(int j=0;j<collectList.size()-i-1;j++){
                CensusCSVDTO census1 = collectList.get(j);
                CensusCSVDTO census2 = collectList.get(j+1);
                if(censusComparator.compare(census1,census2)>0){
                    collectList.set(j,census2);
                    collectList.set(j+1,census1);
                }
            }
        }
    }

    public String getStateWisePopulationCensusData() {
        if(collectList == null || collectList.size() == 0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.population);
        this.sort(censusComparator);
        String sortedStateCensus=new Gson().toJson(collectList);
        return sortedStateCensus;
    }

    public String getStateWisePopulationDensityCensusData() {
        if(collectList == null || collectList.size() == 0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.populationDensity);
        this.sort(censusComparator);
        //Collections.reverse(collectList);
        String sortedStateCensus=new Gson().toJson(collectList);
        return sortedStateCensus;
    }

    public int loadUsCensusData(String usCsvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(usCsvFilePath));){
            IcsvBuilder openCsvBuilder = CsvBuilderFactory.getOpenCsvBuilder();
            Iterator<UsCensusCSV> censusCSVIterator = openCsvBuilder.getCsvFileIterator(reader, UsCensusCSV.class);
            while (censusCSVIterator.hasNext()){
                UsCensusCSV usCensusCSV = censusCSVIterator.next();
                this.censusMap.put(usCensusCSV.state,new CensusCSVDTO(usCensusCSV));
            }
            collectList = censusMap.values().stream().collect(Collectors.toList());

            return collectList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

}
