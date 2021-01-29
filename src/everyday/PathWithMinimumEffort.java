package everyday;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/29 下午4:16
 */
public class PathWithMinimumEffort {

    @Test
    void testSolution() {
        assertEquals(2, minimumEffortPathByUnionFind(fromJson("[[1,2,2],[3,8,2],[5,3,5]]", int[][].class)));
        assertEquals(1, minimumEffortPathByUnionFind(fromJson("[[1,2,3],[3,8,4],[5,3,5]]", int[][].class)));
        assertEquals(0, minimumEffortPathByUnionFind(fromJson("[[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]", int[][].class)));
        assertEquals(6, minimumEffortPathByUnionFind(fromJson("[[10,8],[10,8],[1,2],[10,3],[1,3],[6,3],[5,2]]", int[][].class)));
    }

    /*
     * METHOD: UnionFind
     */

    public int minimumEffortPathByUnionFind(int[][] heights) {
        int rowMax = heights.length, colMax = heights[0].length;
        int nums = rowMax * colMax, idx = 0;
        UnionFind unionFind = new UnionFind(nums);
        List<int[]> list = new ArrayList<>();

        // just left + up
        for (int i = 0; i < rowMax; i++) {
            for (int j = 0; j < colMax; j++) {
                if (i > 0) {
                    list.add(new int[]{idx, idx - colMax, Math.abs(heights[i][j] - heights[i - 1][j])});
                }
                if (j > 0) {
                    list.add(new int[]{idx, idx - 1, Math.abs(heights[i][j] - heights[i][j - 1])});
                }
                idx++;
            }
        }

        list.sort(Comparator.comparingInt(o -> o[2]));

        int ans = 0;
        for (int[] edge : list) {
            ans = Math.max(ans, edge[2]);
            unionFind.add(edge[0], edge[1]);
            if (unionFind.find(0) == unionFind.find(nums - 1)) {
                break;
            }
        }
        return ans;
    }

    private static class UnionFind {
        private final int[] parent;

        public UnionFind(int num) {
            this.parent = new int[num];
            for (int i = 0; i < num; i++) {
                this.parent[i] = i;
            }
        }

        public void add(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return;
            }
            parent[qRoot] = pRoot;
        }

        public int find(int node) {
            while (parent[node] != node) {
                parent[node] = find(parent[node]);
                node = parent[node];
            }
            return node;
        }
    }

    /*
     * METHOD: bfs dfs
     */

    final int[][] DIRECT = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    int rowMax = 0, colMax = 0;

    // bfs 效率比 dfs高很多 但空间占用比较大
    public int minimumEffortPath(int[][] heights) {
        rowMax = heights.length;
        colMax = heights[0].length;
        int maxAbs = 0, nums = rowMax * colMax;
        int[][] cost = new int[nums][DIRECT.length];

        // 可以合并到第二次操作中
        for (int i = 0; i < rowMax; i++) {
            for (int j = 0; j < colMax; j++) {
                for (int k = 0; k < DIRECT.length; k++) {
                    int newRow = i + DIRECT[k][0], newCol = j + DIRECT[k][1];
                    if (newRow >= 0 && newRow < rowMax && newCol >= 0 && newCol < colMax) {
                        int thisAbc = Math.abs(heights[i][j] - heights[newRow][newCol]);
                        maxAbs = Math.max(maxAbs, thisAbc);
                        cost[i * colMax + j][k] = thisAbc;
                    } else {
                        cost[i * colMax + j][k] = -1;
                    }
                }
            }
        }

        int k = maxAbs / 2, left = 0, right = maxAbs, ans = 0;
        while (left <= right) {
            // System.out.printf("left(%d) , k(%d), right(%d)\n", left, k, right);
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(0);
            boolean[] book = new boolean[nums];
            book[0] = true;

            while (!queue.isEmpty()) {
                int idx = queue.poll();
                int i = idx / colMax, j = idx % colMax;
                for (int d = 0; d < DIRECT.length; d++) {
                    if (cost[idx][d] >= 0 && cost[idx][d] <= k) {
                        int newI = i + DIRECT[d][0], newJ = j + DIRECT[d][1];
                        int newIdx = newI * colMax + newJ;
                        if (book[newIdx]) {
                            continue;
                        }
                        book[newIdx] = true;
                        queue.offer(newIdx);
                    }
                }
            }

            if (book[nums - 1]) {
                ans = k;
                right = k - 1;
                k = (left + k) / 2;
            } else {
                left = k + 1;
                k = (int) ((1.0 * k + right) / 2 + 0.5);
            }
        }
        return ans;
    }

}
