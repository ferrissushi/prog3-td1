package firsttd.model;

import java.time.Instant;

public class Product {
  private int id;
  private String name;
  private Instant creationDateTime;
  private Category category;

  public Product(int id, String name, Instant creationDateTime, Category category) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.creationDateTime = creationDateTime;
  }

  public String getCategoryName() {
    return category.getName();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Instant getCreationDateTime() {
    return creationDateTime;
  }

  public Category getCategory() {
    return category;
  }

  @Override
  public String toString() {
    return "Product [id="
        + id
        + ", name="
        + name
        + ", creationDateTime="
        + creationDateTime
        + ", category="
        + category.toString()
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((creationDateTime == null) ? 0 : creationDateTime.hashCode());
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Product other = (Product) obj;
    if (id != other.id)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (creationDateTime == null) {
      if (other.creationDateTime != null)
        return false;
    } else if (!creationDateTime.equals(other.creationDateTime))
      return false;
    if (category == null) {
      if (other.category != null)
        return false;
    } else if (!category.equals(other.category))
      return false;
    return true;
  }
}
