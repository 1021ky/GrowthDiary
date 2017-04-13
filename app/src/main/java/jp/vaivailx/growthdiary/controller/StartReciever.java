package jp.vaivailx.growthdiary.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jp.vaivailx.growthdiary.NotificationService;

/**
 * Created by vaivailx on 2017/03/27.
 */

public class StartReciever extends BroadcastReceiver {
  private Intent intent = null;

  @Override
  public void onReceive(Context context, Intent intent) {
    intent = new Intent(context, NotificationService.class);
    context.startService(intent);
    Log.i("StartReciever", "onReceive: ");
  }
}
