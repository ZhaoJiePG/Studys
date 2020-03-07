package Methmods;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZJ on 2019-3-11
 * comment:线程池
 */
public class ThreadPool {

    public static void main(String[] args) {

        //创建一个单线程的线程池
        //ExecutorService pool = Executors.newSingleThreadExecutor();

        //创建固定大小的线程池
        //ExecutorService pool = Executors.newFixedThreadPool(5);

        //可缓冲的线程池(可以有多个线程)
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 11; i++) {

            pool.execute(new Runnable() {
                @Override
                public void run() {
                    //获取当前线程的名字
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "is over");
                }
            });

            System.out.println("all job is submit");
            //pool.shutdownNow();

            //期望未来可能执行的
            pool.submit(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

    }
}
