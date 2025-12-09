package firsttd.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import firsttd.util.ListUtils;

public class ListUtilsTest {
  private ListUtils<String> listUtils = new ListUtils<String>();
  @Test
  public void should_return_list_from_index_ok() {
    List<String> list = List.of("a", "b", "c", "d", "e");
    List<String> expectedList = List.of("a", "c", "e");
    assertEquals(expectedList, listUtils.listFromIndex(list, 0, 2, 4));
  }
}
