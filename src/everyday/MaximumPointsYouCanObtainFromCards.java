package everyday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/2/6 上午10:01
 */
public class MaximumPointsYouCanObtainFromCards {

    @Test
    void testSolution() {
        assertEquals(12, maxScore(new int[]{1, 2, 3, 4, 5, 6, 1}, 3));
        assertEquals(4, maxScore(new int[]{2, 2, 2}, 2));
        assertEquals(55, maxScore(new int[]{9, 7, 7, 9, 7, 7, 9}, 7));
        assertEquals(1, maxScore(new int[]{1, 1000, 1}, 1));
        assertEquals(202, maxScore(new int[]{1, 79, 80, 1, 1, 1, 200, 1}, 3));
    }

    public int maxScore(int[] cardPoints, int k) {
        int cardNum = cardPoints.length;
        int left = 0, right = cardNum - k, ans = 0, result;
        for (int i = cardNum - 1; i >= cardNum - k; i--) {
            ans += cardPoints[i];
        }
        result = ans;
        // left 不包含  right 包含
        while (left < cardNum && right < cardNum) {
            ans = ans + cardPoints[left] - cardPoints[right];
            result = Math.max(result, ans);
            left++;
            right++;
        }
        return result;
    }

}
