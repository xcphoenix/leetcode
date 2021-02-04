package everyday;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/2/4 上午9:35
 */
public class MaximumAverageSubarrayI {

    @Test
    void testSolution() {
        Assertions.assertEquals(12.75, findMaxAverage(new int[]{1, 12, -5, -6, 50, 3}, 4));
    }

    public double findMaxAverage(int[] nums, int k) {
        int average = 0, left = 0, leftVal = 0;
        for (int i = 0; i < k; i++) {
            average += nums[i];
        }
        if (k == nums.length) {
            return average * 1.0 / k;
        }
        leftVal = nums[left];
        do {
            int nextRight = left + k;
            if (nums[nextRight] >= leftVal) {
                average += (nums[nextRight] - leftVal);
                leftVal = nums[left + 1];
            } else {
                leftVal = leftVal + nums[left + 1] - nums[nextRight];
            }
            left++;
        }
        while (left + k < nums.length);
        return average * 1.0 / k;
    }

}
