package jp.vaivailx.growthdiary.model.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import jp.vaivailx.growthdiary.model.DiaryData;

/**
 * Created by vaivailx on 2016/08/06.
 */
public class LocalDiaryDAO implements IDiaryDAO {

  // 非同期でデータを読み込んだものをいれるためのリスト
  private List<DiaryData> diaryDataList = new ArrayList<DiaryData>();
  private Realm mRealm;

  @Override
  public void initialize() {
    mRealm = Realm.getDefaultInstance();
  }

  public void close() {
    if (mRealm != null) {
      mRealm.close();
      mRealm = null;
    }
  }

  @Override
  public void addDiary(final DiaryData diaryData) {
    mRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        // 何も書いてない日記は追加しない
        if (diaryData.fact == ""
                && diaryData.realization == ""
                && diaryData.theme == ""
                && diaryData.knowledge == ""
                && diaryData.evaluation == 0){
          return;
        }

        // ダブっていたら追加しない
        if (realm.where(DiaryData.class).equalTo("titleDate", diaryData.titleDate).count() >= 1){
          return;
        }
        diaryData.id = getNextDiaryId();
        diaryData.updateDate = new Date();
        realm.copyToRealm(diaryData);
      }
    });
  }

  /**
   * DiaryのIDの最大値を+1した値を取得する。
   * Userが1度も作成されていなければ1を取得する。
   */
  private long getNextDiaryId() {
    long nextID = 1;
    Number maxID = mRealm.where(DiaryData.class).max("id");
    // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
    if(maxID != null) {
      nextID = maxID.longValue() + 1;
    }
    return nextID;
  }

  @Override
  public void updateDiary(final DiaryData diaryData) {
    mRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        if (realm.where(DiaryData.class)
                .equalTo("titleDate", diaryData.titleDate)
                .findAll()
                .where()
                .equalTo("updateDate", diaryData.updateDate)
                .findAll().size() == 0) {
          return;
        }
        realm.copyToRealmOrUpdate(diaryData);
      }
    });
  }

  @Override
  public List<DiaryData> getDiaryList(final Date startDate, final Date endDate) {
    return mRealm.where(DiaryData.class).between("titleDate", startDate, endDate).findAllSorted("titleDate", Sort.ASCENDING);
  }

  @Override
  public List<DiaryData> getLaterDiaryList(int diaryNum) {
    return  getLaterDiaryList(diaryNum, 0);
  }

  @Override
  public List<DiaryData> getLaterDiaryList(int diaryNum, int offset) {
    List<DiaryData> diaryList = mRealm.where(DiaryData.class).lessThanOrEqualTo("titleDate", new Date(System.currentTimeMillis())).findAllSorted("titleDate", Sort.DESCENDING);
    if (diaryList.size() <= offset) {
      return new ArrayList<DiaryData>();
    }
    return  diaryList.subList(offset, diaryList.size() < diaryNum + offset? diaryList.size() : diaryNum + offset);
  }

  @Override
  public DiaryData getDiary(DiaryData diaryData) {
    return mRealm.where(DiaryData.class).equalTo("titleDate", diaryData.titleDate).findFirst();
  }

  @Override
  public DiaryData getDiary(long diaryID) {
    return mRealm.where(DiaryData.class).equalTo("id", diaryID).findFirst();
  }

  @Override
  public DiaryData getDiary(Date titleDate) {
    return mRealm.where(DiaryData.class).equalTo("titleDate", titleDate).findFirst();
  }
}
