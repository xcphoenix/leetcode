package everyday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/24 上午9:38
 */
public class LongestContinuousIncreasingSubsequence {

    @Test
    void testSolution() {
        // assertEquals(3, findLengthOfLCIS(fromJson("[1,3,5,4,7]", int[].class)));
        // assertEquals(1, findLengthOfLCIS(fromJson("[2,2,2,2,2]", int[].class)));
        // assertEquals(4, findLengthOfLCIS(fromJson("[1,3,5,7]", int[].class)));
        assertEquals(2, findLengthOfLCIS(fromJson("[2,1,3]", int[].class)));
    }

    public int findLengthOfLCIS(int[] nums) {
        int val = 0, num = nums.length;
        for (int i = 0; i < num && val <= num - i; ) {
            int k = i + 1;
            while (k < num && nums[k] > nums[k - 1]) {
                k++;
            }
            val = Math.max(val, k - i);
            i = k;
        }
        return val;
    }

}
