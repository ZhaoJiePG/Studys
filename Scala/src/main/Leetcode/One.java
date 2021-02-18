package Leetcode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZJ on 2020/12/14
 * comment:
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * 示例:
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]

 */
public class One {
    public int[] twoSum(int[] nums,int target){
        int len = nums.length-1;
        int[] res = new int[2];
        for(int i=0;i<=len;i++){
            for (int j=i;j<=len;j++){
                if(nums[i]+nums[j]==target){
                    res = new int[]{i,j};
                    break;
                }
                else{
                    continue;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        One one = new One();
        int[] res = one.twoSum(new int[]{2, 7, 11, 15}, 9);
        for(int item:res){
            System.out.println("结果为"+item);
        }
    }

}
