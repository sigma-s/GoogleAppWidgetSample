package layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.neelabh.appwidgetsample.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String mSharedPrefFile = "com.example.android.appwidgetsample";
    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //read the update count from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(mSharedPrefFile,0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId,0);
        count++;
        //get current date
        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        int[] idArray = new int[]{appWidgetId};
        //create intent to update widget when button is pressed
        Intent intent = new Intent(context,NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idArray);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,appWidgetId,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));
        views.setTextViewText(R.id.appwidget_update, context.getResources().getString(R.string.appwidget_update,count,dateString));
        views.setOnClickPendingIntent(R.id.appwidget_button,pendingIntent);
        //put the count in SharedPreferences
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId,count);
        prefEditor.apply();
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

