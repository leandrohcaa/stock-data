package code.nextly.stockdata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlphaVantageQueryResponse {

  @JsonProperty("Meta Data")
  private AlphaVantageQueryMetaDataResponse metaData;

  @JsonProperty("Time Series (Daily)")
  private AlphaVantageQueryTimeSeriesResponse timeSeriesDaily;
}