package cn.edu.ecnu.stu.hotkeydetecter.server.strategy;

import com.lmax.disruptor.EventFactory;

public class KVEventFactory implements EventFactory<KVEvent> {
    @Override
    public KVEvent newInstance() {
        return new KVEvent();
    }
}
