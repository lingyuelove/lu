package com.luxuryadmin.util;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class XMLUtil {

    public static String mapToXml(Map<String, Object> map) {
        SortedMap<String, Object> sort = new TreeMap<String, Object>(map);
        StringBuffer sb = new StringBuffer("<xml>");
        Iterator iterator = sort.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = (String) iterator.next();
            Object value = sort.get(key);
            sb.append("<" + key + ">");
            sb.append(value);
            sb.append("</" + key + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
}
