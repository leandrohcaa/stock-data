package code.nextly.stockdata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String symbol;

  @NotNull
  private LocalDate date;

  @NotNull
  private BigDecimal openPrice;

  @NotNull
  private BigDecimal closePrice;

  @NotNull
  private BigDecimal highPrice;

  @NotNull
  private BigDecimal lowPrice;

  @NotNull
  private Long volume;

}