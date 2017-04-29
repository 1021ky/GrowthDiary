package jp.vaivailx.growthdiary.model.dataaccess;

import java.util.Date;
import java.util.List;

import jp.vaivailx.growthdiary.model.DiaryData;

/**
 * Created by vaivailx on 2016/08/06.
 */
public class DropBoxDiaryDAO implements IDiaryDAO {

  @Override
  public void initialize() {

  }

  @Override
  public void close() {

  }

  @Override
  public void addDiary(DiaryData diaryData) {

  }

  @Override
  public void updateDiary(DiaryData diaryData) {

  }

  @Override
  public List<DiaryData> getDiaryList(Date startDate, Date endDate) {
    return null;
  }

  @Override
  public List<DiaryData> getLaterDiaryList(int getDiaryNum) {
    return null;
  }

  @Override
  public List<DiaryData> getLaterDiaryList(int diaryNum, int offset) {
    return null;
  }

  @Override
  public DiaryData getDiary(DiaryData diaryData) {
    return null;
  }

  @Override
  public DiaryData getDiary(long diaryID) {
    return null;
  }

  @Override
  public DiaryData getDiary(Date titleDate) {
    return null;
  }

  @Override
  public void deleteDiary(Date titleDate) {
    ;
  }
}
