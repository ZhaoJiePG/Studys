package ThreadPool;

import org.springframework.core.NamedThreadLocal;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZJ on 2021/1/14
 * comment:
 */
public class TestThreadPool {
    public static void main(String[] args) {
        TestThreadPool testThreadPool = new TestThreadPool();
        ThreadPoolExecutor threadPoolExecutor = testThreadPool.buildThreadPoolExecutor();

    }

    private static ThreadPoolExecutor buildThreadPoolExecutor(){
        return new ThreadPoolExecutor(10,
                30,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                (ThreadFactory) new NamedThreadLocal("测试线程池"));
    }

}
