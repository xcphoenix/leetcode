package everyday;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/28 下午4:26
 */
public class FindPivotIndex {

    @Test
    void testSolution() {
        Assertions.assertEquals(3, pivotIndex(new int[]{1, 7, 3, 6, 5, 6}));
        Assertions.assertEquals(-1, pivotIndex(new int[]{1, 2, 3}));
        Assertions.assertEquals(-1, pivotIndex(new int[]{}));
        Assertions.assertEquals(-0, pivotIndex(new int[]{1}));
        Assertions.assertEquals(-1, pivotIndex(new int[]{1, 2}));
    }

    public int pivotIndex(int[] nums) {
        int len = nums.length, total;
        if (len <= 1) {
            return len - 1;
        }
        total = nums[0];
        int[] totalArr = new int[len];
        for (int i = 1; i < len; i++) {
            totalArr[i] = total;
            total += nums[i];
        }
        for (int i = 0; i < totalArr.length; i++) {
            if (totalArr[i] * 2 + nums[i] == total) {
                return i;
            }
        }
        return -1;
    }

}
