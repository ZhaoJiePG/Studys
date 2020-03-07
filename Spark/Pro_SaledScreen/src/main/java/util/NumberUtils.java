package util;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class NumberUtils {

    /**
     * double数字类型格式化
     * @param input  输入小数
     * @param scale 四舍五入位数
     * @return 格式化之后小耍
     */
    public static double formatDouble(double input,int scale){
        BigDecimal bigDecimal = new BigDecimal(input);
        return bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 返回两个整数相除的百分比
     * @param num1
     * @param num2
     * @return
     */
    public static String get2NumPercentage(long num1,long num2){
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return nf.format((float)num1/num2);

    }

    public static void main(String[] args) {
        System.out.println(get2NumPercentage(1L,1L));
    }

}
