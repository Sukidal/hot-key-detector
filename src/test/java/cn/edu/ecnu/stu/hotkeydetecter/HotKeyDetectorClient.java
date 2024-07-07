package cn.edu.ecnu.stu.hotkeydetecter;

import cn.edu.ecnu.stu.hotkeydetecter.server.store.KVStore;
import cn.edu.ecnu.stu.hotkeydetecter.server.store.SimpleStore;
import cn.edu.ecnu.stu.hotkeydetecter.server.store.StoreManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@SpringBootTest
public class HotKeyDetectorClient {

    @Autowired
    private StoreManager storeManager;

    @Autowired
    private SimpleStore simpleStore;

    private long[] hotKeys;

    private long[] coldKeys;

    private int numKeys = 50000;

    private double hotKeyRatio = 0.01;

    private void generateKeys(long[] arr, Set<Long> set) {
        Random random = new Random();
        for(int i = 0; i < arr.length; i++) {
            long key = random.nextLong();
            if(set.contains(key)) {
                i--;
                continue;
            }
            arr[i] = key;
        }
    }

    @Test
    public void testHotKey() {
        int numHotKey = (int)(numKeys * hotKeyRatio);
        int numColdKey = (int)(numKeys * (1 - hotKeyRatio));
        hotKeys = new long[numHotKey];
        coldKeys = new long[numColdKey];
        HashSet<Long> set = new HashSet<>();
        generateKeys(hotKeys, set);
        generateKeys(coldKeys, set);
        Random random = new Random();
        long key;
        int i;
        long start = System.currentTimeMillis();
        long curSecond = start + 1000;
        long cur;
        KVStore kvStore = simpleStore;
        for(long k = 1; ; k++) {
            i = random.nextInt(100);
            key = i < 80 || numColdKey == 0 ? hotKeys[random.nextInt(numHotKey)] : coldKeys[random.nextInt(numColdKey)];
            int[] value = new int[4];
            for(int j = 0; j < 4; j++)
                value[j] = random.nextInt();
            kvStore.put(key, value);
            kvStore.read(key);
            cur = System.currentTimeMillis();
            if(cur >= curSecond) {
                System.out.printf("截止到第%d秒总负载数：%d\n", (curSecond - start) / 1000, k);
                curSecond += 1000;
            }
        }
    }
}
