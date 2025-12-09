package firsttd.service;

import firsttd.config.DBConnection;
import firsttd.model.Category;
import firsttd.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

  private DBConnection dbConnection;

  public DataRetriever() {
    this.dbConnection = new DBConnection();
  }

  public List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    try (Connection conn = dbConnection.getDBConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, name FROM product_category;")) {

      while (rs.next()) {
        categories.add(new Category(rs.getInt("id"), rs.getString("name")));
      }

      return categories;

    } catch (SQLException e) {
      throw new RuntimeException("Cannot retrieve Categories", e);
    }
  }

  public List<Product> getProductList(int page, int size) {
    if (page <= 0 || size <= 0) {
      throw new IllegalArgumentException("Page and size must be positive");
    }
    int offset = size * (page - 1);
    String sql =
        """
        SELECT p.id, p.name, p.creation_datetime, c.id as category_id, c.name as category_name
        FROM product p LEFT JOIN product_category c
        ON p.id = c.product_id
        ORDER BY p.id, c.id ASC
        LIMIT ? OFFSET ?;
        """;
    List<Product> products = new ArrayList<>();
    try (Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {
      ps.setInt(1, size);
      ps.setInt(2, offset);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Product product = createProductObject(rs);
          products.add(product);
        }
      }
      return products;
    } catch (Exception e) {
      throw new RuntimeException("Cannot retrieve products: " + e);
    }
  }

  public Product createProductObject(ResultSet rs) throws SQLException {
    int categoryId = rs.getInt("category_id");
    Category category = null;
    if (!rs.wasNull()) {
      String categoryName = rs.getString("category_name");
      category = new Category(categoryId, categoryName);
    }
    int id = rs.getInt("id");
    String name = rs.getString("name");
    Timestamp creationDatetimeTimestamp = rs.getTimestamp("creation_datetime");
    Instant creationDatetime = creationDatetimeTimestamp.toInstant();
    return new Product(id, name, creationDatetime, category);
  }

  public List<Product> getProductsByCriteria(
      String productName, String categoryName, Instant creationMin, Instant creationMax) {
    return getProductsByCriteria(productName, categoryName, creationMin, creationMax, null, null);
  }

  public List<Product> getProductsByCriteria(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      Integer page,
      Integer size) {

    if (page == null) {
      page = 1;
    }
    if (size == null) {
      size = Integer.MAX_VALUE;
    }
    if (page <= 0 || size <= 0) {
      throw new IllegalArgumentException("Page and size must be positive");
    }

    int offset = size * (page - 1);

    StringBuilder sql = new StringBuilder();
    sql.append(
        """
        SELECT p.id, p.name, p.creation_datetime,
               c.id AS category_id, c.name AS category_name
        FROM product p
        LEFT JOIN product_category c ON p.id = c.product_id
        """);

    List<String> whereClauses = new ArrayList<>();
    List<Object> parameters = new ArrayList<>();

    if (productName != null && !productName.isBlank()) {
      whereClauses.add("p.name ILIKE ?");
      parameters.add("%" + productName + "%");
    }
    if (categoryName != null && !categoryName.isBlank()) {
      whereClauses.add("c.name ILIKE ?");
      parameters.add("%" + categoryName + "%");
    }
    if (creationMin != null) {
      whereClauses.add("p.creation_datetime > ?");
      parameters.add(Timestamp.from(creationMin));
    }
    if (creationMax != null) {
      whereClauses.add("p.creation_datetime < ?");
      parameters.add(Timestamp.from(creationMax));
    }

    if (!whereClauses.isEmpty()) {
      sql.append(" WHERE ");
      sql.append(String.join(" AND ", whereClauses));
    }

    sql.append(" ORDER BY p.id, c.id ASC LIMIT ? OFFSET ?;");

    try (Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString())) {

      int id = 1;
      for (Object parameter : parameters) {
        ps.setObject(id++, parameter);
      }

      ps.setInt(id++, size);
      ps.setInt(id, offset);

      List<Product> products = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          products.add(createProductObject(rs));
        }
      }
      return products;

    } catch (SQLException e) {
      throw new RuntimeException("Cannot retrieve products: " + e, e);
    }
  }
}
