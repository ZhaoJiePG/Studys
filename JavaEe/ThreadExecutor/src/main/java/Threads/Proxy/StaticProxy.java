package Threads.Proxy;

/**
 * Created by ZJ on 2021/1/25
 * comment:静态代理
 * 真实对象和代理对象 都要实现同一个接口
 * 代理对象要代理真实角色
 *
 * 好处：代理对象可以做很多真实对象做不了的事情
 *      真实对象只做自己的事情
 */
public class StaticProxy {

    public static void main(String[] args) {

        new Thread( () -> System.out.println("我爱你")).start();

        new WeddingCompany(new You());

        WeddingCompany weddingCompany = new WeddingCompany(new You());
        weddingCompany.HappyMarry();
    }

}

interface Marry{
    //
    void HappyMarry();
}

class You implements Marry{
    public void HappyMarry() {
        System.out.println("你结婚了，开心");
    }
}

//代理角色
class  WeddingCompany implements Marry{

    private Marry target;

    public WeddingCompany(Marry target){
        this.target = target;
    }

    public void HappyMarry() {
        before();
        this.target.HappyMarry();
        after();
    }

    private void after() {
        System.out.println("结婚之后，收尾款");
    }

    private void before() {
        System.out.println("结婚之前，布置");
    }
}
