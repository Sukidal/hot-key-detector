package cn.edu.ecnu.stu.hotkeydetecter.server.strategy;

import cn.edu.ecnu.stu.hotkeydetecter.server.store.StoreManager;
import cn.edu.ecnu.stu.hotkeydetecter.server.store.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Component
public class HotKeyDetector {

    @Autowired
    public StoreManager storeManager;

    private ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(1);

    public double threshold = 12.0 / 7;

    @PostConstruct
    public void init() {
        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                long max = Integer.MIN_VALUE;
                long min = Integer.MAX_VALUE;
                for (Map.Entry<Long, int[]> entry : storeManager.bigMap.entrySet()) {
                    ConcurrentHashMap.MapEntry entry1 = (ConcurrentHashMap.MapEntry) entry;
                    long count = entry1.getCount();
                    if(max < count)
                        max = count;
                    if(min > count)
                        min = count;
                }
                if(max == 0)
                    return;
                if((double)max / min <= threshold) {
                    storeManager.smallMapValid = false;
                    return;
                }
                refreshBigMap();
                setNewSmallMap(max, min);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void refreshBigMap() {
        storeManager.smallMap.forEach(new BiConsumer<Long, int[]>() {
            @Override
            public void accept(Long aLong, int[] ints) {
                storeManager.bigMap.put(aLong, ints);
            }
        });
    }

    public void setNewSmallMap(long max, long min) {
        ConcurrentHashMap<Long, int[]> newSmallMap = new ConcurrentHashMap<>();
        for (Map.Entry<Long, int[]> entry : storeManager.bigMap.entrySet()) {
            ConcurrentHashMap.MapEntry entry1 = (ConcurrentHashMap.MapEntry) entry;
            long count = entry1.getCount();
            if(max - count < count - min) {
                newSmallMap.put(entry.getKey(), storeManager.getFromBigMap(entry.getKey()));
            }
        }
        storeManager.smallMap = newSmallMap;
        storeManager.smallMapValid = true;
    }
}
