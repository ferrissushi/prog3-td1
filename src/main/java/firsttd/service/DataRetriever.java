package firsttd.service;

import firsttd.config.DBConnection;
import firsttd.model.Category;
import firsttd.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DataRetriever {

  private DBConnection dbConnection;

  public DataRetriever() {
    this.dbConnection = new DBConnection();
  }

  public List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    try (Connection conn = dbConnection.getDBConnection();
        Statement st = conn.createStatement(); ) {
      ResultSet rs = st.executeQuery("SELECT id, name FROM product_category;");
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Category category = new Category(id, name);
        categories.add(category);
      }
      return categories;
    } catch (Exception e) {
      throw new RuntimeException("Cannot retrieve Categories: " + e);
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
        FROM product p join product_category c
        ON p.id = c.product_id
        ORDER BY p.id, c.id ASC
        LIMIT ? OFFSET ?;
        """;
    List<Product> products = new ArrayList<>();
    try (Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {
      ps.setInt(1, size);
      ps.setInt(2, offset);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        int categoryId = rs.getInt("category_id");
        String categoryName = rs.getString("category_name");
        Category category = new Category(categoryId, categoryName);
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Timestamp creationDatetimeTimestamp = rs.getTimestamp("creation_datetime");
        Instant creationDatetime = creationDatetimeTimestamp.toInstant();
        Product product = new Product(id, name, creationDatetime, category);
        products.add(product);
      }
      return products;
    } catch (Exception e) {
      throw new RuntimeException("Cannot retrieve products: " + e);
    }
  }

  public List<Product> getProductsByCriteria(
      String productName, String categoryName, Instant creationMin, Instant creationMax) {
    String sql =
        """
        SELECT p.id, p.name, p.creation_datetime, c.name as category_name, c.id as category_id
        FROM product p JOIN product_category c
        ON p.id = c.product_id
        """;
    List<Product> products = new ArrayList<>();
    List<String> conditions = new ArrayList<>();
    String productNameWhereStatement = null;
    String categoryNameWhereStatement = null;
    String creationMinWhereStatement = null;
    String creationMaxWhereStatement = null;
    if (productName != null && !productName.isBlank()) {
      productNameWhereStatement =
          """
          p.name ILIKE ?
          """;
    }
    if (categoryName != null && !categoryName.isBlank()) {
      categoryNameWhereStatement =
          """
          c.name ILIKE ?
          """;
    }
    if (creationMin != null) {
      creationMinWhereStatement =
          """
          p.creation_datetime > ?
          """;
    }
    if (creationMax != null) {
      creationMaxWhereStatement =
          """
          p.creation_datetime < ?
          """;
    }
    if (categoryNameWhereStatement == null
        && productNameWhereStatement == null
        && creationMinWhereStatement == null
        && creationMaxWhereStatement == null) {
      sql += " ORDER BY p.id, c.id ASC;";
    } else {
      sql += " WHERE ";
      conditions =
          Stream.of(
                  productNameWhereStatement,
                  categoryNameWhereStatement,
                  creationMinWhereStatement,
                  creationMaxWhereStatement)
              .filter(Objects::nonNull)
              .toList();
      String whereStatements = String.join(" AND ", conditions);
      sql += whereStatements + " ORDER BY p.id, c.id ASC;";
    }
    try (Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {

      for (int i = 0; i < conditions.size(); i++) {
        if (conditions.get(i).equals(productNameWhereStatement)) {
          ps.setString(i + 1, "%" + productName + "%");
        } else if (conditions.get(i).equals(categoryNameWhereStatement)) {
          ps.setString(i + 1, "%" + categoryName + "%");
        } else if (conditions.get(i).equals(creationMinWhereStatement)) {
          ps.setTimestamp(i + 1, Timestamp.from(creationMin));
        } else if (conditions.get(i).equals(creationMaxWhereStatement)) {
          ps.setTimestamp(i + 1, Timestamp.from(creationMax));
        }
      }

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        int categoryIdinDB = rs.getInt("category_id");
        String categoryNameinDB = rs.getString("category_name");
        Category category = new Category(categoryIdinDB, categoryNameinDB);
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Timestamp creationDatetimeTimestamp = rs.getTimestamp("creation_datetime");
        Instant creationDatetime = creationDatetimeTimestamp.toInstant();
        Product product = new Product(id, name, creationDatetime, category);
        products.add(product);
      }
      return products;
    } catch (Exception e) {
      throw new RuntimeException("Cannot retrieve products: " + e);
    }
  }

  public List<Product> getProductsByCriteria(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      int page,
      int size) {
    if (page <= 0 || size <= 0) {
      throw new IllegalArgumentException("Page and size must be positive");
    }
    int offset = size * (page - 1);
    String sql =
        """
        SELECT p.id, p.name, p.creation_datetime, c.name as category_name, c.id as category_id
        FROM product p JOIN product_category c
        ON p.id = c.product_id
        """;
    List<Product> products = new ArrayList<>();
    List<String> conditions = new ArrayList<>();
    String productNameWhereStatement = null;
    String categoryNameWhereStatement = null;
    String creationMinWhereStatement = null;
    String creationMaxWhereStatement = null;
    if (productName != null && !productName.isBlank()) {
      productNameWhereStatement =
          """
          p.name ILIKE ?
          """;
    }
    if (categoryName != null && !categoryName.isBlank()) {
      categoryNameWhereStatement =
          """
          c.name ILIKE ?
          """;
    }
    if (creationMin != null) {
      creationMinWhereStatement =
          """
          p.creation_datetime > ?
          """;
    }
    if (creationMax != null) {
      creationMaxWhereStatement =
          """
          p.creation_datetime < ?
          """;
    }
    if (categoryNameWhereStatement != null
        || productNameWhereStatement != null
        || creationMinWhereStatement != null
        || creationMaxWhereStatement != null) {
      sql += " WHERE ";
      conditions =
          Stream.of(
                  productNameWhereStatement,
                  categoryNameWhereStatement,
                  creationMinWhereStatement,
                  creationMaxWhereStatement)
              .filter(Objects::nonNull)
              .toList();
      String whereStatements = String.join(" AND ", conditions);
      sql += whereStatements;
    }
    sql += " ORDER BY p.id, c.id ASC LIMIT ? OFFSET ?;";
    try (Connection conn = dbConnection.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {
      int i = 0;
      for (; i < conditions.size(); i++) {
        if (conditions.get(i).equals(productNameWhereStatement)) {
          ps.setString(i + 1, "%" + productName + "%");
        } else if (conditions.get(i).equals(categoryNameWhereStatement)) {
          ps.setString(i + 1, "%" + categoryName + "%");
        } else if (conditions.get(i).equals(creationMinWhereStatement)) {
          ps.setTimestamp(i + 1, Timestamp.from(creationMin));
        } else if (conditions.get(i).equals(creationMaxWhereStatement)) {
          ps.setTimestamp(i + 1, Timestamp.from(creationMax));
        }
      }

      ps.setInt(i + 1, size);
      ps.setInt(i + 2, offset);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        int categoryIdinDB = rs.getInt("category_id");
        String categoryNameinDB = rs.getString("category_name");
        Category category = new Category(categoryIdinDB, categoryNameinDB);
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Timestamp creationDatetimeTimestamp = rs.getTimestamp("creation_datetime");
        Instant creationDatetime = creationDatetimeTimestamp.toInstant();
        Product product = new Product(id, name, creationDatetime, category);
        products.add(product);
      }
      return products;
    } catch (Exception e) {
      throw new RuntimeException("Cannot retrieve products: " + e);
    }
  }
}
