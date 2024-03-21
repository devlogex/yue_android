package com.devlogex.yue.android.serializers;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSerializer {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public UserSerializer(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }



    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static UserSerializer fromJson(String jsonStr) {
        String id = null;
        String first_name = null;
        String last_name = null;
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
            first_name = json.getString("first_name");
        } catch (Exception e) {
        }
        try {
            last_name = json.getString("last_name");
        } catch (Exception e) {
        }
        try {
            email = json.getString("email");
        } catch (Exception e) {
        }
        return new UserSerializer(id, first_name, last_name, email);
    }
}
