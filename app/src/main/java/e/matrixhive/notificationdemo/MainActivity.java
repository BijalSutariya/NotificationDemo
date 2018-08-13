package e.matrixhive.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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
        Button notiCustomContentBigView = findViewById(R.id.button_custom_content_big_view_notification);
        Button notiCustomBigNormalContentView = findViewById(R.id.button_custom_normal_and_big_content_views_notification);
        Button notiCustomMediaContentView = findViewById(R.id.button_custom_media_content_view_notification);
        Button notiHeadsUp = findViewById(R.id.button_heads_up_notification);
        Button notiCustomHeadsUp = findViewById(R.id.button_custom_layout_heads_up_notification);

        notiStandard.setOnClickListener(this);
        notiBundled.setOnClickListener(this);
        notiRemoteInput.setOnClickListener(this);
        notiCustomContentView.setOnClickListener(this);
        notiCustomContentBigView.setOnClickListener(this);
        notiCustomBigNormalContentView.setOnClickListener(this);
        notiCustomMediaContentView.setOnClickListener(this);
        notiHeadsUp.setOnClickListener(this);
        notiCustomHeadsUp.setOnClickListener(this);

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

            case R.id.button_custom_content_big_view_notification:
                viewCustomBigContentView();
                break;

            case R.id.button_custom_normal_and_big_content_views_notification:
                viewCustomBothContentView();
                break;

            case R.id.button_custom_media_content_view_notification:
                viewCustomMediaContentView();
                break;

            case R.id.button_heads_up_notification:
                viewHeadsUp();
                break;

            case R.id.button_custom_layout_heads_up_notification:
                viewCustomHeadsUp();
                break;
        }
    }

    /**
     * show standard
     */
    private void viewStandardNotification() {
        NotificationCompat.Builder notification = createNotificationBuider(
                "Standard Notification", "This is just a standard notification!");
        showNotification(notification.build(), 0);
    }

    /**
     * show bundle
     */
    private void viewBundleNotification() {
            PendingIntent replyIntent = PendingIntent.getActivity(this,
                    REPLY_INTENT_ID,
                    getMessageReplyIntent(LABEL_REPLY),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent archiveIntent = PendingIntent.getActivity(this,
                    ARCHIVE_INTENT_ID,
                    getMessageReplyIntent(LABEL_ARCHIVE),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                    LABEL_REPLY, replyIntent)
                    .build();

            NotificationCompat.Action archiveAction =
                    new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                            LABEL_ARCHIVE, archiveIntent)
                            .build();

            NotificationCompat.Builder first = createNotificationBuider("First notification", "This is the first bundled notification");
            first.setGroupSummary(true).setGroup(KEY_NOTIFICATION_GROUP);

            NotificationCompat.Builder second = createNotificationBuider("Second notification", "Here's the second one");
            second.setGroup(KEY_NOTIFICATION_GROUP);

            NotificationCompat.Builder third = createNotificationBuider("Third notification", "And another for luck!");
            third.setGroup(KEY_NOTIFICATION_GROUP);
            third.addAction(replyAction);
            third.addAction(archiveAction);

            NotificationCompat.Builder fourth = createNotificationBuider("Fourth notification", "This one sin't a part of our group");
            fourth.setGroup(KEY_NOTIFICATION_GROUP);

            showNotification(first.build(), 0);
            showNotification(second.build(), 1);
            showNotification(third.build(), 2);
            showNotification(fourth.build(), 3);

    }

    /**
     * Show direct reply
     */
    private void viewRemoteInputNotification() {
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

            NotificationCompat.Action replyAction =
                    new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                            LABEL_REPLY, replyIntent)
                            .addRemoteInput(remoteInput)
                            .build();

            NotificationCompat.Action archiveAction =
                    new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                            LABEL_ARCHIVE, archiveIntent)
                            .build();

            NotificationCompat.Builder builder =
                    createNotificationBuider("Remote input", "Try typing some text!");
            builder.addAction(replyAction);
            builder.addAction(archiveAction);

            showNotification(builder.build(), REMOTE_INPUT_ID);

    }

    /**
     * show custom content view
     */
    private void viewCustomContentView() {
        RemoteViews remoteViews = createRemoteViews(R.layout.notification_custom_content, R.drawable.ic_phonelink_ring_black_24dp,
                "Custom notification", "This is a custom layout",R.drawable.ic_priority_high_black_24dp);

        Notification.Builder builder = createCustomNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews)
                    .setStyle(new Notification.DecoratedCustomViewStyle());
            showNotification(builder.build(), 0);
        }
    }

    /**
     * show custome big content view
     */
    private void viewCustomBigContentView() {
        RemoteViews remoteViews = createRemoteViews(
                R.layout.notification_custom_big_content, R.drawable.ic_phonelink_ring_black_24dp,
                "Custom notification", "This one is a little bigger!",
                R.drawable.ic_priority_high_black_24dp);

        Notification.Builder builder = createCustomNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomBigContentView(remoteViews)
                    .setStyle(new Notification.DecoratedCustomViewStyle());
            showNotification(builder.build(), 0);
        }

    }

    /**
     * show big and normal content view
     */
    private void viewCustomBothContentView() {
        RemoteViews remoteViews = createRemoteViews(R.layout.notification_custom_content,
                R.drawable.ic_phonelink_ring_black_24dp, "Custom notification",
                "This is a custom layout", R.drawable.ic_priority_high_black_24dp);

        RemoteViews bigRemoteView = createRemoteViews(R.layout.notification_custom_big_content, R.drawable.ic_phonelink_ring_black_24dp,
                "Custom notification", "This one is a little bigger",
                R.drawable.ic_priority_high_black_24dp);

        Notification.Builder builder = createCustomNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews)
                    .setCustomBigContentView(bigRemoteView)
                    .setStyle(new Notification.DecoratedCustomViewStyle());
            showNotification(builder.build(), 0);
        }
    }

    /**
     * show media content view
     */
    private void viewCustomMediaContentView() {
        RemoteViews remoteViews = createRemoteViews(R.layout.notification_custom_content,
                R.drawable.ic_phonelink_ring_black_24dp, "Custom media notification",
                "This is a custom media layout", R.drawable.ic_play_arrow_black_24dp);

        Notification.Builder builder = createCustomNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews)
                    .setStyle(new Notification.DecoratedMediaCustomViewStyle());
            showNotification(builder.build(), 0);
        }

    }

    /**
     * show heads Up
     */
    private void viewHeadsUp() {
        PendingIntent archiveIntent = PendingIntent.getActivity(this,
                ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, archiveIntent)
                        .build();
        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder notificationBuider = createNotificationBuider( "Heads up!",
                "This is a normal heads up notification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuider.setPriority(Notification.PRIORITY_HIGH).setVibrate(new long[0]);
        }
        notificationBuider.addAction(replyAction);
        notificationBuider.addAction(archiveAction);

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(this,MainActivity.class);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                push, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuider.setFullScreenIntent(fullScreenPendingIntent, true);

        showNotification(notificationBuider.build(), 0);
    }

    /**
     * show custom Heads UP
     */
    private void viewCustomHeadsUp() {
        RemoteViews remoteViews = createRemoteViews(R.layout.notification_custom_content, R.drawable.ic_phonelink_ring_black_24dp,
                "Heads up!", "This is a custom heads-up notification",
                R.drawable.ic_priority_high_black_24dp);

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://www.hitherejoe.com"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification.Builder builder = createCustomNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews)
                    .setStyle(new Notification.DecoratedCustomViewStyle())
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[0])
                    .setContentIntent(contentIntent);

            Intent push = new Intent();
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(this, MainActivity.class);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    push, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setFullScreenIntent(fullScreenPendingIntent, true);
            showNotification(builder.build(), 0);
        }

    }

    private Notification.Builder createCustomNotificationBuilder() {
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_phonelink_ring_black_24dp)
                .setAutoCancel(true);
    }

    private RemoteViews createRemoteViews(int layout, int iconResource,String title, String message, int imageResource) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), layout);
        remoteViews.setImageViewResource(R.id.image_icon, iconResource);
        remoteViews.setTextViewText(R.id.text_title, title);
        remoteViews.setTextViewText(R.id.text_message, message);
        remoteViews.setImageViewResource(R.id.image_end, imageResource);
        return remoteViews;
    }

    private Intent getMessageReplyIntent(String label) {
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra(KEY_PRESSED_ACTION, label);
    }

    private void showNotification(Notification notification, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotificationManager.notify(id, notification);
        }
    }

    private NotificationCompat.Builder createNotificationBuider(String title, String message) {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_background);

        return new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true);
    }
}
