package com.heart.prediction.user.webservices;

import org.json.JSONObject;

public class JSONParse {

    public String JSONParse(JSONObject jsonObject) {
        try {
            return jsonObject.getString("Value");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
