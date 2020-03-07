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
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getSortedCensusData(SortedField.STATE);
            IndiaCensusCSV[] censusCSVS = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSVS[0].state);
        }
        catch (CensusAnalyserException e){}
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulation_ShouldReturnLeastPopulationState() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
            String populationCensusData = censusAnalyser.getReverseCensusData(SortedField.POPULATION);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(populationCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals(199812341,censusCSV[0].population);
        }catch (CensusAnalyserException e){

        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnDensity_ShouldReturnLeastPopulationState() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
            String populationCensusData = censusAnalyser.getReverseCensusData(SortedField.POPULATIONDENSITY);
            IndiaCensusCSV[] indiacensusCSV = new Gson().fromJson(populationCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals(1102,indiacensusCSV[0].populationDensity);
        }catch (CensusAnalyserException e){

        }
    }

    @Test
    public void givenIndianCensusData_WhereSortedOnStateCode_ShouldReturnStateCode() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData(SortedField.STATECODE);
        IndianStateCodeCsv[] censusCSVS = new Gson().fromJson(sortedCensusData, IndianStateCodeCsv[].class);
        Assert.assertEquals("AP",censusCSVS[0].stateCode);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnTotalArea_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getReverseCensusData(SortedField.TOTALAREA);
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals(342239, censusCSV[0].totalArea);

    }

    @Test
    public void givenUsCensusCSVFile_ShouldReturnsCorrectRecords() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int record = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        Assert.assertEquals(51,record);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulation_ShouldReturnMostPopulationState() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String usMostPopulatedState = censusAnalyser.getSortedCensusData(SortedField.STATE);
            UsCensusCSV[] usCensusCSVS = new Gson().fromJson(usMostPopulatedState, UsCensusCSV[].class);
            Assert.assertEquals("Alabama", usCensusCSVS[0].state);
        }catch (CensusAnalyserException e){}
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getReverseCensusData(SortedField.POPULATION);
        UsCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, UsCensusCSV[].class);
        Assert.assertEquals(37253956, censusCSV[0].population);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulationDensity_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getReverseCensusData(SortedField.POPULATIONDENSITY);
        System.out.println(sortedCensusData);
        UsCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, UsCensusCSV[].class);
        Assert.assertEquals(3805.61, censusCSV[0].populationDensity, 0.00);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnTotalArea_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getReverseCensusData(SortedField.TOTALAREA);
        System.out.println(sortedCensusData);
        UsCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, UsCensusCSV[].class);
        Assert.assertEquals(1723338.01, censusCSV[0].totalArea, 0.0);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnStateCode_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData = censusAnalyser.getSortedCensusData(SortedField.STATECODE);
        UsCensusCSV[] censusCSVS = new Gson().fromJson(sortedCensusData, UsCensusCSV[].class);
        Assert.assertEquals("AK",censusCSVS[0].stateId);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulationDensity_ShouldReturnMostPopulationState() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getReverseCensusData(SortedField.POPULATIONDENSITY);
            UsCensusCSV[] usCensusCSV = new Gson().fromJson(sortedCensusData, UsCensusCSV[].class);
            Assert.assertEquals("District of Columbia",usCensusCSV[0].state);
        }catch (CensusAnalyserException e){}
    }

    @Test
    public void givenUSCensusData_WhenSortedOnArea_ShouldReturnMostPopulationState() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String reverseCensusData = censusAnalyser.getReverseCensusData(SortedField.TOTALAREA);
            UsCensusCSV[] usCensusCSVS = new Gson().fromJson(reverseCensusData, UsCensusCSV[].class);
            Assert.assertEquals("Alaska",usCensusCSVS[0].state);
        }catch (CensusAnalyserException e){}
    }

    @Test
    public void gievnCensusData_WhenSortedUSAndIndiaOnPopulation_SholudReturnPopulationState() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH,US_CENSUS_CSV_FILE_PATH);
        String indiaSortedData = censusAnalyser.getSortedCensusData(SortedField.POPULATION);
        IndiaCensusCSV[] censusCSVS = new Gson().fromJson(indiaSortedData, IndiaCensusCSV[].class);

        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        String usCensusData = censusAnalyser.getSortedCensusData(SortedField.POPULATION);
        UsCensusCSV[] usCensusCSV = new Gson().fromJson(usCensusData, UsCensusCSV[].class);

        Assert.assertEquals(true,(String.valueOf(usCensusCSV[0].population).compareToIgnoreCase(String.valueOf(censusCSVS[0].population))<0));
    }
}
