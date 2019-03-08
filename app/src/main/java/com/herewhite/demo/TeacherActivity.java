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
import com.herewhite.sdk.domain.DeviceType;
import com.herewhite.sdk.domain.EventEntry;
import com.herewhite.sdk.domain.EventListener;
import com.herewhite.sdk.domain.ImageInformationWithUrl;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.PptPage;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomMouseEvent;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.Scene;
import com.herewhite.sdk.domain.UrlInterrupter;

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
    private EditText scaleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        final EditText uuidView = (EditText) findViewById(R.id.uuidView);
        whiteBroadView = (WhiteBroadView) findViewById(R.id.white);
        scaleText = findViewById(R.id.scale);
        demoAPI.createRoom("unknow", 100, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("white", "create room error ", e);
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

//                GlobalState globalState = new GlobalState();
//                globalState.setCurrentSceneIndex(2);
//                room.setGlobalState(globalState);
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
                new WhiteSdkConfiguration(DeviceType.touch, 10, 0.1)
                // 如果需要拦截并重写 URL ,请实现如下方法, 但请注意不要每次都生成不同的 URL,尽量 cache 结果并请在必要的时候进行更新(如 更新私有 Token)
                , new UrlInterrupter() {
            @Override
            public String urlInterrupter(String s) {
                // 修改资源 URL
                return s;
            }
        }
        );
        whiteSdk.joinRoom(new RoomParams(uuid, roomToken), new AbstractRoomCallbacks() {
            @Override
            public void onPhaseChanged(RoomPhase phase) {
            }

            @Override
            public void onRoomStateChanged(final RoomState modifyState) {
//                if (modifyState.getZoomScale() != null) {
//                    scaleText.setText(String.valueOf(modifyState.getZoomScale().intValue() * 100));
//                }

                scaleText.post(new Runnable() {
                    @Override
                    public void run() {
                        if (modifyState.getZoomScale() != null) {
                            scaleText.setText(String.valueOf((int) (modifyState.getZoomScale() * 100)));
                        }
                    }
                });
//                showToast("scale " + modifyState.getZoomScale());
//                showToast("ppt " + modifyState.getPptImages());

                if (modifyState.getSceneState() != null) {
                    showToast(gson.toJson(modifyState.getSceneState()));
                }
            }

            @Override
            public void onDisconnectWithError(Exception e) {
            }

            @Override
            public void onKickedWithReason(String reason) {
            }
        }, new Promise<Room>() {
            @Override
            public void then(Room room) {

                bindButton(room, uuid);

                room.zoomChange(1.2);

//                room.disableOperations(true); // read only

//                throw new RuntimeException("I throw a biz exception, SDK will be catch it.");
//                GlobalState globalState = new GlobalState();
//                globalState.setCurrentSceneIndex(1);
//                room.setGlobalState(globalState);
//


                room.putScenes("/ppt1", new Scene[]{
                        new Scene("page1", new PptPage("http://oss.tan8.com/yuepuku/69/34654/prev_34654.0.png", 497d, 703d)),
                        new Scene("page2", new PptPage("http://photo.yupoo.com/gwy0606/294324b7eeb7/medish.jpg", 473d, 640d)),
                        new Scene("page3", new PptPage("http://www.77music.com/yuepuku/33/16878/prev_16878.0.png?v=1342110119", 543d, 703d)),
                }, 0);

                room.insertImage(new ImageInformationWithUrl(0d, 0d, 100d, 200d, "http://oss.tan8.com/yuepuku/69/34654/prev_34654.0.png"));




                room.setScenePath("/ppt1/page3");

                room.getScenes(new Promise<Scene[]>() {
                    @Override
                    public void then(Scene[] scenes) {
                        showToast(gson.toJson(scenes));
                    }

                    @Override
                    public void catchEx(SDKError sdkError) {

                    }
                });
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
