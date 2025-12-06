package firsttd.service;

import firsttd.config.DBConnection;
import firsttd.model.Category;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
}
