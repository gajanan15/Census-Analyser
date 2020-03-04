package censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface IcsvBuilder {
    public <E> Iterator<E> getCsvFileIterator(Reader reader, Class<E> csvClass);
    public <E> List<E> getCsvFileList(Reader reader, Class<E> csvClass);
}
