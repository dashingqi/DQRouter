package com.dashingqi.router.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhangqi
 * @desc :
 * @time : 2021/9/19 09:19
 */
public class RouterMappings {

    public Map<String, String> getMapping() {
        HashMap<String, String> mapping = new HashMap<>();
        mapping.putAll(RouterMapping_1.getMapping());
        mapping.putAll(RouterMapping_2.getMapping());
        return mapping;
    }
}
