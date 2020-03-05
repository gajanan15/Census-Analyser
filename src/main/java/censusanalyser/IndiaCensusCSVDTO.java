package censusanalyser;

public class IndiaCensusCSVDTO {
    public String state;
    public String stateCode;
    public int population;
    public int areaInSqKm;
    public int densityPerSqKm;
    public IndiaCensusCSVDTO(IndiaCensusCSV indiaCensusCSV) {
         state = indiaCensusCSV.state;
         population = indiaCensusCSV.population;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqKm = indiaCensusCSV.densityPerSqKm;

    }
}
