package com.heart.prediction.admin.webservices;

import org.json.JSONObject;

/**
 * Created by Sony on 3/9/2018.
 */

public class JSONParse {

    public String parse(JSONObject json) {
        try
        {
            return json.getString("Value");
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
