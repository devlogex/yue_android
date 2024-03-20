package com.devlogex.yue.android.serializers;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSerializer {
    private String id;
    private String name;
    private String email;

    public UserSerializer(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static UserSerializer fromJson(String jsonStr) {
        String id = null;
        String name = null;
        String email = null;
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            return null;
        }
        try {
            id = json.getString("id");
        } catch (Exception e) {
        }
        try {
            name = json.getString("name");
        } catch (Exception e) {
        }
        try {
            email = json.getString("email");
        } catch (Exception e) {
        }
        return new UserSerializer(id, name, email);
    }
}
