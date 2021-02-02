package everyday;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/2/1 上午9:25
 */
public class FairCandySwap {

    @Test
    void testSolution() {
        assertArrayEquals(new int[]{1, 2}, fairCandySwap(new int[]{1, 1}, new int[]{2, 2}));
        assertArrayEquals(new int[]{1, 2}, fairCandySwap(new int[]{1, 2}, new int[]{2, 3}));
        assertArrayEquals(new int[]{2, 3}, fairCandySwap(new int[]{2}, new int[]{1, 3}));
        assertArrayEquals(new int[]{5, 4}, fairCandySwap(new int[]{1, 2, 5}, new int[]{2, 4}));
    }

    public int[] fairCandySwap(int[] A, int[] B) {
        Set<Integer> set = new HashSet<>();
        int allA = 0, allB = 0;
        for (int i : A) {
            allA += i;
        }
        for (int i : B) {
            allB += i;
            set.add(i);
        }
        int x = (allB - allA) / 2;
        for (int i : A) {
            if (set.contains(x + i)) {
                return new int[]{i, x + i};
            }
        }
        return new int[]{};
    }

}
