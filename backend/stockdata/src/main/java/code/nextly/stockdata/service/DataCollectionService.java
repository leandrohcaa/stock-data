package code.nextly.stockdata.service;

import code.nextly.stockdata.dto.AlphaVantageQueryResponse;
import code.nextly.stockdata.exceptions.AlphaVantageApiException;
import code.nextly.stockdata.mapper.StockDataMapper;
import code.nextly.stockdata.model.StockData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataCollectionService {

  private final StockDataService stockDataService;
  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final StockDataMapper stockDataMapper;
  public DataCollectionService(StockDataService stockDataService,
                               WebClient webClient,
                               ObjectMapper objectMapper,
                               StockDataMapper stockDataMapper) {
    this.stockDataService = stockDataService;
    this.webClient = webClient;
    this.objectMapper = objectMapper;
    this.stockDataMapper = stockDataMapper;
  }

  @Value("${alpha_vantage.url}")
  private String alphaVantageUrl;
  @Value("${alpha_vantage.api_key}")
  private String alphaVantageApiKey;
  @Value("${alpha_vantage.query_api}")
  private String alphaVantageQueryApi;
  @Value("${alpha_vantage.query_symbol_query_param}")
  private String alphaVantageQuerySymbolQueryParam;
  @Value("${alpha_vantage.query_function_query_param}")
  private String alphaVantageQueryFunctionQueryParam;
  @Value("${alpha_vantage.query_apikey_query_param}")
  private String alphaVantageQueryApiKeyQueryParam;
  @Value("${alpha_vantage.query_function_value}")
  private String alphaVantageQueryFunctionValue;
  @Value("${is_static_results}")
  private Boolean isStaticResults;


  public void collectStockData(LocalDate startDate, List<String> symbols) {
    for (String symbol : symbols) {
      AlphaVantageQueryResponse alphaVantageQueryResponse = getAlphaVantageQueryResponse(symbol);
      LocalDate endDate = startDate.plusWeeks(3);
      List<StockData> stockDataBySymbolList = stockDataMapper.mapToStockDataList(alphaVantageQueryResponse,
          startDate, endDate);
      stockDataService.deleteAndSaveStockData(stockDataBySymbolList);
    }
  }

  private AlphaVantageQueryResponse getAlphaVantageQueryResponse(String symbol) {
    if (isStaticResults) {
      log.info("Getting static results. Symbol: " + symbol);
      return getResponseFromStaticFile(symbol);
    }

    String uri = UriComponentsBuilder.fromHttpUrl(alphaVantageUrl + alphaVantageQueryApi)
        .queryParam(alphaVantageQueryFunctionQueryParam, alphaVantageQueryFunctionValue)
        .queryParam(alphaVantageQuerySymbolQueryParam, symbol)
        .queryParam(alphaVantageQueryApiKeyQueryParam, alphaVantageApiKey)
        .toUriString();

    log.info("Getting results from external endpoint. Uri: " + uri + ". Symbol: " + symbol);
    Map<String, Object> response = webClient.get()
        .uri(uri)
        .retrieve()
        .onStatus(
            status -> !status.is2xxSuccessful(),
            clientResponse -> clientResponse.bodyToMono(String.class)
                .flatMap(body -> Mono.error(new AlphaVantageApiException("AlphaVantage API Error: " + clientResponse.statusCode() + " - " + body)))
        )
        .bodyToMono(Map.class)
        .block();
    if (response != null && response.size() == 1 && response.containsKey("Information")) {
      throw new AlphaVantageApiException((String) response.get("Information"));
    }
    return objectMapper.convertValue(response, AlphaVantageQueryResponse.class);
  }

  private AlphaVantageQueryResponse getResponseFromStaticFile(String symbol) {
    try {
      ClassPathResource resource = new ClassPathResource(String.format("static_results_%s.json", symbol));
      byte[] binaryData = FileCopyUtils.copyToByteArray(resource.getInputStream());
      AlphaVantageQueryResponse response = objectMapper.readValue(new String(binaryData, StandardCharsets.UTF_8),
          AlphaVantageQueryResponse.class);
      return response;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}