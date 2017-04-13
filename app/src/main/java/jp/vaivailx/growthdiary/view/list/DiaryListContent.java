package jp.vaivailx.growthdiary.view.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing content.
 * <p>
 */
public class DiaryListContent {

  /**
   * An array of sample (dummy) items.
   */
  public static final List<DiaryItem> ITEMS = new ArrayList<DiaryItem>();

  /**
   * A map of sample (dummy) items, by ID.
   */
  public static final Map<Long, DiaryItem> ITEM_MAP = new HashMap<Long, DiaryItem>();

  private static final int COUNT = 3;

  static {
    // Add some sample items.
    for (int i = 1; i <= COUNT; i++) {
      addItem(createItem(i));
    }
  }

  private static void addItem(DiaryItem item) {
    ITEMS.add(item);
    ITEM_MAP.put(item.id, item);
  }

  private static DiaryItem createItem(int position) {
    return new DiaryItem(Long.valueOf(position), String.valueOf(position), "Item " + position, makeDetails(position));
  }

  private static String makeDetails(int position) {
    StringBuilder builder = new StringBuilder();
    builder.append("Details about Item: ").append(position);
    for (int i = 0; i < position; i++) {
      builder.append("\nMore rate information here.");
    }
    return builder.toString();
  }

  /**
   * An item representing a piece of content.
   */
  public static class DiaryItem {
    public final long id;
    public final String titleDate;
    public final String content;
    public final String rate;

    public DiaryItem(long id, String titleDate, String rate, String content) {
      this.id = id;
      this.titleDate = titleDate;
      this.rate = rate;
      this.content = content;
    }

    @Override
    public String toString() {
      return content;
    }
  }
}
