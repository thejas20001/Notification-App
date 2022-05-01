package com.example.mynotification;

import static com.example.mynotification.model.Constants.TOPIC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotification.api.ApiUtilities;
import com.example.mynotification.model.NotificationData;
import com.example.mynotification.model.PushNotification;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText title,message;
    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        button = findViewById(R.id.button);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        button.setOnClickListener(v ->  {
            String titleText = title.getText().toString();
            String messageText = message.getText().toString();

            if(!titleText.isEmpty() && !messageText.isEmpty()){
                PushNotification notification = new PushNotification(new NotificationData(titleText,messageText),TOPIC);
                sendNotification(notification);
            }

        });

    }

    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}