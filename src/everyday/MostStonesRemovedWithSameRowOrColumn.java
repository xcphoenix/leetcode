package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: 947 <br/>
 * Link:    https://leetcode-cn.com/problems/most-stones-removed-with-same-row-or-column/
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/15 下午8:10
 */
public class MostStonesRemovedWithSameRowOrColumn {

    @Test
    void testSolution() {
        JsonUtils.echoJson(
                removeStones(new int[][]{
                        {0, 0},
                        {0, 2},
                        {1, 1},
                        {2, 0},
                        {2, 2},
                })
        );
    }

    /**
     * 并查集，把坐标映射到坐标轴上，凡是有多个点可以落到轴上的同一个点，意味着这两个点是连通的（横纵坐标），
     * 那么相连点可以删掉绝大多数只剩一个点，因为只要是相连的点，都可以选定一个点作为根，其他的所有点都有一条
     * 线路去追溯到根节点上，沿着这条线路逐个删除节点，仅剩根节点，所有的链路删除完毕后，只留下一个根节点
     * <p>
     * 所以建立并查集后，删除最多的节点后，留下的节点个数即为并查集中的count
     */
    public int removeStones(int[][] stones) {
        final int stoneNum = stones.length;
        // key: x pos, value: stone index
        Map<Integer, Integer> xMap = new HashMap<>();
        Map<Integer, Integer> yMap = new HashMap<>();
        UnionFind unionFind = new UnionFind(stoneNum);
        for (int i = 0; i < stoneNum; i++) {
            xMap.putIfAbsent(stones[i][0], i);
            yMap.putIfAbsent(stones[i][1], i);
            unionFind.add(xMap.get(stones[i][0]), i);
            unionFind.add(yMap.get(stones[i][1]), i);
        }
        return stoneNum - unionFind.count;
    }

    private static class UnionFind {
        private final int[] parent;
        private int count;

        public UnionFind(int num) {
            this.parent = new int[num];
            for (int i = 0; i < num; i++) {
                parent[i] = i;
            }
            this.count = num;
        }

        public boolean add(int q, int p) {
            int qRoot = find(q);
            int pRoot = find(p);
            if (qRoot == pRoot) {
                return false;
            }
            parent[qRoot] = pRoot;
            count--;
            return true;
        }

        public int find(int node) {
            while (parent[node] != node) {
                parent[node] = parent[parent[node]];
                node = parent[node];
            }
            return node;
        }
    }

}
