package Threads;

/**
 * Created by ZJ on 2021/1/25
 * comment:lambda表达式
 */
public class TestLambda {
    //3.静态内部类
    static class Like2 implements ILike{
        @Override
        public void lambda() {
            System.out.println("=========2");
        }
    }

    public static void main(String[] args) {
        //4.局部内部类
        class Like3 implements ILike{
            @Override
            public void lambda() {
                System.out.println("=========3");
            }
        }

        ILike iLike = new Like();
        iLike.lambda();

        new Like2().lambda();

        new Like3().lambda();

        //5.匿名内部类
        new ILike() {
            @Override
            public void lambda() {
                System.out.println("=========4");
            }
        }.lambda();

        //6.lambda表达式
        ILike like = new Like();
        like = ()-> System.out.println("=========5");
        like.lambda();

    }
}

//1.定以一个函数是接口
interface ILike{
    void lambda();
}

//2.实现类
class Like implements ILike{
    @Override
    public void lambda() {
        System.out.println("=========1");
    }
}