package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

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

    final static int[][] AROUND = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    /**
     * 打砖块
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
        final int row = grid.length, col = grid[0].length, size = col * row;
        int[] result = new int[hits.length];
        // 0 作为屋顶的虚拟节点
        UnionFind unionFind = new UnionFind(size + 1);

        for (int[] hit : hits) {
            if (grid[hit[0]][hit[1]] == 1) {
                grid[hit[0]][hit[1]] = 2;
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] != 1) {
                    continue;
                }
                if (i == 0) {
                    unionFind.add(pos2Idx(i, j, col), 0);
                }
                connect(grid, col, row, unionFind, i, j);
            }
        }
        for (int i = hits.length - 1; i >= 0; i--) {
            int hitI = hits[i][0], hitJ = hits[i][1];
            if (grid[hitI][hitJ] == 0) {
                continue;
            }
            int beforeNum, afterNum;
            // 统计之前稳定的数量
            beforeNum = unionFind.getConNum(0);
            grid[hitI][hitJ] = 1;
            if (hitI == 0) {
                unionFind.add(pos2Idx(0, hitJ, col), 0);
            }
            connect(grid, col, row, unionFind, hitI, hitJ);
            // 统计之后稳定的数量
            afterNum = unionFind.getConNum(0);
            result[i] = Math.max(afterNum - beforeNum - 1, 0);
        }

        return result;
    }

    private int pos2Idx(int i, int j, int jLimit) {
        return i * jLimit + j + 1;
    }

    private void connect(int[][] grid, int jLimit, int iLimit, UnionFind unionFind, int i, int j) {
        int pointIndex = pos2Idx(i, j, jLimit);
        for (int[] inc : AROUND) {
            int newI = i + inc[0], newJ = j + inc[1];
            if (newI >= 0 && newI < iLimit && newJ >= 0 && newJ < jLimit) {
                if (grid[newI][newJ] == 1) {
                    if (newI == 0) {
                        unionFind.add(pos2Idx(newI, newJ, jLimit), 0);
                    }
                    unionFind.add(pos2Idx(newI, newJ, jLimit), pointIndex);
                }
            }
        }
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
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return false;
            }
            int subSet = Math.max(qRoot, pRoot), mainSet = Math.min(qRoot, pRoot);
            parent[subSet] = mainSet;
            nodeNum[mainSet] += nodeNum[subSet];
            nodeNum[subSet] = 1;
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
            return nodeNum[find(node)];
        }
    }

}
