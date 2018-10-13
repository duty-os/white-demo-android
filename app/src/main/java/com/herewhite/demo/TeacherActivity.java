package com.herewhite.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.herewhite.sdk.domain.AkkoEvent;
import com.herewhite.sdk.domain.Appliance;
import com.herewhite.sdk.domain.BroadcastState;
import com.herewhite.sdk.domain.DeviceType;
import com.herewhite.sdk.domain.EventEntry;
import com.herewhite.sdk.domain.EventListener;
import com.herewhite.sdk.domain.GlobalState;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.PptPage;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.herewhite.demo.MainActivity.EVENT_NAME;

public class TeacherActivity extends AppCompatActivity {

    WhiteBroadView whiteBroadView;
    Gson gson = new Gson();
    DemoAPI demoAPI = new DemoAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        final EditText uuidView = (EditText) findViewById(R.id.uuidView);
        whiteBroadView = (WhiteBroadView) findViewById(R.id.white);
        demoAPI.createRoom("unknow", 100, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject room = gson.fromJson(response.body().string(), JsonObject.class);
                final String uuid = room.getAsJsonObject("msg").getAsJsonObject("room").get("uuid").getAsString();
                String roomToken = room.getAsJsonObject("msg").get("roomToken").getAsString();
                Log.i("white", uuid + "|" + roomToken);
                uuidView.post(new Runnable() {
                    @Override
                    public void run() {
                        uuidView.setText(uuid);
                    }
                });

                joinRoom(uuid, roomToken);
            }
        });


    }

    private void bindButton(final Room room, final String uuid) {
        room.addMagixEventListener(EVENT_NAME, new EventListener() {
            @Override
            public void onEvent(EventEntry eventEntry) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("test", "js");
                room.dispatchMagixEvent(new AkkoEvent("event2", payload));
                showToast(gson.toJson(eventEntry));
            }
        });
        room.addMagixEventListener("event2", new EventListener() {
            @Override
            public void onEvent(EventEntry eventEntry) {
                showToast(gson.toJson(eventEntry));
            }
        });
        findViewById(R.id.pencil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberState memberState = new MemberState();
                memberState.setCurrentApplianceName(Appliance.PENCIL);
                room.setMemberState(memberState);
            }
        });
        findViewById(R.id.selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberState memberState = new MemberState();
                memberState.setCurrentApplianceName(Appliance.SELECTOR);
                room.setMemberState(memberState);
            }
        });
        findViewById(R.id.rectangle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberState memberState = new MemberState();
                memberState.setCurrentApplianceName(Appliance.RECTANGLE);
                room.setMemberState(memberState);
            }
        });
        findViewById(R.id.eraser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberState memberState = new MemberState();
                memberState.setCurrentApplianceName(Appliance.ERASER);
                room.setMemberState(memberState);
            }
        });
        findViewById(R.id.event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("test", "js");
                Map<String, Object> childPayload = new HashMap<>();
                childPayload.put("id", 2222);
                childPayload.put("ddd", "ddd");
                payload.put("child", childPayload);
                room.dispatchMagixEvent(new AkkoEvent(EVENT_NAME, payload));
            }
        });
        findViewById(R.id.copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("room", uuid);
                cm.setPrimaryClip(mClipData);
            }
        });
    }

    private void joinRoom(final String uuid, String roomToken) {
        WhiteSdk whiteSdk = new WhiteSdk(
                whiteBroadView,
                TeacherActivity.this,
                new WhiteSdkConfiguration(DeviceType.touch, 10, 0.1));
        whiteSdk.addRoomCallbacks(new AbstractRoomCallbacks() {
            @Override
            public void onPhaseChanged(RoomPhase phase) {
                showToast(phase.name());
                // handle room phase
            }

            @Override
            public void onRoomStateChanged(RoomState modifyState) {
//                showToast(gson.toJson(modifyState));
            }
        });
        whiteSdk.joinRoom(new RoomParams(uuid, roomToken), new Promise<Room>() {
            @Override
            public void then(Room room) {

                bindButton(room, uuid);
//                GlobalState globalState = new GlobalState();
//                globalState.setCurrentSceneIndex(1);
//                room.setGlobalState(globalState);
//
//                room.pushPptPages(new PptPage[]{
//                        new PptPage("https://white-pan.oss-cn-shanghai.aliyuncs.com/101/image/image.png", 600d, 600d),
//                });
//
//
//                room.getBroadcastState(new Promise<BroadcastState>() {
//                    @Override
//                    public void then(BroadcastState broadcastState) {
//                        showToast(broadcastState.getMode());
//                    }
//
//                    @Override
//                    public void catchEx(SDKError t) {
//                        showToast(t.getMessage());
//                    }
//                });

            }

            @Override
            public void catchEx(SDKError t) {
                showToast(t.getMessage());
            }
        });
    }

    void showToast(Object o) {
        Toast.makeText(this, o.toString(), Toast.LENGTH_SHORT).show();
    }


}
