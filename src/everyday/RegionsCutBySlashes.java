package everyday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Problem: 959 <br />
 * Link:    https://leetcode-cn.com/problems/regions-cut-by-slashes/<br />
 * Level:   Medium <br />
 * Tag:     并查集
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/25 上午9:53
 */
public class RegionsCutBySlashes {

    @Test
    void testSolution() {
        assertEquals(2, regionsBySlashes(new String[]{" /", "/ "}));
        assertEquals(1, regionsBySlashes(new String[]{" /", "  "}));
        assertEquals(4, regionsBySlashes(new String[]{"\\/", "/\\"}));
        assertEquals(5, regionsBySlashes(new String[]{"/\\", "\\/"}));
        assertEquals(3, regionsBySlashes(new String[]{"//", "/ "}));
    }

    private static int N;
    // 顺时针 这里由于是按照顺序去遍历的，不需要四个方向都找一次，可以只找左右、上下其中的一个方向即可
    private final static int[][] DIRECT = {{0, -1}, {-1, 0}};
    // private final static int[][] DIRECT = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

    public int regionsBySlashes(String[] grid) {
        N = grid.length;
        UnionFind unionFind = new UnionFind(N * N * 4);
        for (int i = 0; i < grid.length; i++) {
            char[] arr = grid[i].toCharArray();
            for (int j = 0; j < arr.length; j++) {
                final int startIdx = point(i, j);
                connectInner(unionFind, arr[j], startIdx);
                // 四个方向：上、左、下、右
                // 分别对应：0-2 1-3 2-0 3-1 ==> +2 % 4
                for (int d = 0; d < DIRECT.length; d++) {
                    int thisSubIdx = (d + 2) % 4;
                    int x = i + DIRECT[d][0], y = j + DIRECT[d][1];
                    if (x >= 0 && x < N && y >= 0 && y < N) {
                        unionFind.add(startIdx + thisSubIdx, point(x, y) + d);
                    }
                }
            }
        }
        return unionFind.getCnt();
    }

    private int point(int i, int j) {
        return (i * N + j) * 4;
    }

    private void connectInner(UnionFind unionFind, char ch, int startIdx) {
        // 这里也能用数组优化，不需要 switch case
        switch (ch) {
            case '\\':
                unionFind.add(startIdx, startIdx + 3);
                unionFind.add(startIdx + 1, startIdx + 2);
                break;
            case '/':
                unionFind.add(startIdx, startIdx + 1);
                unionFind.add(startIdx + 2, startIdx + 3);
                break;
            default:
                unionFind.add(startIdx, startIdx + 1);
                unionFind.add(startIdx, startIdx + 2);
                unionFind.add(startIdx, startIdx + 3);
        }
    }

    private static class UnionFind {
        private final int[] parent;
        private int cnt;

        public UnionFind(int num) {
            this.parent = new int[num];
            for (int i = 0; i < num; i++) {
                this.parent[i] = i;
            }
            this.cnt = num;
        }

        public void add(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return;
            }
            parent[qRoot] = pRoot;
            cnt--;
        }

        public int find(int node) {
            while (node != parent[node]) {
                parent[node] = find(parent[node]);
                node = parent[node];
            }
            return node;
        }

        public int getCnt() {
            return cnt;
        }
    }

}
