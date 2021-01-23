package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/17 下午5:51
 */
public class MinimumCostTreeFromLeafValues2 {

    @Test
    void testSolution() {
        JsonUtils.echoJson(mctFromLeafValues(new int[]{
                6, 2, 4, 5, 8, 9, 10, 13, 1
        }));
        System.out.println();
    }

    private int[][] subArrMax;
    private int[][] record;

    /**
     * DP
     */
    public int mctFromLeafValues(int[] arr) {
        final int nodeNum = arr.length;
        this.subArrMax = new int[nodeNum][nodeNum];
        this.record = new int[nodeNum][nodeNum];
        // 提前计算好 i->j 的最大值
        for (int i = 0; i < nodeNum; i++) {
            int maxVal = arr[i];
            subArrMax[i][i] = arr[i];
            for (int j = i + 1; j < nodeNum; j++) {
                maxVal = Math.max(maxVal, arr[j]);
                subArrMax[i][j] = maxVal;
            }
        }

        for (int i = 0; i < nodeNum; i++) {
            for (int j = i; j < nodeNum; j++) {
                int minVal = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int root = subArrMax[i][k] * subArrMax[k + 1][j];
                    if (root > minVal) {
                        continue;
                    }
                    System.out.printf("(%d, %d) --> (%d, %d),(%d, %d)\n", i, j, i, k, k + 1, j);
                    minVal = Math.min(minVal, root + record[i][k] + record[k + 1][j]);
                }
                System.out.printf("OK (%d, %d)\n", i, j);
                record[i][j] = minVal;
            }
        }

        return record[0][nodeNum - 1];
    }

}
