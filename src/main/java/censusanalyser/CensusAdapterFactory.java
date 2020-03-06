package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory {
    public static Map<String, CensusCSVDTO> getCensusData(CensusAnalyser.Country country, String... csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndianCensusAdapter().loadCensusData(csvFilePath);
        else if(country.equals(CensusAnalyser.Country.US))
            return new USCensusAdapter().loadCensusData(csvFilePath);
        else
            throw new CensusAnalyserException("Incorrect Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}
