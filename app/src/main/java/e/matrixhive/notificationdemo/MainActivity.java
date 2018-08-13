package e.matrixhive.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        Button notiStandard = findViewById(R.id.button_standard_notification);
        Button notiBundled = findViewById(R.id.button_bundled_notification);
        Button notiRemoteInput = findViewById(R.id.button_remote_input_notification);
        Button notiCustomContentView = findViewById(R.id.button_custom_content_view_notification);

        notiStandard.setOnClickListener(this);
        notiBundled.setOnClickListener(this);
        notiRemoteInput.setOnClickListener(this);
        notiCustomContentView.setOnClickListener(this);
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

            case R.id.button_remote_input_notification:
                viewRemoteInputNotification();
                break;

            case R.id.button_custom_content_view_notification:
                viewCustomContentView();
                break;
        }
    }

    private void viewCustomContentView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            RemoteViews remoteViews = createRemoteViews(R.layout.notification_custom_content, R.drawable.ic_phonelink_ring_black_24dp,
                    "Custom notification", "This is a custom layout",
                    R.drawable.ic_priority_high_black_24dp);
            Notification.Builder builder = createCustomNotificationBuilder();
            builder.setCustomContentView(remoteViews).setStyle(new Notification.DecoratedCustomViewStyle());
            showNotification(builder, 0);
        }
    }

    private Notification.Builder createCustomNotificationBuilder() {
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_phonelink_ring_black_24dp)
                .setAutoCancel(true);
    }

    private RemoteViews createRemoteViews( int layout, int iconResource,
                                           String title, String message, int imageResource) {

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), layout);
        remoteViews.setImageViewResource(R.id.image_icon, iconResource);
        remoteViews.setTextViewText(R.id.text_title, title);
        remoteViews.setTextViewText(R.id.text_message, message);
        remoteViews.setImageViewResource(R.id.image_end, imageResource);
        return remoteViews;
    }

    /**
     * show standard
     */
    private void viewStandardNotification() {
        Notification.Builder notification = createNotificationBuider(
                "Standard Notification", "This is just a standard notification!");
        showNotification(notification, 0);
    }

    /**
     * show bundle
     */
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

            Notification.Builder first = createNotificationBuider("First notification", "This is the first bundled notification");
            first.setGroupSummary(true).setGroup(KEY_NOTIFICATION_GROUP);

            Notification.Builder second = createNotificationBuider("Second notification", "Here's the second one");
            second.setGroup(KEY_NOTIFICATION_GROUP);

            Notification.Builder third = createNotificationBuider("Third notification", "And another for luck!");
            third.setGroup(KEY_NOTIFICATION_GROUP);
            third.addAction(replyAction);
            third.addAction(archiveAction);

            Notification.Builder fourth = createNotificationBuider("Fourth notification", "This one sin't a part of our group");
            fourth.setGroup(KEY_NOTIFICATION_GROUP);

            showNotification(first, 0);
            showNotification(second, 1);
            showNotification(third, 2);
            showNotification(fourth, 3);
        }
    }

    /**
     * Show direct reply
     */
    private void viewRemoteInputNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(this.getString(R.string.app_name))
                    .build();

            PendingIntent replyIntent = PendingIntent.getActivity(this,
                    REPLY_INTENT_ID,
                    getMessageReplyIntent(LABEL_REPLY),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent archiveIntent = PendingIntent.getActivity(this,
                    ARCHIVE_INTENT_ID,
                    getMessageReplyIntent(LABEL_ARCHIVE),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Action replyAction =
                    new Notification.Action.Builder(android.R.drawable.sym_def_app_icon,
                            LABEL_REPLY, replyIntent)
                            .addRemoteInput(remoteInput)
                            .build();

            Notification.Action archiveAction =
                    new Notification.Action.Builder(android.R.drawable.sym_def_app_icon,
                            LABEL_ARCHIVE, archiveIntent)
                            .build();

            Notification.Builder builder =
                    createNotificationBuider("Remote input", "Try typing some text!");
            builder.addAction(replyAction);
            builder.addAction(archiveAction);

            showNotification(builder, REMOTE_INPUT_ID);
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
        if (mNotificationManager!=null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationManager.notify(id, notification.build());
        }
    }

    private Notification.Builder createNotificationBuider(String title,String message) {
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);
    }
}
