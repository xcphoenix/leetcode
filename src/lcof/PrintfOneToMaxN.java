package lcof;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/20 下午4:46
 * @see <a href="https://leetcode-cn.com/problems/da-yin-cong-1dao-zui-da-de-nwei-shu-lcof/">
 *        剑指 Offer 17：打印从1到最大的n位十进制数
 *      </a>
 */
public class PrintfOneToMaxN {

    private final List<String> numbers = new ArrayList<>();

    public String[] printBigNumbers(int n) {
        dfs(0, n, new StringBuilder());
        return numbers.toArray(new String[0]);
    }

    private void dfs(int index, int length, StringBuilder builder) {
        if (index == length) {
            int zeroIndex = 0;
            for (int i = 0; i < builder.length() && builder.charAt(zeroIndex) == '0'; i++) {
                zeroIndex++;
            }
            zeroIndex = Math.min(zeroIndex, builder.length() - 1);
            numbers.add(builder.substring(zeroIndex));
            return;
        }
        for (int i = 0; i <= 9; i++) {
            dfs(index + 1, length, builder.append(i));
            builder.deleteCharAt(index);
        }
    }

    public int[] printNumbers(int n) {
        int maxNum = (int) (Math.pow(10, n) - 1);
        int[] res = new int[maxNum];
        for (int i = 1; i <= maxNum; i++) {
            res[i - 1] = i;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(
                // Arrays.toString(new PrintfOneToMaxN().printNumbers(1))
                Arrays.toString(new PrintfOneToMaxN().printBigNumbers(2))
        );
    }

}
