package e.matrixhive.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button notiStandard,notiBundled;
    public static final int REPLY_INTENT_ID = 0;
    public static final int ARCHIVE_INTENT_ID = 1;

    public static final int REMOTE_INPUT_ID = 1247;

    public static final String LABEL_REPLY = "Reply";
    public static final String LABEL_ARCHIVE = "Archive";
    public static final String REPLY_ACTION = "com.hitherejoe.notifi.util.ACTION_MESSAGE_REPLY";
    public static final String KEY_PRESSED_ACTION = "KEY_PRESSED_ACTION";
    public static final String KEY_TEXT_REPLY = "KEY_TEXT_REPLY";
    private static final String KEY_NOTIFICATION_GROUP = "KEY_NOTIFICATION_GROUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notiStandard = findViewById(R.id.button_standard_notification);
        notiBundled = findViewById(R.id.button_bundled_notification);

        notiStandard.setOnClickListener(this);
        notiBundled.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_standard_notification:
                viewStandardNotification();
                break;

            case R.id.button_bundled_notification:
                viewBundleNotification();
                break;
        }
    }

    private void viewStandardNotification() {

        Notification.Builder notification = createNotificationBuider(
                "Standard Notification");
        showNotification(notification, 0);
    }

    private void viewBundleNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            PendingIntent archiveIntent = PendingIntent.getActivity(this,
                    ARCHIVE_INTENT_ID,
                    getMessageReplyIntent(LABEL_ARCHIVE),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Action replyAction = new Notification.Action.Builder(android.R.drawable.sym_def_app_icon,
                    LABEL_REPLY, archiveIntent)
                    .build();

            Notification.Action archiveAction =
                    new Notification.Action.Builder(android.R.drawable.sym_def_app_icon,
                            LABEL_ARCHIVE, archiveIntent)
                            .build();

            Notification.Builder first = createNotificationBuider("First notification");
            first.setGroupSummary(true).setGroup(KEY_NOTIFICATION_GROUP);

            Notification.Builder second = createNotificationBuider("Second notification");
            second.setGroup(KEY_NOTIFICATION_GROUP);

            Notification.Builder third = createNotificationBuider("Third notification");
            third.setGroup(KEY_NOTIFICATION_GROUP);
            third.addAction(replyAction);
            third.addAction(archiveAction);

            Notification.Builder fourth = createNotificationBuider( "Fourth notification");
            fourth.setGroup(KEY_NOTIFICATION_GROUP);

            showNotification(first, 0);
            showNotification(second, 1);
            showNotification(third, 2);
            showNotification( fourth, 3);
        }



    }

    private Intent getMessageReplyIntent(String label) {
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra(KEY_PRESSED_ACTION, label);
    }

    private void showNotification(Notification.Builder notification, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationManager.notify(id, notification.build());
        }
    }

    private Notification.Builder createNotificationBuider(String standard_notification) {

        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Notification Demo")
                .setContentText(standard_notification)
                .setAutoCancel(true);
    }
}
