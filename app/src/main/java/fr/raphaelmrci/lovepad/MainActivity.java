package fr.raphaelmrci.lovepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    final String TAG = "MainActivity";

    Button submitBtn, send0, send1, send2, send3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sharedPref = getSharedPreferences("phoneNums", MODE_PRIVATE);

        submitBtn = findViewById(R.id.submit);

        send0 = findViewById(R.id.button0);
        send1 = findViewById(R.id.button1);
        send2 = findViewById(R.id.button2);
        send3 = findViewById(R.id.button3);

        send0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(sharedPref.getString("phone0", ""));
            }
        });

        send1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(sharedPref.getString("phone1", ""));
            }
        });

        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(sharedPref.getString("phone2", ""));
            }
        });

        send3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(sharedPref.getString("phone3", ""));
            }
        });






        TextView phone0 = findViewById(R.id.phone0);
        TextView phone1 = findViewById(R.id.phone1);
        TextView phone2 = findViewById(R.id.phone2);
        TextView phone3 = findViewById(R.id.phone3);

        phone0.setText(sharedPref.getString("phone0", ""));
        phone1.setText(sharedPref.getString("phone1", ""));
        phone2.setText(sharedPref.getString("phone2", ""));
        phone3.setText(sharedPref.getString("phone3", ""));


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "token: "+ token;
                        Log.d("---> LovePad", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });




        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPref.edit()
                        .putString("phone0", phone0.getText().toString())
                        .putString("phone1", phone1.getText().toString())
                        .putString("phone2", phone2.getText().toString())
                        .putString("phone3", phone3.getText().toString())
                        .apply();


                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Dev_test")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Le service est lancé")
                        .setContentText("Le service de recherche de SMS est bien démarré.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);



            }
        });
    }

    private void sendSMS(String phoneNo) {

        final String msg = "Le message du lovePad";




        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}