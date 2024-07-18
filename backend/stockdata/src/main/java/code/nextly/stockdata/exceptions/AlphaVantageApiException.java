package code.nextly.stockdata.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AlphaVantageApiException extends RuntimeException {

  public AlphaVantageApiException(String message) {
    super(message);
  }
}