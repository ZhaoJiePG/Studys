package Expand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Appcmp {


    /**
     * 在Scala中对应Comparable的是Ordered这个特质
     * Comparator Ordering
     * @param args
     */

    public static void main(String[] args) {

        Teachers dd = new Teachers("丁丁", 99);
        Teachers jj = new Teachers("静静", 100);

        List<Teachers> list = new ArrayList<Teachers>();
        list.add(dd);
        list.add(jj);


        // 比较2个老师的颜值 --- 比较器
//        Collections.sort(list, new Comparator<Teachers>() {
//            @Override
//            public int compare(Teachers o1, Teachers o2) {
//                return - (o1.getFaceValue() - o2.getFaceValue());
//            }
//        });


        Collections.sort(list);

        for (Teachers teachers : list) {
            System.out.println("teachers = " + teachers.getName());
        }


    }
}
