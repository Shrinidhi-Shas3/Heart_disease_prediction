/* JSON API for android appliation */
package com.heart.prediction.admin.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;

public class RestAPI {
    private final String urlString = "http://aheartrandom.hostoise.com/Handler1.ashx";

    private static String convertStreamToUTF8String(InputStream stream) throws IOException {
        String result = "";
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[4096];
            int readedChars = 0;
            while (readedChars != -1) {
                readedChars = reader.read(buffer);
                if (readedChars > 0)
                    sb.append(buffer, 0, readedChars);
            }
            result = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private String load(String contents) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(60000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream());
        w.write(contents);
        w.flush();
        InputStream istream = conn.getInputStream();
        String result = convertStreamToUTF8String(istream);
        return result;
    }


    private Object mapObject(Object o) {
        Object finalValue = null;
        if (o.getClass() == String.class) {
            finalValue = o;
        } else if (Number.class.isInstance(o)) {
            finalValue = String.valueOf(o);
        } else if (Date.class.isInstance(o)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", new Locale("en", "USA"));
            finalValue = sdf.format((Date) o);
        } else if (Collection.class.isInstance(o)) {
            Collection<?> col = (Collection<?>) o;
            JSONArray jarray = new JSONArray();
            for (Object item : col) {
                jarray.put(mapObject(item));
            }
            finalValue = jarray;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            Method[] methods = o.getClass().getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass() == o.getClass()
                        && method.getModifiers() == Modifier.PUBLIC
                        && method.getName().startsWith("get")) {
                    String key = method.getName().substring(3);
                    try {
                        Object obj = method.invoke(o, null);
                        Object value = mapObject(obj);
                        map.put(key, value);
                        finalValue = new JSONObject(map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return finalValue;
    }

    public JSONObject ALogin(String adminId, String password) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "ALogin");
        p.put("adminId", mapObject(adminId));
        p.put("password", mapObject(password));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject getDoctors() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "getDoctors");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject AddDoc(String Name, String Address, String cont, String email, String age, String gender, String cate) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "AddDoc");
        p.put("Name", mapObject(Name));
        p.put("Address", mapObject(Address));
        p.put("cont", mapObject(cont));
        p.put("email", mapObject(email));
        p.put("age", mapObject(age));
        p.put("gender", mapObject(gender));
        p.put("cate", mapObject(cate));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateDoc(String did, String Name, String Address, String cont, String email, String age, String gender, String cate) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "UpdateDoc");
        p.put("did", mapObject(did));
        p.put("Name", mapObject(Name));
        p.put("Address", mapObject(Address));
        p.put("cont", mapObject(cont));
        p.put("email", mapObject(email));
        p.put("age", mapObject(age));
        p.put("gender", mapObject(gender));
        p.put("cate", mapObject(cate));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject DelDoc(String did) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "DelDoc");
        p.put("did", mapObject(did));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject getUsers() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "getUsers");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject getFeedback() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "getFeedback");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject getTrainingData() throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "getTrainingData");
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject AddTrainingData(String DName, String Age, String gender, String chestpain, String bloodsugar, String restecg, String exang, String ca, String slope, String thal, String bloodpressure, String cholesterol, String thalach, String oldpeak) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "AddTrainingData");
        p.put("DName", mapObject(DName));
        p.put("Age", mapObject(Age));
        p.put("gender", mapObject(gender));
        p.put("chestpain", mapObject(chestpain));
        p.put("bloodsugar", mapObject(bloodsugar));
        p.put("restecg", mapObject(restecg));
        p.put("exang", mapObject(exang));
        p.put("ca", mapObject(ca));
        p.put("slope", mapObject(slope));
        p.put("thal", mapObject(thal));
        p.put("bloodpressure", mapObject(bloodpressure));
        p.put("cholesterol", mapObject(cholesterol));
        p.put("thalach", mapObject(thalach));
        p.put("oldpeak", mapObject(oldpeak));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject UpdateTrainingData(String tid, String DName, String Age, String gender, String chestpain, String bloodsugar, String restecg, String exang, String ca, String slope, String thal, String bloodpressure, String cholesterol, String thalach, String oldpeak) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "UpdateTrainingData");
        p.put("tid", mapObject(tid));
        p.put("DName", mapObject(DName));
        p.put("Age", mapObject(Age));
        p.put("gender", mapObject(gender));
        p.put("chestpain", mapObject(chestpain));
        p.put("bloodsugar", mapObject(bloodsugar));
        p.put("restecg", mapObject(restecg));
        p.put("exang", mapObject(exang));
        p.put("ca", mapObject(ca));
        p.put("slope", mapObject(slope));
        p.put("thal", mapObject(thal));
        p.put("bloodpressure", mapObject(bloodpressure));
        p.put("cholesterol", mapObject(cholesterol));
        p.put("thalach", mapObject(thalach));
        p.put("oldpeak", mapObject(oldpeak));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject DeleteTraningData(String tid) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "DeleteTraningData");
        p.put("tid", mapObject(tid));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject Register(String name, String gender, String age, String address, String cont, String email, String pass) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "Register");
        p.put("name", mapObject(name));
        p.put("gender", mapObject(gender));
        p.put("age", mapObject(age));
        p.put("address", mapObject(address));
        p.put("cont", mapObject(cont));
        p.put("email", mapObject(email));
        p.put("pass", mapObject(pass));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject PLogin(String email, String pass) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "PLogin");
        p.put("email", mapObject(email));
        p.put("pass", mapObject(pass));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject PgetProfile(String pid) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "PgetProfile");
        p.put("pid", mapObject(pid));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject PUpdateProfile(String id, String name, String gender, String age, String address, String city, String state, String cont, String email) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "PUpdateProfile");
        p.put("id", mapObject(id));
        p.put("name", mapObject(name));
        p.put("gender", mapObject(gender));
        p.put("age", mapObject(age));
        p.put("address", mapObject(address));
        p.put("city", mapObject(city));
        p.put("state", mapObject(state));
        p.put("cont", mapObject(cont));
        p.put("email", mapObject(email));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject PChangePass(String id, String oldpass, String newpass) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "PChangePass");
        p.put("id", mapObject(id));
        p.put("oldpass", mapObject(oldpass));
        p.put("newpass", mapObject(newpass));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject AnalyseHeart(String uid, String chestpain, String bloodsugar, String restecg, String exang, String ca, String slope, String thal, String bloodpressure, String cholesterol, String thalach, String oldpeak) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "AnalyseHeart");
        p.put("uid", mapObject(uid));
        p.put("chestpain", mapObject(chestpain));
        p.put("bloodsugar", mapObject(bloodsugar));
        p.put("restecg", mapObject(restecg));
        p.put("exang", mapObject(exang));
        p.put("ca", mapObject(ca));
        p.put("slope", mapObject(slope));
        p.put("thal", mapObject(thal));
        p.put("bloodpressure", mapObject(bloodpressure));
        p.put("cholesterol", mapObject(cholesterol));
        p.put("thalach", mapObject(thalach));
        p.put("oldpeak", mapObject(oldpeak));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject searchDoctor(String src, String query) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "searchDoctor");
        p.put("src", mapObject(src));
        p.put("query", mapObject(query));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject AddFeedBack(String uid, String feedback, String date, String time) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "AddFeedBack");
        p.put("uid", mapObject(uid));
        p.put("feedback", mapObject(feedback));
        p.put("date", mapObject(date));
        p.put("time", mapObject(time));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

    public JSONObject PgetFeedback(String uid) throws Exception {
        JSONObject result = null;
        JSONObject o = new JSONObject();
        JSONObject p = new JSONObject();
        o.put("interface", "RestAPI");
        o.put("method", "PgetFeedback");
        p.put("uid", mapObject(uid));
        o.put("parameters", p);
        String s = o.toString();
        String r = load(s);
        result = new JSONObject(r);
        return result;
    }

}


