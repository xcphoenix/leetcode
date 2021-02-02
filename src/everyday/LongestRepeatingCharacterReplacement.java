package everyday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Problem: 424 <br />
 * Link:    https://leetcode-cn.com/problems/longest-repeating-character-replacement/ <br />
 * Level:   Medium <br />
 * Tag:     双指针,滑动窗口
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/2/2 上午9:07
 */
public class LongestRepeatingCharacterReplacement {

    @Test
    void testSolution() {
        assertEquals(4, characterReplacement("ABAB", 2));
        assertEquals(4, characterReplacement("AABABBA", 1));
    }

    public int characterReplacement(String s, int k) {
        int left = 0, right = 0;
        int[] chCnt = new int[26];
        while (right < s.length()) {
            chCnt[s.charAt(right) - 'A']++;
            int needReplaceNum = 0;
            // 2. maxCount 在内层循环「左边界向右移动一个位置」的过程中，没有维护它的定义，结论是否正确？
            // 答：结论依然正确。「左边界向右移动一个位置」的时候，maxCount 或者不变，或者值减 11。
            //
            // maxCount 的值虽然不维护，但数组 freq 的值是被正确维护的；
            // 当「左边界向右移动」之前：
            // 如果有两种字符长度相等，左边界向右移动不改变 maxCount 的值。例如 s = [AAABBB]、k = 2，左边界 A 移除以后，窗口内字符出现次数不变，依然为 33；
            // 如果左边界移除以后，使得此时 maxCount 的值变小，又由于 我们要找的只是最长替换 k 次以后重复子串的长度。接下来我们继续让右边界向右移动一格，有两种情况：① 右边界如果读到了刚才移出左边界的字符，恰好 maxCount 的值被正确维护；② 右边界如果读到了不是刚才移出左边界的字符，新的子串要想在符合题意的条件下变得更长，maxCount 一定要比之前的值还要更多，因此不会错过更优的解。
            //
            //
            // 作者：LeetCode
            // 链接：https://leetcode-cn.com/problems/longest-repeating-character-replacement/solution/ti-huan-hou-de-zui-chang-zhong-fu-zi-fu-eaacp/
            // 来源：力扣（LeetCode）
            // 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
            for (int cnt : chCnt) {
                needReplaceNum = Math.max(needReplaceNum, cnt);
            }
            if (right - left + 1 - needReplaceNum > k) {
                chCnt[s.charAt(left) - 'A']--;
                left++;
            }
            right++;
        }
        return right - left;
    }

}
