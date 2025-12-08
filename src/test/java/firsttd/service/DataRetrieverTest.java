package firsttd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import firsttd.config.DBConnection;
import firsttd.model.Category;
import firsttd.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class DataRetrieverTest {

  private DataRetriever dataRetriever = new DataRetriever();
  private DBConnection dbConnection = new DBConnection();

  @BeforeAll
  public void setUpTest() {
    String sql =
        """
          INSERT INTO product (id,name, price, creation_datetime) VALUES
          (1,'Laptop Dell XPS', 4500.00, '2024-01-15 09:30:00'),
          (2,'Iphone 13', 5200.00, '2024-02-01 14:10:00'),
          (3,'Casque Sony WH1000', 890.50, '2024-02-10 16:45:00'),
          (4,'Clavier Logitech', 180.00, '2024-03-05 11:20:00'),
          (5,'Ecran Samsung 27""', 1200.00, '2024-03-18 08:00:00')

          INSERT INTO product_category (id, name, product_id) VALUES
          (1,'Informatique', 1),
          (2,'Telephonie', 2),
          (3,'Audio', 3),
          (4,'Accessoires', 4),
          (5,'Informatique', 5 ),
          (6,'Bureau', 5),
          (7,'Mobile', 2);
        """;
    try (Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {
      ps.executeUpdate();
      System.out.println("Data inserted successfully");
    } catch (Exception e) {
      System.out.println("Data already found in the database.");
    }
  }

  @Test
  public void should_return_all_categories_ok() {
    List<Category> categories = dataRetriever.getAllCategories();
    List<Category> expectedCategories =
        List.of(
            new Category(1, "Informatique"),
            new Category(2, "Telephonie"),
            new Category(3, "Audio"),
            new Category(4, "Accessoires"),
            new Category(5, "Informatique"),
            new Category(6, "Bureau"),
            new Category(7, "Mobile"));

    assertEquals(expectedCategories, categories);
  }

  @Test
  public void should_return_categories_ko() {
    // Remove this later.... getAllCategories() can't fail.
  }

  @Test
  public void should_return_all_product_ok() {
    List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null);
    List<Product> expectedProducts =
        List.of(
            new Product(
                1,
                "Laptop Dell XPS",
                LocalDateTime.parse(
                        "2024-01-15 09:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(1, "Informatique")),
            new Product(
                2,
                "Iphone 13",
                LocalDateTime.parse(
                        "2024-02-01 14:10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(2, "Telephonie")),
            new Product(
                3,
                "Casque Sony WH1000",
                LocalDateTime.parse(
                        "2024-02-10 16:45:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(3, "Audio")),
            new Product(
                4,
                "Clavier Logitech",
                LocalDateTime.parse(
                        "2024-03-05 11:20:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(4, "Accessoires")),
            new Product(
                5,
                "Ecran Samsung 27\"\"",
                LocalDateTime.parse(
                        "2024-03-18 08:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(5, "Informatique")),
            new Product(
                5,
                "Ecran Samsung 27\"\"",
                LocalDateTime.parse(
                        "2024-03-18 08:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(6, "Bureau")),
            new Product(
                2,
                "Iphone 13",
                LocalDateTime.parse(
                        "2024-02-01 14:10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(7, "Mobile")));

    assertEquals(expectedProducts, products);
  }

  @Test
  public void should_return_product_with_pagination_ok() {
    List<Product> products = dataRetriever.getProductList(2, 2);
    List<Product> expectedProducts =
        List.of(
            new Product(
                3,
                "Casque Sony WH1000",
                LocalDateTime.parse(
                        "2024-02-10 16:45:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(3, "Audio")),
            new Product(
                4,
                "Clavier Logitech",
                LocalDateTime.parse(
                        "2024-03-05 11:20:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant(),
                new Category(4, "Accessoires")));
    assertEquals(expectedProducts, products);
  }
}
