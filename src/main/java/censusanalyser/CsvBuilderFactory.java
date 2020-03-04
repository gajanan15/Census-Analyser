package censusanalyser;

public class CsvBuilderFactory {
    public static IcsvBuilder getOpenCsvBuilder() {
        return new OpenCsvBuilder();
    }
}
