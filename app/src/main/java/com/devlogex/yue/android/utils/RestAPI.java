package com.devlogex.yue.android.utils;


import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestAPI {

    public static Response post(String url, String data, Map<String, String> headers) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody payload = RequestBody.create(
                data,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request.Builder request = new Request.Builder()
                .url(url)
                .post(payload);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Response response = client.newCall(request.build()).execute();
        return response;
    }

    public static void aPost(String url, String data, Map<String, String> headers, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder()
                .url(url);
        RequestBody payload = RequestBody.create(null, new byte[0]);
        if (data != null) {
            payload = RequestBody.create(
                    data,
                    MediaType.parse("application/json; charset=utf-8")
            );
        }
        request.post(payload);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (callback == null) {
            callback = new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    // TODO: handle failure
                    System.out.println("call api failed :" + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                }
            };
        }
        client.newCall(request.build()).enqueue(callback);
    }
}
