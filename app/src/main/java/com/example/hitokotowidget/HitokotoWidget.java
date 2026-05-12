package com.example.hitokotowidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HitokotoWidget extends AppWidgetProvider {

    private static final String ACTION_UPDATE = "com.example.hitokotowidget.UPDATE";
    private static final String ACTION_ALARM = "com.example.hitokotowidget.ALARM";
    private static final long UPDATE_INTERVAL = 60 * 60 * 1000; // 1小时
    private static final int ALARM_REQUEST_CODE = 1001;
    private static final String API_URL = "https://api.codelife.cc/yiyan/random?lang=cn";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        setupAlarmManager(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        cancelAlarmManager(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, HitokotoWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        } else if (ACTION_ALARM.equals(intent.getAction())) {
            // 闹钟触发，更新widget并重新设置下次闹钟
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, HitokotoWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
            setupAlarmManager(context);
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // 设置点击刷新
        Intent intent = new Intent(context, HitokotoWidget.class);
        intent.setAction(ACTION_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.tv_hitokoto, pendingIntent);

        // 先显示加载中
        appWidgetManager.updateAppWidget(appWidgetId, views);

        // 在新线程中获取数据
        new Thread(() -> {
            try {
                String response = fetchHitokoto();

                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");

                    if (code == 200) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String hitokoto = data.getString("hitokoto");
                        String from = data.getString("from");

                        updateWidgetUI(context, hitokoto, from);
                    } else {
                        updateWidgetUI(context, "加载失败", "请稍后重试");
                    }
                } else {
                    updateWidgetUI(context, "网络错误", "请检查网络连接");
                }

            } catch (Exception e) {
                e.printStackTrace();
                updateWidgetUI(context, "解析错误", "数据格式异常");
            }
        }).start();
    }

    private static String fetchHitokoto() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    private static void updateWidgetUI(Context context, String hitokoto, String from) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, HitokotoWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.tv_hitokoto, hitokoto);
            views.setTextViewText(R.id.tv_author, from);

            // 重新设置点击事件
            Intent intent = new Intent(context, HitokotoWidget.class);
            intent.setAction(ACTION_UPDATE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.tv_hitokoto, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static void setupAlarmManager(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, HitokotoWidget.class);
        intent.setAction(ACTION_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 使用 setRepeating 并配合 RTC_WAKEUP
        // RTC_WAKEUP 会在指定时间触发，即使设备在Doze Mode也会唤醒
        long triggerTime = SystemClock.elapsedRealtime() + UPDATE_INTERVAL;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, UPDATE_INTERVAL, pendingIntent);
    }

    private void cancelAlarmManager(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, HitokotoWidget.class);
        intent.setAction(ACTION_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
    }
}
