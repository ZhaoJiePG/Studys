package Threads;

/**
 * Created by ZJ on 2021/1/25
 * comment:实现runnable接口，重写run方法，执行线程丢入runnable接口实现类
 */
public class TestRunnable1 implements Runnable {
    public void run() {
        //run方法线程体
        for (int i=1;i<=1000;i++){
            System.out.println("我再看代码------"+i);
        }
    }

    public static void main(String[] args) {
        TestRunnable1 testRunnable1 = new TestRunnable1();
        //调用start()方法开启
        new Thread(testRunnable1).start();

        for (int i=1;i<=1000;i++) {
            System.out.println("我再学习多线程-----"+i);
        }
    }
}
