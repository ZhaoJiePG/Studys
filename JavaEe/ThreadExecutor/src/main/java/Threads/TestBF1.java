package Threads;

/**
 * Created by ZJ on 2021/1/25
 * comment:
 */
public class TestBF1 implements Runnable{
    //票数
    private int ticketNums = 10;

    public void run() {
        while (true){
            if(ticketNums <=0){
                System.out.println("票已卖完");
                break;
            }
            //延时
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"--->拿到了第"+ticketNums--+"票");
        }

    }

    public static void main(String[] args) {
        TestBF1 bf = new TestBF1();

        new Thread(bf,"小明").start();
        new Thread(bf,"老师").start();
        new Thread(bf,"黄牛党").start();
    }
}
