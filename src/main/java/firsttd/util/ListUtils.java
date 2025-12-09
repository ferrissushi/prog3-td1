package firsttd.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils<T> {
  public List<T> listFromIndex(List<T> baseList, int... indexes) {
    List<T> list = new ArrayList<>();
    for (int index: indexes) {
      if (index < 0 || index > baseList.size()) {
        list.add(baseList.get(index));
      }
    }
    return list;
  }

}
