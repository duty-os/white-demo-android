package com.herewhite.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.herewhite.sdk.AbstractRoomCallbacks;
import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteBroadView;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.DeviceType;
import com.herewhite.sdk.domain.GlobalState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.PptPage;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public final static String EVENT_NAME = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whiteboard);

        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        final EditText uuidInput = (EditText) findViewById(R.id.uuid);
        final EditText m3u8Input = (EditText) findViewById(R.id.m3u8);

        findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = uuidInput.getText().toString();
                if (text.length() == 32) {  //uuid length is 32
                    Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                    intent.putExtra("uuid", text);
                    MainActivity.this.startActivity(intent);
                } else {

                }

            }
        });


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = uuidInput.getText().toString();
                String m3u8 = m3u8Input.getText().toString();
                if (text.length() == 32) {  //uuid length is 32
                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    intent.putExtra("uuid", text);
                    intent.putExtra("m3u8", m3u8);
                    MainActivity.this.startActivity(intent);
                } else {

                }

            }
        });
    }

}
