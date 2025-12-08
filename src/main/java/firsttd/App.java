package firsttd;

import firsttd.model.Category;
import firsttd.model.Product;
import firsttd.service.DataRetriever;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/** Hello world! */
public class App {
  public static void main(String[] args) {
    DataRetriever dataRetriever = new DataRetriever();
    List<Category> categories = dataRetriever.getAllCategories();
    List<Product> products1 = dataRetriever.getProductList(1, 10);
    List<Product> products2 = dataRetriever.getProductList(1, 5);
    List<Product> products3 = dataRetriever.getProductList(1, 3);
    List<Product> products4 = dataRetriever.getProductList(2, 2);
    List<Product> products5 = dataRetriever.getProductsByCriteria("Dell", null, null, null);
    List<Product> products6 = dataRetriever.getProductsByCriteria(null, "info", null, null);
    List<Product> products7 = dataRetriever.getProductsByCriteria(null, "info", null, null);
    List<Product> products8 = dataRetriever.getProductsByCriteria("Iphone", "Mobile", null, null);
    List<Product> products9 =
        dataRetriever.getProductsByCriteria(
            null,
            null,
            LocalDate.parse("2024-02-01").atTime(23, 59, 59).atZone(ZoneId.of("UTC")).toInstant(),
            LocalDate.parse("2024-03-01").atTime(00, 00, 00).atZone(ZoneId.of("UTC")).toInstant());
    List<Product> products10 = dataRetriever.getProductsByCriteria("Samsung", "Bureau", null, null);
    List<Product> products11 =
        dataRetriever.getProductsByCriteria("Sony", "informatique", null, null);
    List<Product> products12 =
        dataRetriever.getProductsByCriteria(
            null,
            "audio",
            LocalDate.parse("2024-01-01").atTime(23, 59, 59).atZone(ZoneId.of("UTC")).toInstant(),
            LocalDate.parse("2024-12-01").atTime(00, 00, 00).atZone(ZoneId.of("UTC")).toInstant());
    List<Product> products13 = dataRetriever.getProductsByCriteria(null, null, null, null);
    List<Product> products14 = dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10);
    List<Product> products15 = dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 5);
    List<Product> products16 =
        dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10);


    System.out.println("------- MANUEL TEST -------");
    System.out.println(">>>>> getAllCategories(): " + categories.toString());
    System.out.println(">>>>> getProductList(1, 10): " + products1.toString());
    System.out.println(">>>>> getProductList(1, 5): " + products2.toString());
    System.out.println(">>>>> getProductList(1, 3): " + products3.toString());
    System.out.println(">>>>> getProductList(2, 2): " + products4.toString());
    System.out.println(">>>>> getProductsByCriteria(null, null, null, null): " + products13.toString());
    System.out.println(">>>>> getProductsByCriteria(null, null, null, null, 1, 10): " + products14.toString());
    System.out.println(">>>>> getProductsByCriteria(\"Dell\", null, null, null, 1, 5): " + products15.toString());
    System.out.println(">>>>> getProductsByCriteria(null, \"informatique\", null, null, 1, 10): " + products16.toString());
    System.out.println(">>>>> getProductsByCriteria(\"Iphone\", \"Mobile\", null, null): " + products8.toString());
    System.out.println(">>>>> getProductsByCriteria(null, null, LocalDate.parse(\"2024-02-01\").atTime(23, 59, 59).atZone(ZoneId.of(\"UTC\")).toInstant(), LocalDate.parse(\"2024-03-01\").atTime(00, 00, 00).atZone(ZoneId.of(\"UTC\")).toInstant()): " + products9.toString());
    System.out.println(">>>>> getProductsByCriteria(\"Samsung\", \"Bureau\", null, null): " + products10.toString());
    System.out.println(">>>>> getProductsByCriteria(\"Sony\", \"informatique\", null, null): " + products11.toString());
    System.out.println(">>>>> getProductsByCriteria(null, \"audio\", LocalDate.parse(\"2024-01-01\").atTime(23, 59, 59).atZone(ZoneId.of(\"UTC\")).toInstant(), LocalDate.parse(\"2024-12-01\").atTime(00, 00, 00).atZone(ZoneId.of(\"UTC\")).toInstant()): " + products12.toString());
  }
}
