package jp.vaivailx.growthdiary.view;

import java.util.Date;

/**
 * Created by vaivailx on 2017/03/07.
 */

public class DiaryDTO {
  // id
  public long id;

  // 日記を付けた日。タイトルになる。
  public Date titleDate;

  // 事実
  public String fact;

  // 気づき
  public String realization;

  // 教訓
  public String knowledge;

  // 目標
  public String theme;

  // 評価
  public int evaluation;

  // 更新日
  public Date updateDate;

  public DiaryDTO() {
    id = 0;
    titleDate = null;
    fact = null;
    realization = null;
    knowledge = null;
    theme = null;
    evaluation = 0;
    updateDate = null;
  }
}
