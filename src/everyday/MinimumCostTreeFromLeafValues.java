package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

/**
 * Problem: 1130 <br />
 * Link:    https://leetcode-cn.com/problems/minimum-cost-tree-from-leaf-values/ <br />
 * Level:   Medium <br />
 * Tag:     动态规划,单调栈
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/17 上午10:07
 */
public class MinimumCostTreeFromLeafValues {

    /*
     * 满足每个节点都有0个或2个子节点的二叉树，若有 k 个叶子节点，则有 k - 1 个非叶子节点,
     * 考虑到中序遍历的特殊性，两个叶子节点之间必然存在一个父节点
     */

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

        return dp(0, nodeNum - 1);
    }

    /**
     * 本质上是暴力，穷举根节点的位置，在这个基础上计算左右子树，分解为子问题，
     * 穷举完所有的之后，选择非叶子节点和最小的
     */
    private int dp(int i, int j) {
        // 到了叶子节点
        if (i == j) {
            return 0;
        }
        // 存在记录
        if (record[i][j] > 0) {
            return record[i][j];
        }
        int minVal = Integer.MAX_VALUE;
        for (int k = i; k < j; k++) {
            int root = subArrMax[i][k] * subArrMax[k + 1][j];
            if (root > minVal) {
                continue;
            }
            minVal = Math.min(
                    minVal, root + /* 根k的值 */ +dp(i, k) /* 左子树 */ + dp(k + 1, j) /* 右子树 */
            );
        }
        record[i][j] = minVal;
        return minVal;
    }


}
