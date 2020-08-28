package com.example.demo.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private static RedisTemplate redisTemplate;

    // prefix 需要结合业务，如 "bth-" "web-"
    private static final String prefix = "web-";
    private static final long defaultExpireTime = 60 * 60; //seconds

    // to avoid cache avalanche, the default expire time
    // should be a relatively random time range
    private static long genExTime() {
        return (long) (Math.random() * 10 * defaultExpireTime) + 1000;
    }

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static boolean exists(final String k) {
        try {
            return redisTemplate.hasKey(prefix + k);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void del(final String k){
        redisTemplate.delete(prefix + k);
    }

    public static void put(String k, Object v, boolean expire) {
        put(k, v, genExTime(), expire);
    }

    public static void put(String k, Object v) {
        put(k, v, genExTime(), true);
    }

    public static void put(String k, Object v, long expireTime) {
        put(k, v, expireTime, true);
    }

    private static void put(final String k, final Object v, final long expireTime, final boolean expire) {
        redisTemplate.opsForValue().set(prefix + k, v);
        if (expire) redisTemplate.expire(prefix + k, expireTime, TimeUnit.SECONDS);
    }

    public static Long getExpiresIn(final String k) {
        return redisTemplate.getExpire(prefix + k);
    }

    public static Object get(final String k) {
        return k == null ? null : redisTemplate.opsForValue().get(prefix + k);
    }

    //expireTime time uint: second
    public static void resetExpireTime(final String k, final Long expireTime) {
        redisTemplate.expire(prefix + k, expireTime, TimeUnit.SECONDS);
    }

    public static void delGroup(final String group) {
        Set<String> keys = redisTemplate.keys(prefix + group + "*");
        System.out.println(keys);
        redisTemplate.delete(keys);
    }


    public static Boolean hSet(final String k, final String field, final Object v) {
        try {
            redisTemplate.opsForHash().put(prefix + k, field, v);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rPush(final String k, final Object... v) {
        try {
            redisTemplate.opsForList().rightPush(prefix + k, v);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object hGet(final String k, final String field) {
        return redisTemplate.opsForHash().get(prefix + k, field);
    }

    public static void lRemove(final String k,final Object o) {
        redisTemplate.opsForList().remove(prefix + k, 0 , o);
    }

    //0 to -1 represent all values
    public static List<Object> lGetAll(final String k) {
        try {
            return redisTemplate.opsForList().range(prefix + k, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Object> lRange(final String k,final int start,final int stop) {
        try {
            return redisTemplate.opsForList().range(prefix + k, start, stop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Object> hVals(final String k) {
        try {
            return redisTemplate.opsForHash().values(prefix + k);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //this method is only to convert java Date type to TimeStamp type in order to match mysql datetime type
    private static Object formatObj(Object obj) {
        if(obj==null)return null;
        if (obj instanceof Collection) {
            LinkedList<Object> res = new LinkedList<>();
            for (Object o : (Collection) obj) {
                res.add(formatObj(o));
            }
            return res;
        }
        Class<?> clz = obj.getClass();
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clz).getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (!Date.class.equals(pd.getPropertyType())) continue;
                Method readMethod = pd.getReadMethod();
                Method writeMethod = pd.getWriteMethod();
                if (readMethod == null || writeMethod == null) continue;
                Object o = readMethod.invoke(obj);
                if (o instanceof Timestamp || o == null) continue;
                Timestamp tmp = new Timestamp(((Date) o).getTime());
                writeMethod.invoke(obj, tmp);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
