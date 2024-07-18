package code.nextly.stockdata.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DataCollectionRequest {
  @NotNull
  private LocalDate startDate;
  @NotEmpty
  private List<String> symbols;
}