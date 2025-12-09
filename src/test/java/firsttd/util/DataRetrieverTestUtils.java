package firsttd.util;

import firsttd.model.Category;
import firsttd.model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DataRetrieverTestUtils {
  public int[] parseStringToIntArray(String string) {
    return Arrays.stream(string.split(" ")).mapToInt(Integer::parseInt).toArray();
  }

  public void deleteAndInstertProductsAndCategoriesToDatabase(Connection conn) throws SQLException {
    try (Statement stmt = conn.createStatement()) {

      stmt.executeUpdate("DELETE FROM product_category");
      stmt.executeUpdate("DELETE FROM product");

      stmt.executeUpdate(
          """
              INSERT INTO product (id, name, price, creation_datetime) VALUES
              (1,'Laptop Dell XPS', 4500.00, '2024-01-15 09:30:00'),
              (2,'Iphone 13',        5200.00, '2024-02-01 14:10:00'),
              (3,'Casque Sony WH1000', 890.50, '2024-02-10 16:45:00'),
              (4,'Clavier Logitech', 180.00, '2024-03-05 11:20:00'),
              (5,'Ecran Samsung 27""', 1200.00, '2024-03-18 08:00:00')
          """);

      stmt.executeUpdate(
          """
              INSERT INTO product_category (id, name, product_id) VALUES
              (1,'Informatique', 1),
              (2,'Telephonie',    2),
              (3,'Audio',         3),
              (4,'Accessoires',   4),
              (5,'Informatique',  5),
              (6,'Bureau',        5),
              (7,'Mobile',        2)
          """);
    }
  }

  public List<Category> generateExpectedDatabaseCategory() {
    return List.of(
        new Category(1, "Informatique"),
        new Category(2, "Telephonie"),
        new Category(3, "Audio"),
        new Category(4, "Accessoires"),
        new Category(5, "Informatique"),
        new Category(6, "Bureau"),
        new Category(7, "Mobile"));
  }

  public List<Product> generateExpectedDatabaseProducts() {
    return List.of(
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
            2,
            "Iphone 13",
            LocalDateTime.parse(
                    "2024-02-01 14:10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.systemDefault())
                .toInstant(),
            new Category(7, "Mobile")),
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
            new Category(6, "Bureau")));
  }
}
