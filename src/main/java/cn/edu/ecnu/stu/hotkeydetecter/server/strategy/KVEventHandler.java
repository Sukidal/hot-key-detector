package cn.edu.ecnu.stu.hotkeydetecter.server.strategy;


import com.lmax.disruptor.EventHandler;

public class KVEventHandler implements EventHandler<KVEvent> {

    @Override
    public void onEvent(KVEvent kvEvent, long l, boolean b) throws Exception {

    }

}
