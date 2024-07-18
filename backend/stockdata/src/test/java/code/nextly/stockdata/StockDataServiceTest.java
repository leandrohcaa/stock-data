package code.nextly.stockdata;

import code.nextly.stockdata.dto.StockDataResponse;
import code.nextly.stockdata.exceptions.StockDataNotFoundException;
import code.nextly.stockdata.mapper.StockDataMapper;
import code.nextly.stockdata.mapper.StockDataMapperImpl;
import code.nextly.stockdata.model.StockData;
import code.nextly.stockdata.repository.DbCreateRepository;
import code.nextly.stockdata.repository.StockDataRepository;
import code.nextly.stockdata.service.StockDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockDataServiceTest {

  @Mock
  private StockDataRepository stockDataRepository;

  @Mock
  private DbCreateRepository dbCreateRepository;

  @InjectMocks
  private StockDataService stockDataService;

  private StockDataMapper stockDataMapper = new StockDataMapperImpl();

  @BeforeEach
  public void setUp() {
    stockDataService = new StockDataService(stockDataRepository, dbCreateRepository, stockDataMapper);
  }

  @Test
  public void testGetStockDataBySymbolAndDate() {
    String symbol = "MSFT";
    LocalDate date = LocalDate.of(2024, 6, 24);
    StockData stockData = new StockData();
    stockData.setSymbol(symbol);
    stockData.setDate(date);
    stockData.setOpenPrice(new BigDecimal(100.0));
    stockData.setClosePrice(new BigDecimal(110.0));
    stockData.setHighPrice(new BigDecimal(115.0));
    stockData.setLowPrice(new BigDecimal(95.0));
    stockData.setVolume(1000L);

    when(stockDataRepository.findSingleBySymbolAndDate(symbol, date)).thenReturn(Optional.of(stockData));

    StockDataResponse result = stockDataService.getStockDataBySymbolAndDate(symbol, date);

    assertEquals(result.getClosePrice(), stockData.getClosePrice());
    assertEquals(result.getOpenPrice(), stockData.getOpenPrice());
    assertEquals(result.getHighPrice(), stockData.getHighPrice());
    assertEquals(result.getLowPrice(), stockData.getLowPrice());
    assertEquals(result.getVolume(), stockData.getVolume());
  }

  @Test
  public void testGetStockDataBySymbolAndDate_NotFound() {
    String symbol = "MSFT";
    LocalDate date = LocalDate.of(2024, 6, 24);

    when(stockDataRepository.findSingleBySymbolAndDate(symbol, date)).thenReturn(Optional.empty());

    assertThrows(StockDataNotFoundException.class, () -> stockDataService.getStockDataBySymbolAndDate(symbol, date));
    verify(stockDataRepository, times(1)).findSingleBySymbolAndDate(symbol, date);
  }

  @Test
  public void testGetFirstExistingWeekDates() {
    LocalDate date = LocalDate.of(2024, 6, 24);
    when(stockDataRepository.findAllDates()).thenReturn(List.of(date));

    List<LocalDate> result = stockDataService.getFirstExistingWeekDates();

    assertNotNull(result);
    assertEquals(List.of(date), result);
  }

  @Test
  public void testGetAllSymbols() {
    List<String> symbols = List.of("MSFT", "AAPL");
    when(stockDataRepository.findAllSymbols()).thenReturn(symbols);

    List<String> result = stockDataService.getAllSymbols();

    assertEquals(symbols, result);
  }
}