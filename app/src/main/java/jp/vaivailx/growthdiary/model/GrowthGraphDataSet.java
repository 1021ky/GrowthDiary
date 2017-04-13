package jp.vaivailx.growthdiary.model;


import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet;
import com.github.mikephil.charting.utils.Utils;

import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.List;

/**
 * グラフで表示するデータ集合のクラス
 * Created by vaivailx on 2017/01/04.
 */

public class GrowthGraphDataSet {
  /**
   * 集計単位
   */
  public enum DataTerm {
    DAYS,   // 日単位
    WEEK,   // 週単位
    MONTH,  // 月単位
    YEAR,   // 年単位
  }
  private DataTerm dataTerm;
  private List<GrowthGraphEntry> dataList;
}
