package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCsvBuilder implements IcsvBuilder {
    @Override
    public <E>Iterator<E> getCsvFileIterator(Reader reader, Class<E> csvClass) {
        try {
            return getCsvToBeanBuilder(reader,csvClass).iterator();
        }catch (CsvBuilderException e){
            throw new CsvBuilderException(CsvBuilderException.ExceptionType.UNABLE_TO_PARSE,e.getMessage());
        }
    }

    @Override
    public <E> List<E> getCsvFileList(Reader reader, Class<E> csvClass) {
        return  getCsvToBeanBuilder(reader,csvClass).parse();
    }

    private <E> CsvToBean<E> getCsvToBeanBuilder(Reader reader, Class<E> csvClass) {
        CsvToBean<E> csvToBean;
        Iterator<E> censusCSVIterator;
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<E>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        csvToBean = csvToBeanBuilder.build();
        return csvToBean;
    }
}

