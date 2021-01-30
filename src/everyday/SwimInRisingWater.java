package everyday;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/30 上午9:30
 */
public class SwimInRisingWater {

    @Test
    void testSolution() {
        assertEquals(3, swimInWater(fromJson("[[0,2],[1,3]]", int[][].class)));
        assertEquals(16, swimInWater(fromJson("[[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]", int[][].class)));
    }

    final int[][] DIRECTION = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public int swimInWater(int[][] grid) {
        final int n = grid.length;
        int time = 0, lastIdx = 0;
        UnionFind unionFind = new UnionFind(n * n);
        Queue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(value -> grid[value >> 16][value & 0xFFFF]));
        boolean[] book = new boolean[n * n];
        queue.offer(0);
        book[0] = true;

        while (!queue.isEmpty()) {
            int idx = queue.poll();
            int row = idx >> 16, col = idx & 0xFFFF;
            time = Math.max(grid[row][col], time);
            unionFind.unite(row * n + col, lastIdx);
            // System.out.printf("(%d, %d) -> %d\n", row, col, time);
            for (int i = 0; i < 4; i++) {
                int newRow = row + DIRECTION[i][0], newCol = col + DIRECTION[i][1];
                int newIdx = (newRow << 16) | newCol;
                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n && !book[newRow * n + newCol]) {
                    book[newRow * n + newCol] = true;
                    queue.offer(newIdx);
                }
            }
            if (unionFind.isConnect(0, n * n - 1)) {
                break;
            }
        }
        return time;
    }

    public static class UnionFind {
        private final int[] parent;

        public UnionFind(int num) {
            this.parent = new int[num];
            for (int i = 0; i < parent.length; i++) {
                parent[i] = i;
            }
        }

        public void unite(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return;
            }
            parent[qRoot] = pRoot;
        }

        public boolean isConnect(int q, int p) {
            return find(q) == find(p);
        }

        private int find(int node) {
            while (parent[node] != node) {
                parent[node] = find(parent[node]);
                node = parent[node];
            }
            return node;
        }
    }

}
