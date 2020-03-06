package censusanalyser;

public class CensusCSVDTO {
    public String state;
    public String stateCode;
    public int population;
    public double totalArea;
    public double populationDensity;
    public CensusCSVDTO(IndiaCensusCSV indiaCensusCSV) {
         state = indiaCensusCSV.state;
         population = indiaCensusCSV.population;
        totalArea = indiaCensusCSV.totalArea;
        populationDensity = indiaCensusCSV.populationDensity;
    }


    public CensusCSVDTO(UsCensusCSV censusCSV) {
        state = censusCSV.state;
        stateCode = censusCSV.stateId;
        population=censusCSV.population;
        totalArea = censusCSV.totalArea;
        populationDensity=censusCSV.populationDensity;
    }
}
