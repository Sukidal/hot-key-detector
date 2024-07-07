package cn.edu.ecnu.stu.hotkeydetecter.server.store;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;


@Repository
public class SimpleStore implements KVStore{

    public ConcurrentHashMap<Long, int[]> map = new ConcurrentHashMap<>();

    public int[] read(long key) {
        return map.get(key);
    }

    public void put(long key, int[] value) {
        map.put(key, value);
    }
}
