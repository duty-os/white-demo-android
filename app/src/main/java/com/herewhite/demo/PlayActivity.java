package com.herewhite.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.herewhite.sdk.AbstractRoomCallbacks;
import com.herewhite.sdk.Logger;
import com.herewhite.sdk.Player;
import com.herewhite.sdk.PlayerEventListener;
import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteBroadView;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.DeviceType;
import com.herewhite.sdk.domain.EventEntry;
import com.herewhite.sdk.domain.EventListener;
import com.herewhite.sdk.domain.PlayerConfiguration;
import com.herewhite.sdk.domain.PlayerPhase;
import com.herewhite.sdk.domain.PlayerState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.UpdateCursor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.herewhite.demo.MainActivity.EVENT_NAME;

public class PlayActivity extends AppCompatActivity {

    WhiteBroadView whiteBroadView;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        final String uuid = intent.getStringExtra("uuid");
        final String m3u8 = intent.getStringExtra("m3u8");

        whiteBroadView = (WhiteBroadView) findViewById(R.id.playWhite);
        WhiteSdk whiteSdk = new WhiteSdk(
                whiteBroadView,
                PlayActivity.this,
                new WhiteSdkConfiguration(DeviceType.touch, 10, 0.1));
        PlayerConfiguration playerConfiguration = new PlayerConfiguration();
        playerConfiguration.setRoom(uuid);
        playerConfiguration.setAudioUrl("https://ohuuyffq2.qnssl.com/98398e2c5a43d74321214984294c157e_60def9bac25e4a378235f6249cae63c1.m3u8");

        whiteSdk.createPlayer(playerConfiguration, new PlayerEventListener() {
            @Override
            public void onPhaseChanged(PlayerPhase playerPhase) {

            }

            @Override
            public void onLoadFirstFrame() {

            }

            @Override
            public void onSliceChanged(String s) {

            }

            @Override
            public void onPlayerStateChanged(PlayerState playerState) {

            }

            @Override
            public void onStoppedWithError(SDKError sdkError) {

            }

            @Override
            public void onScheduleTimeChanged(long l) {

            }

            @Override
            public void onCatchErrorWhenAppendFrame(SDKError sdkError) {

            }

            @Override
            public void onCatchErrorWhenRender(SDKError sdkError) {

            }

            @Override
            public void onCursorViewsUpdate(UpdateCursor updateCursor) {

            }
        }, new Promise<Player>() {
            @Override
            public void then(Player player) {
                player.play();
            }

            @Override
            public void catchEx(SDKError t) {
                Logger.error("create player error, ", t);
            }
        });
    }

    void showToast(Object o) {
        Toast.makeText(this, o.toString(), Toast.LENGTH_SHORT).show();
    }


}
