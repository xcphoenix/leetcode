package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

import java.util.Arrays;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/20 上午10:44
 */
public class MaximumProductOfThreeNumbers {

    @Test
    void testSolution() {
        JsonUtils.echoJson(maximumProduct(
                new int[]{
                        -2, -100, -98, -1, 2, 3, 4,
                }
        ));
    }

    /*
     * 三个数：直接乘
     * n个数：
     *  - 如果都为负数，选择最大的三个数
     *  - 如果有一个负数，在n大于3的情况下，选最大的三个数字
     *  - 如果 >2 个负数 两个最小与最大的正数乘，与最大的三个正数比较
     */

    public int maximumProduct(int[] nums) {
        if (nums.length <= 3) {
            return multiplicity(nums);
        }
        int[] maxVal = new int[3], minVal = new int[3];
        int negativeNum = topK(nums, maxVal, minVal);

        if (negativeNum == nums.length || negativeNum == 1) {
            return multiplicity(maxVal);
        }
        return Math.max(multiplicity(maxVal), maxVal[0] * minVal[0] * minVal[1]);
    }

    private int multiplicity(int[] nums) {
        return nums[0] * nums[1] * nums[2];
    }

    private int topK(int[] arr, int[] maxVal, int[] minVal) {
        int negativeNum = 0, j;
        Arrays.fill(maxVal, Integer.MIN_VALUE);
        Arrays.fill(minVal, Integer.MAX_VALUE);
        for (int num : arr) {
            if (num < 0) {
                negativeNum++;
            }
            if (num > maxVal[2]) {
                j = 1;
                for (; j >= 0 && maxVal[j] < num; j--) {
                    maxVal[j + 1] = maxVal[j];
                }
                maxVal[j + 1] = num;
            }
            if (num < minVal[2]) {
                j = 1;
                for (; j >= 0 && minVal[j] > num; j--) {
                    minVal[j + 1] = minVal[j];
                }
                minVal[j + 1] = num;
            }
        }
        return negativeNum;
    }

}
