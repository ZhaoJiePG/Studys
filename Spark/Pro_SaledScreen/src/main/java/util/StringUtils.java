package util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StringUtils {

    public static String trimComma(String input) {
        if (input.startsWith(",")) input = input.substring(1);
        if (input.endsWith(",")) input.substring(0, input.length() - 1);
        return input;
    }

    /**
     * 根据key去除JSON参数对象中的值
     *
     * @param jsonObject
     * @param keyField
     * @return
     */
    public static String getParamFromJSON(JSONObject jsonObject, String keyField) {
        JSONArray jsonArray = jsonObject.getJSONArray(keyField);
        if (null == jsonArray || jsonArray.size() == 0) return null;
        return jsonArray.get(0).toString();

    }

}
