package com.example.chargingPileSystem.redis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ObjectInputStream;
import java.nio.charset.Charset;


public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }

//        byte[] bytes = JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
//        return bytes;
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);

    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
         if (bytes == null || bytes.length <= 0) {
            return null;
        }
//        String str = new String(bytes, DEFAULT_CHARSET);
//        return JSON.parseObject(str, clazz);
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
    }
}
