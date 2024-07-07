package cn.edu.ecnu.stu.hotkeydetecter.server.store;

import cn.edu.ecnu.stu.hotkeydetecter.server.store.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;


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
