package code.nextly.stockdata.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Data
public class AlphaVantageQueryTimeSeriesResponse {

  private Map<String, AlphaVantageQueryDailyPriceResponse> dailyPrices = new HashMap<>();

  @JsonAnySetter
  public void addDailyPrice(String date, AlphaVantageQueryDailyPriceResponse dailyPrice) {
    this.dailyPrices.put(date, dailyPrice);
  }
}
