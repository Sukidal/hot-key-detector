package cn.edu.ecnu.stu.hotkeydetecter.server.store;

public interface KVStore {

    int[] read(long key);

    void put(long key, int[] value);
}
