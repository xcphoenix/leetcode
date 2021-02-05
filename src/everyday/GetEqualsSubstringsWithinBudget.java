package everyday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/2/5 上午9:36
 */
public class GetEqualsSubstringsWithinBudget {

    @Test
    void testSolution() {
        assertEquals(3, equalSubstring("abcd", "bcdf", 3));
        assertEquals(1, equalSubstring("abcd", "cdef", 3));
        assertEquals(1, equalSubstring("abcd", "acde", 0));
        assertEquals(2, equalSubstring("krrgw", "zjxss", 19));
    }

    public int equalSubstring(String s, String t, int maxCost) {
        int strLen = s.length(), maxTranLen = 0, right = 0, left = 0;
        int[] diff = new int[strLen];
        for (int i = 0; i < strLen; i++) {
            diff[i] = Math.abs(s.charAt(i) - t.charAt(i));
        }
        while (right < strLen) {
            if (diff[right] <= maxCost) {
                maxCost -= diff[right];
                maxTranLen++;
            } else if (right + 1 < strLen) {
                maxCost += diff[left] - diff[right];
                left++;
            }
            right++;
        }
        return maxTranLen;
    }

}
