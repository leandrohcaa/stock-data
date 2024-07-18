package code.nextly.stockdata.controller;

import code.nextly.stockdata.dto.StockDataResponse;
import code.nextly.stockdata.model.StockData;
import code.nextly.stockdata.service.StockDataService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-data")
public class StockDataController {

  private final StockDataService stockDataService;
  public StockDataController(StockDataService stockDataService) {
    this.stockDataService = stockDataService;
  }

  @GetMapping("/{symbol}/{date}")
  public StockDataResponse getStockData(@PathVariable String symbol, @PathVariable LocalDate date) {
    return stockDataService.getStockDataBySymbolAndDate(symbol, date);
  }

  @GetMapping("/weeks")
  public List<LocalDate> getWeeks() {
    return stockDataService.getFirstExistingWeekDates();
  }

  @GetMapping("/symbols")
  public List<String> getAllSymbols() {
    return stockDataService.getAllSymbols();
  }
}
