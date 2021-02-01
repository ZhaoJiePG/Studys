package Threads;

/**
 * Created by ZJ on 2021/1/25
 * comment:建议线程正常停止，不建议死循环，使用外部标志位，不使用stop等过时方法
 */
public class TestStop implements Runnable{

    //1.设置一个标识位
    boolean flag;

    @Override
    public void run() {

    }

    public static void main(String[] args) {

    }
}
