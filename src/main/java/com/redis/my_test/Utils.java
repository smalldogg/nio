package com.redis.my_test;

/**
 * @author wangyh
 * @create 2020-09-03 11:01
 */

import com.google.common.collect.Maps;
import io.netty.util.internal.ObjectUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import com.google.common.base.CaseFormat;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class Utils {

    static {
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ByteConverter(null), Byte.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
    }

    public static Map<String, String> objectToHash(Object obj) {
        try {
            Map<String, String> map = Maps.newHashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (!property.getName().equals("class")) {
                    if (property.getReadMethod().invoke(obj) != null) {
                        map.put(property.getName(), "" + property.getReadMethod().invoke(obj));
                    }
                }
            }
            return map;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void hashToObject(Map<?, ?> map, Object obj) {
        try {
            BeanUtils.populate(obj, (Map) map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T hashToObject(Map<?, ?> map, Class c) {
        if (map == null) {
            return null;
        }
        try {
            Object obj = c.newInstance();
            hashToObject(map, obj);
            return (T) obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}