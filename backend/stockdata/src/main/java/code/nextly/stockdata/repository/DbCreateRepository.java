package code.nextly.stockdata.repository;

import code.nextly.stockdata.exceptions.DbCreateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Repository
@Slf4j
public class DbCreateRepository {

  private final DataSource dataSource;
  @Lazy
  public DbCreateRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Value("${db_create.filename}")
  private String dbCreateFileName;

  public void executeDbCreateScript() {
    Resource resource = new ClassPathResource(dbCreateFileName);
    try (Connection connection = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(connection, resource);
    } catch (SQLException e) {
      log.error("Error during db_create script execution.", e);
      throw new DbCreateException("Error during db_create script execution.");
    }
  }
}