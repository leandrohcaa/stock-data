package code.nextly.stockdata.exceptions;


public class StockDataNotFoundException extends RuntimeException {

  public StockDataNotFoundException(String message) {
    super(message);
  }
}