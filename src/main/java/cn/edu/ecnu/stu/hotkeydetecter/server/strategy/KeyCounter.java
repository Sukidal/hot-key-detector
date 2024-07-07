package cn.edu.ecnu.stu.hotkeydetecter.server.strategy;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.function.BiFunction;

@Component
public class KeyCounter {

    public int numProcessors = Runtime.getRuntime().availableProcessors();

    public ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            numProcessors / 2 + 1,
            numProcessors / 2 + 1,
            1,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    public static ConcurrentHashMap<Long, Long> countMap = new ConcurrentHashMap<>();

    public KVEventTranslator eventTranslator = new KVEventTranslator();

    public Disruptor<KVEvent> disruptor;

    EventFactory<KVEvent> eventEventFactory;

    RingBuffer<KVEvent> ringBuffer;

    @PostConstruct
    public void init() {
        //2.创建Event工厂
        eventEventFactory = new KVEventFactory();
        //3.设置ringBuffer大小
        int ringBufferSize = 1024 * 1024;
        //4.创建Disruptor，单生产者模式，消费者等待策略为YieldingWaitStrategy
        disruptor = new Disruptor<>(eventEventFactory, ringBufferSize, threadPool, ProducerType.SINGLE,new YieldingWaitStrategy());
        //5.注册消费者
        disruptor.handleEventsWith(new KVEventHandler());
        //6.启动Disruptor
        disruptor.start();
        //7.创建RingBuffer容器
        ringBuffer = disruptor.getRingBuffer();
    }

    public void countKey(long key) {
        ringBuffer.publishEvent(eventTranslator, key);
    }

    public static class KVEventHandler implements EventHandler<KVEvent> {

        @Override
        public void onEvent(KVEvent kvEvent, long l, boolean b) throws Exception {
            countMap.compute(kvEvent.getKey(), new BiFunction<Long, Long, Long>() {
                @Override
                public Long apply(Long key, Long value) {
                    return value == null ? 1 : value + 1;
                }
            });
        }

    }
}
