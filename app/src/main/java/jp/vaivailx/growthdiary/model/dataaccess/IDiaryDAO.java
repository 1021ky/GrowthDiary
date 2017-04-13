package jp.vaivailx.growthdiary.model.dataaccess;

import java.util.Date;
import java.util.List;

import jp.vaivailx.growthdiary.model.DiaryData;

/**
 * Created by vaivailx on 2016/08/06.
 */
public interface IDiaryDAO {

  void initialize();

  void close();

  void addDiary(DiaryData diaryData);

  void updateDiary(DiaryData diaryData);

  List<DiaryData> getDiaryList(Date startDate, Date endDate);

  List<DiaryData> getLaterDiaryList(int getDiaryNum);

  DiaryData getDiary(DiaryData diaryData);

  DiaryData getDiary(long diaryID);

  DiaryData getDiary(Date titleDate);

  List<DiaryData> getLaterDiaryList(int diaryNum, int offset);
}
