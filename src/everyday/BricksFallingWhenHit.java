package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Problem: 803 <br />
 * Link:    https://leetcode-cn.com/problems/bricks-falling-when-hit/ <br />
 * Level:   Hard <br />
 * Tag:     <kbd>并查集</kbd>
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/16 上午10:41
 */
public class BricksFallingWhenHit {

    @Test
    void testSolution() {
        JsonUtils.echoJson(
                hitBricks(
                        new int[][]{
                                {1, 0, 1},
                                {1, 1, 1},
                        },
                        new int[][]{
                                {0, 0},
                                {0, 2},
                                {1, 1},
                        })
        );
    }

    /**
     * 打砖块
     *
     * <pre>
     *     emmm 效率有点差，但是可以过
     * </pre>
     *
     * @param grid 二元网格，其中 1 表示砖块，0 表示空白。砖块 稳定（不会掉落）的前提是：
     *             <ul>
     *                 <li>一块砖直接连接到网格的顶部，或者</li>
     *                 <li>至少有一块相邻（4个方向之一）砖块稳定不会掉落时</li>
     *             </ul>
     * @param hits 依次消除砖块的位置
     * @return 返回一个数组 result ，其中 result[i] 表示第 i 次消除操作对应掉落的砖块数目。
     */
    public int[] hitBricks(int[][] grid, int[][] hits) {
        final int hitFlag = 2, hitNum = hits.length;
        final int jLimit = grid[0].length, iLimit = grid.length;
        int[] result = new int[hitNum];
        UnionFind unionFind = new UnionFind(jLimit * iLimit);

        for (int[] hit : hits) {
            if (grid[hit[0]][hit[1]] == 1) {
                grid[hit[0]][hit[1]] = hitFlag;
            }
        }
        for (int i = 0; i < iLimit; i++) {
            for (int j = 0; j < jLimit; j++) {
                final int type = grid[i][j];
                if (type != 1) {
                    continue;
                }
                int pointIndex = i * jLimit + j;
                int[][] allAround = pointAllAround(i, j, jLimit, iLimit);
                for (int[] point : allAround) {
                    if (grid[point[0]][point[1]] == type) {
                        unionFind.add(pointIndex, point[0] * jLimit + point[1]);
                    }
                }
            }
        }
        for (int i = hits.length - 1; i >= 0; i--) {
            final int hitI = hits[i][0], hitJ = hits[i][1];
            if (grid[hitI][hitJ] == 0) {
                continue;
            }
            int beforeNum, afterNum;
            // 统计之前稳定的数量
            beforeNum = cntStableNum(unionFind, grid, jLimit);
            // 这里肯定是type为2的情况，现在还是找四周，只要不是2和0就可以连通
            int pointIndex = hitI * jLimit + hitJ;
            int[][] allAround = pointAllAround(hitI, hitJ, jLimit, iLimit);
            for (int[] point : allAround) {
                if (grid[point[0]][point[1]] == 1) {
                    unionFind.add(pointIndex, point[0] * jLimit + point[1]);
                }
            }
            grid[hitI][hitJ] = 1;
            // 统计之后稳定的数量
            afterNum = cntStableNum(unionFind, grid, jLimit);
            if (beforeNum != afterNum) {
                result[i] = afterNum - beforeNum - 1;
            }
        }

        return result;
    }

    private int cntStableNum(UnionFind unionFind, int[][] grid, int maxStableIdx) {
        int cnt = 0;
        Set<Integer> rootSet = new HashSet<>();
        for (int i = 0; i < maxStableIdx; i++) {
            if (grid[0][i] != 1) {
                continue;
            }
            int root = unionFind.find(i);
            if (!rootSet.contains(root)) {
                cnt += unionFind.getConNum(root);
                rootSet.add(root);
            }
        }
        return cnt;
    }

    private int[][] pointAllAround(int i, int j, final int jLimit, final int iLimit) {
        final int[][] around = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        List<int[]> res = new ArrayList<>(4);
        for (int[] inc : around) {
            int newX = i + inc[0];
            int newY = j + inc[1];
            if (newX >= 0 && newX < iLimit && newY >= 0 && newY < jLimit) {
                res.add(new int[]{newX, newY});
            }
        }
        return res.toArray(new int[0][]);
    }

    private static class UnionFind {
        private final int[] parent;
        private final int[] nodeNum;

        public UnionFind(int num) {
            this.parent = new int[num];
            this.nodeNum = new int[num];
            for (int i = 0; i < num; i++) {
                parent[i] = i;
                nodeNum[i] = 1;
            }
        }

        public boolean add(int q, int p) {
            int qRoot = find(q);
            int pRoot = find(p);
            if (qRoot == pRoot) {
                return false;
            }
            parent[qRoot] = pRoot;
            nodeNum[pRoot] += nodeNum[qRoot];
            nodeNum[qRoot] = 1;
            return true;
        }

        public int find(int node) {
            while (parent[node] != node) {
                parent[node] = find(parent[node]);
                node = parent[node];
            }
            return node;
        }

        public int getConNum(int node) {
            return nodeNum[node];
        }
    }

}
