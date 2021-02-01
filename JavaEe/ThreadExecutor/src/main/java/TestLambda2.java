import org.apache.tomcat.util.descriptor.InputSourceUtil;

/**
 * Created by ZJ on 2021/1/25
 * comment:
 */
public class TestLambda2 {
    static class Love2 implements ILove{

        @Override
        public void Love(int a) {
            System.out.println("i love you");
        }
    }

    public static void main(String[] args) {
        ILove love = new Love();
        love.Love(2);

        //简化1
        ILove love2 = (int a)->{
            System.out.println("i like "+a);
        };
        love2.Love(2);

        //简化2
        love2 = a -> System.out.println("i like "+a);
        love2.Love(520);
    }
}

interface ILove{
    void Love(int a);
}

class Love implements ILove{

    @Override
    public void Love(int a) {
        System.out.println("i love you");
    }
}