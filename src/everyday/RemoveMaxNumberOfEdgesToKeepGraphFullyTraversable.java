package everyday;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static utils.JsonUtils.fromJson;

/**
 * Problem: 1579<br />
 * Link:    https://leetcode-cn.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/<br />
 * Level:   Hard<br />
 * Tag:     并查集
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/27 上午9:32
 */
public class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable {

    @Test
    void testSolution() {
        Assertions.assertEquals(2, maxNumEdgesToRemove(
                4,
                fromJson("[[3,1,2],[3,2,3],[1,1,3],[1,2,4],[1,1,2],[2,3,4]]", int[][].class))
        );
        Assertions.assertEquals(33, maxNumEdgesToRemove(
                12,
                fromJson("[[3,1,2],[2,2,3],[3,1,4],[2,3,5],[1,2,6],[2,4,7],[3,3,8],[3,2,9],[2,1,10],[2,1,11],[1,11,12],[1,10,11],[2,5,9],[2,7,10],[2,4,12],[3,9,10],[1,6,9],[2,10,12],[1,2,5],[3,5,6],[1,7,11],[1,8,9],[1,1,11],[3,4,5],[1,5,9],[2,4,9],[1,8,11],[3,6,8],[1,8,10],[2,2,4],[2,3,8],[3,2,6],[3,10,11],[2,3,11],[3,5,9],[3,3,5],[2,6,11],[3,2,7],[1,5,11],[1,1,5],[2,9,10],[1,6,7],[3,2,3],[2,8,9],[3,2,8]]", int[][].class))
        );
        Assertions.assertEquals(114, maxNumEdgesToRemove(
                13,
                fromJson("[[1,1,2],[2,1,3],[3,2,4],[3,2,5],[1,2,6],[3,6,7],[3,7,8],[3,6,9],[3,4,10],[2,3,11],[1,5,12],[3,3,13],[2,1,10],[2,6,11],[3,5,13],[1,9,12],[1,6,8],[3,6,13],[2,1,4],[1,1,13],[2,9,10],[2,1,6],[2,10,13],[2,2,9],[3,4,12],[2,4,7],[1,1,10],[1,3,7],[1,7,11],[3,3,12],[2,4,8],[3,8,9],[1,9,13],[2,4,10],[1,6,9],[3,10,13],[1,7,10],[1,1,11],[2,4,9],[3,5,11],[3,2,6],[2,1,5],[2,5,11],[2,1,7],[2,3,8],[2,8,9],[3,4,13],[3,3,8],[3,3,11],[2,9,11],[3,1,8],[2,1,8],[3,8,13],[2,10,11],[3,1,5],[1,10,11],[1,7,12],[2,3,5],[3,1,13],[2,4,11],[2,3,9],[2,6,9],[2,1,13],[3,1,12],[2,7,8],[2,5,6],[3,1,9],[1,5,10],[3,2,13],[2,3,6],[2,2,10],[3,4,11],[1,4,13],[3,5,10],[1,4,10],[1,1,8],[3,3,4],[2,4,6],[2,7,11],[2,7,10],[2,3,12],[3,7,11],[3,9,10],[2,11,13],[1,1,12],[2,10,12],[1,7,13],[1,4,11],[2,4,5],[1,3,10],[2,12,13],[3,3,10],[1,6,12],[3,6,10],[1,3,4],[2,7,9],[1,3,11],[2,2,8],[1,2,8],[1,11,13],[1,2,13],[2,2,6],[1,4,6],[1,6,11],[3,1,2],[1,1,3],[2,11,12],[3,2,11],[1,9,10],[2,6,12],[3,1,7],[1,4,9],[1,10,12],[2,6,13],[2,2,12],[2,1,11],[2,5,9],[1,3,8],[1,7,8],[1,2,12],[1,5,11],[2,7,12],[3,1,11],[3,9,12],[3,2,9],[3,10,11]]", int[][].class))
        );
    }

    public int maxNumEdgesToRemove(int n, int[][] edges) {
        UnionFind aliceUn = new UnionFind(n);
        UnionFind bobUn = new UnionFind(n);
        int answer = 0;

        // 删除多余公共边
        for (int[] edge : edges) {
            if (edge[0] == 3) {
                int p = edge[1] - 1, q = edge[2] - 1;
                if (aliceUn.add(p, q)) {
                    bobUn.add(p, q);
                } else {
                    answer++;
                }
            }
        }

        // 删除冗余边
        for (int[] edge : edges) {
            int p = edge[1] - 1, q = edge[2] - 1;
            switch (edge[0]) {
                case 1:
                    if (!aliceUn.add(p, q)) {
                        answer++;
                    }
                    break;
                case 2:
                    if (!bobUn.add(p, q)) {
                        answer++;
                    }
                    break;
            }
        }

        if (aliceUn.getCnt() > 1 || bobUn.getCnt() > 1) {
            return -1;
        }
        return answer;
    }

    /**
     * BUG: 这种计算下来的是理想情况下可以删除的最大边数，忽略了删除边对连通性的影响
     */
    public int maxNumEdgesToRemove_Error(int n, int[][] edges) {
        UnionFind alice = new UnionFind(n);
        UnionFind bob = new UnionFind(n);
        int commonNum = 0, aliceNum = 0, bobNum = 0, result = 0;

        for (int[] edge : edges) {
            int p = edge[1] - 1, q = edge[2] - 1;
            switch (edge[0]) {
                case 1:
                    aliceNum++;
                    alice.add(p, q);
                    break;
                case 2:
                    bobNum++;
                    bob.add(p, q);
                    break;
                case 3:
                    commonNum++;
                    aliceNum++;
                    bobNum++;
                    alice.add(p, q);
                    bob.add(p, q);
                    break;
            }
        }

        if (alice.getCnt() > 1 || bob.getCnt() > 1) {
            return -1;
        }

        // alice bob 冗余边
        int aliceExtras = Math.max(0, aliceNum - n + 1), bobExtras = Math.max(0, bobNum - n + 1);
        // 在属于自己的边中尽可能删除冗余边
        result += Math.min(aliceExtras, aliceNum - commonNum);
        result += Math.min(bobExtras, bobNum - commonNum);
        int aExCom = aliceExtras - aliceNum + commonNum, bExCom = bobExtras - bobNum + commonNum;
        result += Math.max(0, Math.min(aExCom, bExCom));

        return result;
    }

    private static class UnionFind {
        private final int[] parent;
        private int cnt;

        public UnionFind(int num) {
            this.parent = new int[num];
            this.cnt = num;
            for (int i = 0; i < num; i++) {
                this.parent[i] = i;
            }
        }

        public boolean add(int p, int q) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return false;
            }
            parent[qRoot] = pRoot;
            cnt--;
            return true;
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
    }
}
