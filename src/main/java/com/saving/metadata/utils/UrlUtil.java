package com.saving.metadata.utils;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Mr.Saving
 * @date 2019年4月3日14:56:35
 **/
@Slf4j
public class UrlUtil {
    private static final String INFOSTR = "?";
    private static final String ERRORMSG = "URL工具类错误!";

    private UrlUtil() {
        super();
    }

    public static String getHttpInterface(String path, Map<String, Object> event) {
        BufferedReader in = null;
        StringBuilder result = null;
        try {
            StringBuilder urlEvent = new StringBuilder(INFOSTR);
            for (Map.Entry<String, Object> entry : event.entrySet()) {
                if (INFOSTR.equals(urlEvent.toString())) {
                    urlEvent.append(entry.getKey() + "=" + entry.getValue());
                } else {
                    urlEvent.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
            if (INFOSTR.equals(urlEvent.toString())) {
                urlEvent = new StringBuilder();
            }
            URL url = new URL(path + urlEvent);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestMethod("POST");
            connection.connect();
            result = new StringBuilder();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            log.error(ERRORMSG, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                log.error(ERRORMSG, e2);
            }
        }
        return null;
    }

    public static String getHttpPostInterface(String path, Map<String, Object> event) {
        BufferedReader in = null;
        StringBuilder result;
        PrintWriter out = null;
        HttpURLConnection connection = null;
        try {
            StringBuilder urlEvent = new StringBuilder(INFOSTR);
            for (Map.Entry<String, Object> entry : event.entrySet()) {
                if (entry.getValue() != null) {
                    if (INFOSTR.equals(urlEvent.toString())) {
                        urlEvent.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8.displayName()));
                    } else {
                        urlEvent.append("&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8.displayName()));
                    }
                }

            }
            if (INFOSTR.equals(urlEvent.toString())) {
                urlEvent = new StringBuilder();
            }
            URL url = new URL(path + urlEvent);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out = new PrintWriter(connection.getOutputStream());
            out.flush();
            result = new StringBuilder();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            log.error(ERRORMSG, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                log.error(ERRORMSG, e2);
            }

        }
        return null;
    }


    public static Map<String, Object> doPost(String urlStr, Map<String, Object> params) throws Exception {
        Map map = new HashMap(10);

        // post参数
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (param.getValue() != null) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
        }
        log.info(postData.toString());
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        //开始访问
        HttpURLConnection conn = (HttpURLConnection) (new URL(urlStr)).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0; ) {
            sb.append((char) c);
        }
        in.close();
        conn.disconnect();

        String responseStr = sb.toString();
        log.info("RequestUtils - responseStr <== " + responseStr);
        if (StringUtils.isEmpty(responseStr)) {
            responseStr = "{}";
        }
        int statusCode = conn.getResponseCode();
        log.info("RequestUtils - statusCode <== " + statusCode);
        if (HttpServletResponse.SC_OK == statusCode) {
            JSONObject dataJson = JSONObject.fromObject(responseStr);
            map = new HashMap(dataJson);
        }

        return map;
    }

    /**
     * 将json字符串转为Map结构
     * 如果json复杂，结果可能是map嵌套map
     *
     * @param jsonStr 入参，json格式字符串
     * @return 返回一个map
     */
    public static List<Map<String, Object>> json2Map(String jsonStr) {

        List<Map<String, Object>> tcList = new ArrayList<>();
        if (jsonStr != null && !"".equals(jsonStr)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonStr);

            for (int i = 0; i < jsonArray.size(); i++) {
                tcList.add(jsonStrToMap(jsonArray.getString(i)));
            }
        }
        return tcList;
    }

    public static Map<String, Object> jsonStrToMap(String jsonStr) {
        Map<String, Object> map = new LinkedHashMap<>();
        JSONObject json = JSONObject.fromObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(jsonStrToMap(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    public static Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else if (v instanceof JSONObject) {
                map.put(k.toString(), parseJSON2Map((JSONObject) v));
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }


}
