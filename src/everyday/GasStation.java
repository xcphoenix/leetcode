package everyday;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/18 下午8:27
 * @see <a href="https://leetcode-cn.com/problems/gas-station/">134.加油站</a>
 */
public class GasStation {

    static class Solution {
        /**
         * 暴力解法
         */
        public int canCompleteCircuit(int[] gas, int[] cost) {
            final int stationNum = gas.length;
            final int[] needed = new int[stationNum];
            int total = 0;
            int startIndex = -1;

            for (int i = 0; i < stationNum; i++) {
                needed[i] = gas[i] - cost[i];
                if (needed[i] >= 0 && startIndex < 0) {
                    startIndex = i;
                }
                total += needed[i];
            }

            if (total < 0) {
                return -1;
            }

            for (int i = startIndex; i < stationNum; i++) {
                if (test(i, needed)) {
                    return i;
                }
            }

            return -1;
        }

        private boolean test(int index, int[] array) {
            int all = 0;
            for (int i = index; i < index + array.length; i++) {
                all += array[i % array.length];
                if (all < 0) {
                    return false;
                }
            }
            return true;
        }

    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(
                solution.canCompleteCircuit(
                        new int[]{2},
                        new int[]{2}
                )
        );
    }

}