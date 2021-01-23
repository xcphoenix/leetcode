package everyday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/23 上午9:28
 */
public class NumbersOfOperationsToMakeNetworkConnected {

    @Test
    void testSolution() {
        assertEquals(1, makeConnected(4, fromJson("[[0,1],[0,2],[1,2]]", int[][].class)));
        assertEquals(2, makeConnected(6, fromJson("[[0,1],[0,2],[0,3],[1,2],[1,3]]", int[][].class)));
        assertEquals(-1, makeConnected(6, fromJson("[[0,1],[0,2],[0,3],[1,2]]", int[][].class)));
        assertEquals(0, makeConnected(5, fromJson("[[0,1],[0,2],[3,4],[2,3]]", int[][].class)));
    }

    public int makeConnected(int n, int[][] connections) {
        final int connectionNum = connections.length;
        if (n == 1 || n == connectionNum - 1) {
            return 0;
        }
        if (connectionNum < n - 1) {
            return -1;
        }

        UnionFind unionFind = new UnionFind(n);
        for (int[] connection : connections) {
            unionFind.add(connection[0], connection[1]);
        }

        return unionFind.getCnt() - 1;
    }

    private static class UnionFind {
        private final int[] parent;
        // private final int[] nodeNum;
        // private final int[] lineNum;
        private int cnt;

        public UnionFind(int num) {
            this.parent = new int[num];
            // this.nodeNum = new int[num];
            for (int i = 0; i < num; i++) {
                // this.nodeNum[i] = 1;
                this.parent[i] = i;
            }
            // this.lineNum = new int[num];
            this.cnt = num;
        }

        public void add(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                // lineNum[qRoot]++;
                return;
            }
            parent[qRoot] = pRoot;
            // nodeNum[pRoot] += nodeNum[qRoot];
            // lineNum[pRoot] = lineNum[pRoot] + lineNum[qRoot] + 1;
            cnt--;
        }

        public int find(int node) {
            while (parent[node] != node) {
                parent[node] = find(parent[node]);
                node = parent[node];
            }
            return node;
        }

        public int getCnt() {
            return cnt;
        }

        // public int[] getParents() {
        //     int[] parentNodes = new int[parent.length];
        //     int size = 0;
        //     for (int i = 0; i < parent.length; i++) {
        //         if (i == find(i)) {
        //             parentNodes[size++] = i;
        //         }
        //     }
        //     return Arrays.copyOf(parentNodes, size);
        // }
        //
        // public int getExtraLineNum(int node) {
        //     int root = find(node);
        //     return lineNum[root] - nodeNum[root];
        // }
    }

}
