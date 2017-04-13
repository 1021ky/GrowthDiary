package jp.vaivailx.growthdiary;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import jp.vaivailx.growthdiary.view.MainActivity;

import static android.content.ContentValues.TAG;

public class NotificationService extends Service {
  private final int CHECK_INTERVAL = 1000 * 60 * 15;
  private final int NOTIFY_ID = 1000;
  public final int NOTIFICATION_REQUEST_CODE = 1001;
  private Calendar notificationTime;

  public NotificationService() {
    notificationTime = Calendar.getInstance();
    notificationTime.set(Calendar.HOUR_OF_DAY, 21);
    notificationTime.set(Calendar.MINUTE, 0);
    notificationTime.set(Calendar.SECOND, 0);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
  public @interface SERVICE_FLAG{}

  @Override
  public int onStartCommand(Intent intent, @SERVICE_FLAG int flags, int startId) {
    AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        try {
          if (isNotificationTime()) {
            showNotification();
          }
          Thread.sleep(CHECK_INTERVAL);
        } catch (InterruptedException e) {
          Log.e(TAG, "doInBackground: " + e.getMessage());;
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        stopSelf();
      }
    };
    asyncTask.execute();
    return Service.START_STICKY;
  }

  private boolean isNotificationTime(){
    Calendar currentTime = Calendar.getInstance();
    if (currentTime.after(notificationTime)){
      return true;
    }
    return false;
  }

  private void showNotification() {
    Intent editorIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingEditorIntent = PendingIntent.getActivity(getApplicationContext(),
            NOTIFICATION_REQUEST_CODE, editorIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(getApplicationContext())
            .setContentTitle(getString(R.string.NOTIFICATION_TITLE))
            .setContentText(getString(R.string.NOTIFICATION_CONTENT))
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentIntent(pendingEditorIntent)
            .setAutoCancel(true);
    Notification notification = builder.build();
    NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
    manager.notify(NOTIFY_ID, notification);
  }
}
