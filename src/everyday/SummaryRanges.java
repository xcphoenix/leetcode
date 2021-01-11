package everyday;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: 228 <br />
 * Link: https://leetcode-cn.com/problems/summary-ranges/
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/10 下午2:40
 */
public class SummaryRanges {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(
                new Solution().summaryRanges(new int[]{
                        -2147483648, -2147483647, 2147483647
                }).toArray(new String[0]))
        );
    }

    private static class Solution {
        public List<String> summaryRanges(int[] nums) {
            List<String> res = new ArrayList<>();
            if (nums == null || nums.length == 0) {
                return res;
            }
            int lastIndex = 0;
            int[] idx = new int[]{nums[0], 0};
            for (int i = 1; i < nums.length; i++) {
                if ((long) nums[i] - nums[lastIndex] > i - lastIndex) {
                    idx[1] = nums[i - 1];
                    res.add(joinIdx(idx));
                    idx[0] = nums[i];
                    lastIndex = i;
                }
            }
            idx[1] = nums[nums.length - 1];
            res.add(joinIdx(idx));
            return res;
        }

        private String joinIdx(int[] idx) {
            if (idx[0] == idx[1]) {
                return String.valueOf(idx[0]);
            }
            return idx[0] + "->" + idx[1];
        }
    }

}