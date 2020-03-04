package censusanalyser;

public class CsvBuilderException extends RuntimeException {
    enum ExceptionType {
        UNABLE_TO_PARSE;
    }

    ExceptionType type;

    public CsvBuilderException(ExceptionType type, String message) {
        super(message);
        this.type = type;
    }
}
