package com.luxuryadmin.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 实体copy数据工具类
 */
public class KKBeanUtil extends BeanUtil {
    public static <T> List<T> copyList(Class<T> dest, List orig) {
        try {
            List<T> resultList = new ArrayList();
            if (orig != null && orig.size() > 0) {
                Iterator var3 = orig.iterator();

                while (var3.hasNext()) {
                    Object o = var3.next();
                    T destObject = dest.newInstance();
                    if (o != null) {
                        copyProperties(o, destObject, CopyOptions.create().ignoreError());
                        resultList.add(destObject);
                    }
                }

                return resultList;
            } else {
                return resultList;
            }
        } catch (Exception var6) {
            return null;
        }
    }

    public static <T> T copy(Class<T> destClass, Object origin) {
        try {
            T dest = destClass.newInstance();
            copyProperties(origin, dest, CopyOptions.create().ignoreError());
            return dest;
        } catch (Exception e) {
            return null;
        }
    }
}
