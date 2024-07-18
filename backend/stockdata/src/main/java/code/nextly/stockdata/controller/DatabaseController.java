package code.nextly.stockdata.controller;

import code.nextly.stockdata.service.StockDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/database")
public class DatabaseController {

  private final StockDataService stockDataService;
  public DatabaseController(StockDataService stockDataService) {
    this.stockDataService = stockDataService;
  }

  @PostMapping("/db_create")
  public void createDatabase() {
    stockDataService.createDatabaseAndSchemasIfNotExists();
  }

}