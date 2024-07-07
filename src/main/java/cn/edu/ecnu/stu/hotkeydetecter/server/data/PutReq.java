package cn.edu.ecnu.stu.hotkeydetecter.server.data;

public class PutReq {

    private long key;

    private int[] value;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
        this.value = value;
    }
}
