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

public class DataRetriever {

  private DBConnection dbConnection;

  public DataRetriever() {
    this.dbConnection = new DBConnection();
  }

  public List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    try (Connection conn = dbConnection.getDBConnection();
        Statement st = conn.createStatement(); ) {
      ResultSet rs = st.executeQuery("SELECT id, name FROM category;");
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
    int offset = size * (page - 1) + 1;
    String sql = """
    SELECT p.id, p.name, p.creation_datetime, c.id as category_id, c.name as category_name
    FROM product p join product_category c
    ON p.id = c.product_id
    LIMIT ? OFFSET ?;
    """;
    List<Product> products = new ArrayList<>();
    try(Connection conn = dbConnection.getDBConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
    ) {
      ps.setInt(1, size);
      ps.setInt(2, offset);
      ResultSet rs = ps.executeQuery();
      while(rs.next()) {
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
    } catch(Exception e) {
      throw new RuntimeException("Cannot retrieve products: " + e);
    }
  }
}






