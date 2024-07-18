package code.nextly.stockdata.repository;

import code.nextly.stockdata.model.StockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockDataRepository extends JpaRepository<StockData, Long> {

  Optional<StockData> findSingleBySymbolAndDate(String symbol, LocalDate date);

  void deleteBySymbol(String symbol);

  @Query("SELECT DISTINCT s.date FROM StockData s ORDER BY s.date")
  List<LocalDate> findAllDates();

  @Query("SELECT DISTINCT s.symbol FROM StockData s")
  List<String> findAllSymbols();
}