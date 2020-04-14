package com.t3h.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "LANDT1907E";
        //check version >=26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }
        String title = remoteMessage.getData().get("title"); //postman
        String text = remoteMessage.getData().get("body");

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId);
        notification.setContentTitle(title);
        notification.setContentText(text);
        notification.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent(this, MainActivity.class); // activity sẽ vào khi click notify
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setContentIntent(pending);

        if (remoteMessage.getData().containsKey("image")) {
            String image = remoteMessage.getData().get("image");
            try {
                URL url = new URL(image);
                URLConnection connection = url.openConnection();
                Bitmap b = BitmapFactory.decodeStream(connection.getInputStream());
                notification.setLargeIcon(b);

                notification.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(b)
                        .bigLargeIcon(b));
            } catch (Exception e) {
                e.printStackTrace();
//                notification.setSmallIcon(R.mipmap.ic_launcher);
            }
        }
        manager.notify(1234, notification.build());

    }
}
