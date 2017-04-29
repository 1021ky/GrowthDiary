package jp.vaivailx.growthdiary;

import android.app.Application;
import android.content.Intent;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import jp.vaivailx.growthdiary.model.DiaryData;

/**
 * Created by vaivailx on 2016/12/11.
 */

public class GrowthDiaryApplication extends Application {
  private final static String SCHEMA_NAME = "GrowthDiary";
  private final static String REALM_FILENAME = "growthdiary.realm";
  private Intent intent = null;
  @Override
  public void onCreate() {
    super.onCreate();
    // bootstrap初期化
    TypefaceProvider.registerDefaultIconSets();

    Realm.init(getApplicationContext());
    RealmConfiguration realmConfig = buildRealmConfiguration();
    // テスト用コード
//    Realm.deleteRealm(realmConfig);
    Realm.setDefaultConfiguration(realmConfig);

    intent = new Intent(this.getApplicationContext(), NotificationService.class);
    startService(intent);
  }

  private RealmConfiguration buildRealmConfiguration() {
    return new RealmConfiguration.Builder()
            .name(REALM_FILENAME)
//            .encryptionKey(getKey())
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
  }}

