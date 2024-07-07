package cn.edu.ecnu.stu.hotkeydetecter.server.strategy;

import com.lmax.disruptor.EventTranslatorOneArg;

public class KVEventTranslator implements EventTranslatorOneArg<KVEvent, Long> {
    @Override
    public void translateTo(KVEvent kvEvent, long l, Long aLong) {
        kvEvent.setKey(aLong);
    }
}
