package cn.edu.ecnu.stu.hotkeydetecter.server.store;

import cn.edu.ecnu.stu.hotkeydetecter.server.strategy.KeyCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

@Repository
public class StoreManager implements KVStore{

    public volatile ConcurrentHashMap<Long, int[]> smallMap = new ConcurrentHashMap<>();

    public volatile boolean smallMapValid = false;

    public ConcurrentHashMap<Long, int[]> bigMap = new ConcurrentHashMap<>();

    @Autowired
    public KeyCounter keyCounter;

    public int[] read(long key) {
        keyCounter.countKey(key);
        if(smallMapValid) {
            int[] ints = smallMap.get(key);
            if(ints != null)
                return ints;
        }
        return bigMap.get(key);
    }

    public void put(long key, int[] value) {
        if(!smallMapValid) {
            bigMap.put(key, value);
            return;
        }
        smallMap.compute(key, new BiFunction<Long, int[], int[]>() {
            @Override
            public int[] apply(Long aLong, int[] ints) {
                if(ints == null) {
                    bigMap.put(key, value);
                    return null;
                }
                return value;
            }
        });
    }

    public int[] getFromBigMap(long key) {
        return bigMap.get(key);
    }
}
