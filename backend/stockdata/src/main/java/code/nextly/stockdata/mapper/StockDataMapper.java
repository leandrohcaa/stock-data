package code.nextly.stockdata.mapper;

import code.nextly.stockdata.dto.AlphaVantageQueryDailyPriceResponse;
import code.nextly.stockdata.dto.AlphaVantageQueryResponse;
import code.nextly.stockdata.dto.StockDataResponse;
import code.nextly.stockdata.model.StockData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StockDataMapper {

  StockDataMapper INSTANCE = Mappers.getMapper(StockDataMapper.class);

  StockDataResponse toStockDataResponse(StockData stockData);

  @Mapping(target = "date", source = "entry.key")
  @Mapping(target = "highPrice", source = "entry.value.high", qualifiedByName = "stringToBigDecimal")
  @Mapping(target = "lowPrice", source = "entry.value.low", qualifiedByName = "stringToBigDecimal")
  @Mapping(target = "openPrice", source = "entry.value.open", qualifiedByName = "stringToBigDecimal")
  @Mapping(target = "closePrice", source = "entry.value.close", qualifiedByName = "stringToBigDecimal")
  @Mapping(target = "volume", source = "entry.value.volume", qualifiedByName = "stringToLong")
  @Mapping(target = "symbol", source = "symbol")
  @Mapping(target = "id", ignore = true)
  StockData toStockData(Map.Entry<String, AlphaVantageQueryDailyPriceResponse> entry, String symbol);

  default List<StockData> mapToStockDataList(AlphaVantageQueryResponse response,
                                             LocalDate startDate, LocalDate endDate) {
    return response.getTimeSeriesDaily().getDailyPrices().entrySet().stream()
        .map(entry -> toStockData(entry, response.getMetaData().getSymbol()))
        .filter(sd -> sd.getDate().compareTo(startDate) >= 0 && sd.getDate().compareTo(endDate) < 0)
        .sorted(Comparator.comparing(StockData::getDate).reversed())
        .collect(Collectors.toList());
  }

  @Named("stringToBigDecimal")
  default BigDecimal stringToBigDecimal(String value) {
    return new BigDecimal(value);
  }

  @Named("stringToLong")
  default Long stringToLong(String value) {
    return Long.valueOf(value);
  }
}