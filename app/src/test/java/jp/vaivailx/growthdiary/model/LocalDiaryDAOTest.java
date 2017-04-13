package jp.vaivailx.growthdiary.model;

import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.realm.BuildConfig;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;
import jp.vaivailx.growthdiary.model.dataaccess.LocalDiaryDAO;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by vaivailx on 2016/12/25.
 */
//@RunWith(Enclosed.class)
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("")
@PrepareForTest({Realm.class, LocalDiaryDAO.class})
public class LocalDiaryDAOTest {

  public static LocalDiaryDAO localDiaryDAO;
  private final static String SCHEMA_NAME = "GrowthDiary";
  private final static String REALM_FILENAME = "growthdiary.realm";
  private RealmResults<DiaryData> diaryDataRealmResults;
  @Rule
  public PowerMockRule rule = new PowerMockRule();
  public static Realm mockRealm;

  // 準備と後始末
  @Before
  public void setUp() throws Exception {

    // Realmをモック化するための初期化
    mockStatic(RealmCore.class);
    mockStatic(RealmLog.class);
    mockStatic(Realm.class);
    mockStatic(RealmConfiguration.class);
    Realm.init(RuntimeEnvironment.application);

    // モック生成
    final Realm mockRealm = mock(Realm.class);
    final RealmConfiguration mockRealmConfig = mock(RealmConfiguration.class);
//    Mockito.when(Realm.getDefaultInstance()).thenReturn(mockRealm);

    // Realmのテストサンプルのそのまま引用。コアの部分のモックを作るのは難しいので何もしないようにしている(?)。
    // https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleActivityTest.java
    // TODO: Better solution would be just mock the RealmConfiguration.Builder class. But it seems there is some
    // problems for powermock to mock it (static inner class). We just mock the RealmCore.loadLibrary(Context) which
    // will be called by RealmConfiguration.Builder's constructor.
    doNothing().when(RealmCore.class);
    RealmCore.loadLibrary(any(Context.class));

    // サンプルと同じ。RealmConfigurationのモック生成。ただ、本来はBuilderのモックを作るべき。
    // TODO: Mock the RealmConfiguration's constructor. If the RealmConfiguration.Builder.build can be mocked, this
    // is not necessary anymore.
    whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);

    // コンフィグは常にモックのものを使う。
    when(Realm.getDefaultInstance()).thenReturn(mockRealm);

    // DiaryDTOデータ作成時は常に新しいインスタンスを生成
    when(mockRealm.createObject(DiaryData.class)).thenReturn(new DiaryData());

    // TODO モックのメソッドの振る舞いとテストデータを用意する
    long now = System.currentTimeMillis();
    DiaryData diaryData1 = new DiaryData(1, new Date(now), "fact1", "realization1", "knowledge1", "theme1", 1, new Date(now));
    now += 24 * 60 * 1000;
    DiaryData diaryData2 = new DiaryData(1, new Date(now), "fact2", "realization2", "knowledge2", "theme1", 2, new Date(now));
    now += 24 * 60 * 1000;
    DiaryData diaryData3 = new DiaryData(1, new Date(now), "fact3", "realization3", "knowledge3", "theme1", 3, new Date(now));
    now += 24 * 60 * 1000;
    DiaryData diaryData4 = new DiaryData(1, new Date(now), "fact4", "realization4", "knowledge4", "theme1", 4, new Date(now));

    List diaryDataList = Arrays.asList(diaryData1, diaryData2, diaryData3, diaryData4);

    // Create a mock RealmQuery
    RealmQuery<DiaryData> diaryQuery = mockRealmQuery();

    // When the RealmQuery performs findFirst, return the first record in the list.
    when(diaryQuery.findFirst()).thenReturn((DiaryData) diaryDataList.get(0));

    // When the where clause is called on the Realm, return the mock query.
    when(mockRealm.where(DiaryData.class)).thenReturn(diaryQuery);

    // When the RealmQuery is filtered on any string and any integer, return the person query
    when(diaryQuery.equalTo(anyString(), anyInt())).thenReturn(diaryQuery);

    // RealmResults is final, must mock static and also place this in the PrepareForTest annotation array.
    mockStatic(RealmResults.class);

    // Create a mock RealmResults
    RealmResults<DiaryData> diaryDatas = mockRealmResults();

    // When we ask Realm for all of the Person instances, return the mock RealmResults
    when(mockRealm.where(DiaryData.class).findAll()).thenReturn(diaryDatas);

    // When a between query is performed with any string as the field and any int as the
    // value, then return the diaryQuery itself
    when(diaryQuery.between(anyString(), anyInt(), anyInt())).thenReturn(diaryQuery);

    // When a beginsWith clause is performed with any string field and any string value
    // return the same person query
    when(diaryQuery.beginsWith(anyString(), anyString())).thenReturn(diaryQuery);

    // When we ask the RealmQuery for all of the Person objects, return the mock RealmResults
    when(diaryQuery.findAll()).thenReturn(diaryDatas);


    // The for(...) loop in Java needs an iterator, so we're giving it one that has items,
    // since the mock RealmResults does not provide an implementation. Therefore, anytime
    // anyone asks for the RealmResults Iterator, give them a functioning iterator from the
    // ArrayList of Persons we created above. This will allow the loop to execute.
    when(diaryDatas.iterator()).thenReturn(diaryDataList.iterator());

    // Return the size of the mock list.
    when(diaryDatas.size()).thenReturn(diaryDataList.size());

    this.mockRealm = mockRealm;
    this.diaryDataRealmResults = diaryDatas;


    this.mockRealm = mockRealm;

    localDiaryDAO = new LocalDiaryDAO();
    localDiaryDAO.initialize();
  }

  @SuppressWarnings("unchecked")
  private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
    return mock(RealmQuery.class);
  }

  @SuppressWarnings("unchecked")
  private <T extends RealmObject> RealmResults<T> mockRealmResults() {
    return mock(RealmResults.class);
  }

  private RealmConfiguration buildRealmConfiguration() {
    return new RealmConfiguration.Builder()
            .name(REALM_FILENAME)
            .schemaVersion(1L)
            .migration(new RealmMigration() {
              @Override
              public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                if (oldVersion == 0L) {
                  realm.getSchema().create(SCHEMA_NAME)
                          .addField("diary", DiaryData.class);
                  //noinspection UnusedAssignment
                  oldVersion++;
                }
              }
            })
            .build();
  }

  @After
  public void tearDown() throws Exception {
    localDiaryDAO.close();
  }

  @RunWith(Theories.class)
  public static class データ件数に関わらず結果が同じもののテスト {

    @Test
    public void initialize() throws Exception {
      localDiaryDAO.initialize();
    }

    @Test
    public void close() throws Exception {
      localDiaryDAO.initialize();
      localDiaryDAO.close();
    }
  }


  @RunWith(Theories.class)
  public static class 日記が0件のときのテスト {

    // テストデータ
    static class Fixture {
      DiaryData inputDiaryData;
      DiaryData expectedDiaryData;

      Fixture(DiaryData input, DiaryData expected) {
        this.inputDiaryData = input;
        this.expectedDiaryData = expected;
      }
    }

    @DataPoint
    public static DiaryData INPUT_DIARY_DTO = new DiaryData(1L, new Date(System.currentTimeMillis()), "fact dummy 1", "realization dummy 1", "knowledge dummy 1", "theme dummy 1", 5, new Date(System.currentTimeMillis()));
    @DataPoint
    public static DiaryData EXPECTED_DIARY_DTO = new DiaryData(1L, new Date(System.currentTimeMillis()), "fact dummy 1", "realization dummy 1", "knowledge dummy 1", "theme dummy 1", 5, new Date(System.currentTimeMillis()));


    // テスト
    @Test
    public void addDiary(DiaryData inputDiaryData, DiaryData expectedDiaryData) throws Exception {
      localDiaryDAO.initialize();
      localDiaryDAO.addDiary(inputDiaryData);
      List<DiaryData> actualDiaryDataList = localDiaryDAO.getLaterDiaryList(1);
      Assert.assertThat(actualDiaryDataList.get(0), is(expectedDiaryData));

    }

    @Test
    public void updateDiary(DiaryData inputDiaryData, DiaryData expectedDiaryData) throws Exception {
      localDiaryDAO.updateDiary(inputDiaryData);


    }

    @Test
    public void getDiaryList() throws Exception {

    }

    @Test
    public void getLaterDiaryList() throws Exception {

    }

  }
}