package utils;

import com.google.gson.Gson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/14 上午10:17
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static void echoJson(Object obj) {
        System.out.println(toJson(obj));
    }

}
