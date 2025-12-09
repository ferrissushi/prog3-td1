package firsttd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import firsttd.config.DBConnection;
import firsttd.model.Category;
import firsttd.model.Product;
import firsttd.util.DataRetrieverTestUtils;
import firsttd.util.ListUtils;
import java.sql.Connection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataRetrieverTest {

  private DataRetriever dataRetriever = new DataRetriever();
  private DBConnection dbConnection = new DBConnection();
  private ListUtils<Product> productListUtils = new ListUtils<Product>();
  private ListUtils<Category> categoryListUtils = new ListUtils<Category>();
  private DataRetrieverTestUtils dataRetrieverTestUtils = new DataRetrieverTestUtils();
  private List<Product> dbProducts;
  private List<Category> dbCategories;

  @BeforeEach
  public void setUpTest() {
    try (Connection conn = dbConnection.getDBConnection(); ) {
      dataRetrieverTestUtils.deleteAndInstertProductsAndCategoriesToDatabase(conn);
      dbProducts = dataRetrieverTestUtils.generateExpectedDatabaseProducts();
      dbCategories = dataRetrieverTestUtils.generateExpectedDatabaseCategory();
    } catch (Exception e) {
      throw new RuntimeException("An error occured while setting up the test: " + e);
    }
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

  @Test
  public void should_return_product_with_pagination_ok() {
    List<Product> products = dataRetriever.getProductList(2, 2);
    List<Product> expectedProducts = productListUtils.listFromIndex(dbProducts, 3, 4);
    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_empty_product_list_without_pagination_ok() {
    List<Product> products = dataRetriever.getProductList(0, 0);
    List<Product> expectedProducts = List.of();
    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_all_product_list_with_pagination() {
    List<Product> products = dataRetriever.getProductList(1, 7);
    List<Product> expectedProducts =
        productListUtils.listFromIndex(dbProducts, 1, 2, 3, 4, 5, 5, 2);
    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_empty_list_with_pagination_out_of_bounds_ok() {
    List<Product> products = dataRetriever.getProductList(2, 8);
    List<Product> expectedProducts = List.of();
    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_product_list_ko() {
    Exception exception =
        assertThrows(IllegalArgumentException.class, () -> dataRetriever.getProductList(-1, 1));
    assertEquals("Page and size must be positive", exception.getMessage());
  }

  @Test
  public void should_return_product_by_criteria_ok() {}
}
