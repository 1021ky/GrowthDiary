package jp.vaivailx.growthdiary.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 日記のDTO.
 * Created by vaivailx on 2016/07/27.
 */
public class DiaryData extends RealmObject {

  // id
  @PrimaryKey
  public long id;

  // 日記を付けた日。タイトルになる。
  @Required
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
  @Required
  @Index
  public Date updateDate;

  public DiaryData() {

  }

  public DiaryData(long id, Date titleDate, String fact, String realization, String knowledge, String theme, int evaluation, Date updateDate) {
    this.id = id;
    this.titleDate = titleDate;
    this.fact = fact;
    this.realization = realization;
    this.knowledge = knowledge;
    this.theme = theme;
    this.evaluation = evaluation;
    this.updateDate = updateDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DiaryData diaryData = (DiaryData) o;

    if (id != diaryData.id) return false;
    if (evaluation != diaryData.evaluation) return false;
    if (titleDate != null ? !titleDate.equals(diaryData.titleDate) : diaryData.titleDate != null)
      return false;
    if (fact != null ? !fact.equals(diaryData.fact) : diaryData.fact != null) return false;
    if (realization != null ? !realization.equals(diaryData.realization) : diaryData.realization != null)
      return false;
    if (knowledge != null ? !knowledge.equals(diaryData.knowledge) : diaryData.knowledge != null)
      return false;
    if (theme != null ? !theme.equals(diaryData.theme) : diaryData.theme != null) return false;
    return updateDate != null ? updateDate.equals(diaryData.updateDate) : diaryData.updateDate == null;

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (titleDate != null ? titleDate.hashCode() : 0);
    result = 31 * result + (fact != null ? fact.hashCode() : 0);
    result = 31 * result + (realization != null ? realization.hashCode() : 0);
    result = 31 * result + (knowledge != null ? knowledge.hashCode() : 0);
    result = 31 * result + (theme != null ? theme.hashCode() : 0);
    result = 31 * result + evaluation;
    result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
    return result;
  }

  /**
   * 保存可能な内容かどうかを返す
   * @return 保存可能ならばtrue
   */
  public boolean saves() {
    if(titleDate == null) {
      return false;
    }
    return true;
  }
}
