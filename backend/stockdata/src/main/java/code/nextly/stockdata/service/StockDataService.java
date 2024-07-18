package code.nextly.stockdata.service;

import code.nextly.stockdata.dto.StockDataResponse;
import code.nextly.stockdata.exceptions.StockDataNotFoundException;
import code.nextly.stockdata.mapper.StockDataMapper;
import code.nextly.stockdata.model.StockData;
import code.nextly.stockdata.repository.DbCreateRepository;
import code.nextly.stockdata.repository.StockDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockDataService {

  private final StockDataRepository stockDataRepository;
  private final DbCreateRepository dbCreateRepository;
  private final StockDataMapper stockDataMapper;
  public StockDataService(StockDataRepository stockDataRepository,
                          DbCreateRepository dbCreateRepository,
                          StockDataMapper stockDataMapper) {
    this.stockDataRepository = stockDataRepository;
    this.dbCreateRepository = dbCreateRepository;
    this.stockDataMapper = stockDataMapper;
  }

  public StockDataResponse getStockDataBySymbolAndDate(String symbol, LocalDate date) {
    StockData stockData = stockDataRepository.findSingleBySymbolAndDate(symbol, date)
        .orElseThrow(() -> new StockDataNotFoundException("Stock data not found."));
    return stockDataMapper.toStockDataResponse(stockData);
  }

  @Transactional
  public void deleteAndSaveStockData(List<StockData> stockDataBySymbolList) {
    String symbol = stockDataBySymbolList.stream().findFirst().get().getSymbol();
    stockDataRepository.deleteBySymbol(symbol);
    stockDataRepository.saveAll(stockDataBySymbolList);
  }

  @Transactional
  public void createDatabaseAndSchemasIfNotExists() {
    dbCreateRepository.executeDbCreateScript();
  }

  public List<LocalDate> getFirstExistingWeekDates() {
    List<LocalDate> dates = stockDataRepository.findAllDates();
    List<LocalDate> firstDatesOfWeeks = new ArrayList<>();

    LocalDate currentWeekStart = null;
    for (LocalDate date : dates) {
      LocalDate weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
      if (currentWeekStart == null || !weekStart.equals(currentWeekStart)) {
        firstDatesOfWeeks.add(date);
        currentWeekStart = weekStart;
      }
    }
    return firstDatesOfWeeks;
  }

  public List<String> getAllSymbols() {
    return stockDataRepository.findAllSymbols();
  }
}