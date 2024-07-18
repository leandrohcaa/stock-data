package code.nextly.stockdata.controller;

import code.nextly.stockdata.dto.DataCollectionRequest;
import code.nextly.stockdata.service.DataCollectionService;
import code.nextly.stockdata.service.StockDataService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/data-collection")
public class DataCollectionController {

  private final DataCollectionService dataCollectionService;
  public DataCollectionController(DataCollectionService dataCollectionService) {
    this.dataCollectionService = dataCollectionService;
  }

  @PostMapping("/collect_data")
  public void collectData(@Valid @RequestBody DataCollectionRequest request) {
    dataCollectionService.collectStockData(request.getStartDate(), request.getSymbols());
  }
}