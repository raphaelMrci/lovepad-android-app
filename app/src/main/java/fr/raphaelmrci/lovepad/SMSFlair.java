package fr.raphaelmrci.lovepad;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class SMSFlair extends FirebaseMessagingService {

    SharedPreferences sharedPref;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (data.size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Log.d(TAG, "Phone_ID: " + data.get("phone_id"));
            sendSMS(getPhoneNoById(Objects.requireNonNull(data.get("phone_id"))));

            sharedPref = getSharedPreferences("phoneNums", MODE_PRIVATE);
            Notifier();

        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Developer test";
        String description = "The developer test notification channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Dev_test", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void Notifier() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Dev_test")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Le service est lancé")
                .setContentText("Le service de recherche de SMS est bien démarré.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, builder.build());
    }

    private String getPhoneNoById(String id){
        sharedPref = getSharedPreferences("phoneNums", MODE_PRIVATE);
        return sharedPref.getString("phone"+id, "");
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    private void sendSMS(String phoneNo) {

        final String msg = getEmojiByUnicode(0x2764);

        Log.d("Sender", phoneNo);




        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            //Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(),ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
