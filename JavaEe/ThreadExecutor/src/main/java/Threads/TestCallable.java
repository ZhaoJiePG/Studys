package Threads;

import java.util.concurrent.*;

/**
 * Created by ZJ on 2021/1/25
 * comment:线程创建三：实现callable接口
 * 可以定以返回值
 * 可以抛出异常
 */
public class TestCallable implements Callable<Boolean> {
    public static void main(String[] args) {
        TestCallable t1 = new TestCallable();
        TestCallable t2 = new TestCallable();

        //创建执行服务
        ExecutorService service = Executors.newFixedThreadPool(3);
        //提交执行
        Future<Boolean> r1 = service.submit(t1);
        Future<Boolean> r2 = service.submit(t1);
        //获取结果
        try {
            Boolean res1 = r1.get();
            Boolean res2 = r2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //关闭服务
        service.shutdownNow();

        for (int i=1;i<=1000;i++) {
            System.out.println(Thread.currentThread().getName()+"==学习多线程-----"+i);
        }
    }

    public Boolean call() throws Exception {
        //run方法线程体
        for (int i=1;i<=1000;i++){
            System.out.println(Thread.currentThread().getName()+"==我再看代码------"+i);
        }
        return true;
    }
}
