package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_CENSUS_CSV_FILE_PATH="./src/test/resources/USCensusData.csv";


    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] censusCSVS = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSVS[0].state);
        }catch (CensusAnalyserException e){}
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPolulation_ShouldReturnLeastPopulationState() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String populationCensusData = censusAnalyser.getStateWisePopulationCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(populationCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals(607688,censusCSV[0].population);
        }catch (CensusAnalyserException e){

        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnDensity_ShouldReturnLeastPopulationState() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String populationCensusData = censusAnalyser.getStateWisePopulationDensityCensusData();
            IndiaCensusCSV[] indiacensusCSV = new Gson().fromJson(populationCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals(50,indiacensusCSV[0].populationDensity);
        }catch (CensusAnalyserException e){

        }
    }

    @Test
    public void givenUsCensusCSVFile_ShouldReturnsCorrectRecords() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int record = censusAnalyser.loadUsCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        Assert.assertEquals(51,record);
    }
}
