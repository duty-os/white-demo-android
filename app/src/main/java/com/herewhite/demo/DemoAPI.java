package com.herewhite.demo;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *
 */

public class DemoAPI {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    // 获取 Token 请参考
    public static final String TOKEN = "(请注册 console.herewhite.com 申请 Token 进行接入)";
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public void createRoom(String name, int limit, Callback callback) {
        Map<String, Object> roomSpec = new HashMap<>();
        roomSpec.put("name", name);
        roomSpec.put("limit", limit);
        RequestBody body = RequestBody.create(JSON, gson.toJson(roomSpec));
        Request request = new Request.Builder()
                .url("https://cloudcapiv3.herewhite.com/room?token=" + TOKEN)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void joinRoom(String uuid, Callback callback) {
        RequestBody body = RequestBody.create(JSON, "{}");
        Request request = new Request.Builder()
                .url("https://cloudcapiv3.herewhite.com/room/join?uuid=" + uuid + "&token=" + TOKEN)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
