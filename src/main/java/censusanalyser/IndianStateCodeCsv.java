package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndianStateCodeCsv {
    @CsvBindByName(column = "SrNo",required = true)
    public String srNo;

    @CsvBindByName(column = "State Name",required = true)
    public String stateName;

    @CsvBindByName(column = "TIN",required = true)
    public String tIN;

    @CsvBindByName(column = "StateCode",required = true)
    public String stateCode;
}
