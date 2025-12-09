package firsttd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import firsttd.config.DBConnection;
import firsttd.model.Category;
import firsttd.model.Product;
import firsttd.util.DataRetrieverTestUtils;
import firsttd.util.ListUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DataRetrieverTest {

  private DataRetriever dataRetriever = new DataRetriever();
  private DBConnection dbConnection = new DBConnection();
  private ListUtils<Product> productListUtils = new ListUtils<Product>();
  private ListUtils<Category> categoryListUtils = new ListUtils<Category>();
  private DataRetrieverTestUtils dataRetrieverTestUtils = new DataRetrieverTestUtils();
  private List<Product> dbProducts;
  private List<Category> dbCategories;

  @BeforeEach
  public void setUpTest() throws SQLException {
    try (Connection conn = dbConnection.getDBConnection()) {
      dataRetrieverTestUtils.deleteAndInstertProductsAndCategoriesToDatabase(conn);
    }
    dbProducts = dataRetrieverTestUtils.generateExpectedDatabaseProducts();
    dbCategories = dataRetrieverTestUtils.generateExpectedDatabaseCategory();
  }

  @Test
  public void should_return_all_categories_ok() {
    List<Category> categories = dataRetriever.getAllCategories();
    List<Category> expectedCategories = dbCategories;
    assertEquals(expectedCategories, categories);
  }

  @Test
  public void should_return_all_product_ok() {
    List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null);
    assertEquals(dbProducts, products);
  }

  @ParameterizedTest
  @CsvSource({
    "2, 2, 2 3"
  })
  public void should_return_product_with_pagination_ok(int page, int size, String expectedIndexes) {
    List<Product> products = dataRetriever.getProductList(page, size);
    List<Product> expectedProducts = productListUtils.listFromIndex(dbProducts, dataRetrieverTestUtils.parseStringToIntArray(expectedIndexes));
    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_all_product_list_with_pagination() {
    List<Product> products = dataRetriever.getProductList(1, 7);
    List<Product> expectedProducts =
        productListUtils.listFromIndex(dbProducts, 0, 1, 2, 3, 4, 5, 6);
    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_empty_list_with_pagination_out_of_bounds_ok() {
    List<Product> products = dataRetriever.getProductList(2, 8);
    List<Product> expectedProducts = List.of();
    assertEquals(expectedProducts, products);
  }

  @ParameterizedTest
  @CsvSource({
    "-1, 1", "1, -1", "-1, -1", "0, 0"
  })
  public void should_return_product_list_ko(int page, int size) {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class, () -> dataRetriever.getProductList(page, size));
    assertEquals("Page and size must be positive", exception.getMessage());
  }

  @ParameterizedTest
  @CsvSource(
      value = {"laptop, info, null, null, 0", "eCrAn, null, null, null, 5 6"},
      nullValues = "null")
  public void should_return_product_by_criteria_ok(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      String expectedIndexes) {
    List<Product> products =
        dataRetriever.getProductsByCriteria(productName, categoryName, creationMin, creationMax);
    List<Product> expectedProducts =
        productListUtils.listFromIndex(
            dbProducts, dataRetrieverTestUtils.parseStringToIntArray(expectedIndexes));
    assertEquals(expectedProducts, products);
  }
}
