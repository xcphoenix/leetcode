package everyday;

import org.junit.jupiter.api.Test;

import java.util.*;

import static utils.JsonUtils.echoJson;
import static utils.JsonUtils.fromJson;

/**
 * Problem: 1489 <br />
 * Link:    https://leetcode-cn.com/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/ <br />
 * Level:   Hard <br />
 * Tag:     并查集
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/21 上午9:28
 */
public class FindCriticalAndPseudoCriticalEdgesInMinimumSpanningTree {

    @Test
    void testKruskal() {
        // echoJson(kruskal(edges, 5));
    }

    @Test
    void testSolution() {
        String json = "[[0,1,1],[1,2,1],[2,3,2],[0,3,2],[0,4,3],[3,4,3],[1,4,6]]";
        int n = 5;
        int[][] edges = fromJson(json, int[][].class);
        echoJson(findCriticalAndPseudoCriticalEdges(n, edges));
    }

    private int accEdge = -1;
    private int[][] globalEdges;

    public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
        this.globalEdges = edges;
        Queue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(o -> globalEdges[o][2]));
        List<Integer> keyEdgeIdx = new ArrayList<>(), fakeKeyEdgeIdx = new ArrayList<>();
        List<Integer> edgeList = new ArrayList<>(edges.length);
        for (int i = 0, edgesLength = edges.length; i < edgesLength; i++) {
            edgeList.add(i);
        }

        final int mst = kruskal(edgeList, queue, n, -1);
        for (int i = 0; i < edgeList.size(); i++) {
            int nowMst = kruskal(edgeList, queue, n, i);
            if (nowMst > mst || nowMst == -1) {
                keyEdgeIdx.add(i);
            }
        }

        for (int i = 0; i < edgeList.size(); i++) {
            if (keyEdgeIdx.contains(i)) {
                continue;
            }
            accEdge = i;
            int nowMst = kruskal(edgeList, queue, n, -1);
            if (nowMst == mst) {
                fakeKeyEdgeIdx.add(i);
            }
        }

        return Arrays.asList(keyEdgeIdx, fakeKeyEdgeIdx);
    }

    private int kruskal(List<Integer> edges, Queue<Integer> queue, int nodeNum, int ignored) {
        int msl = 0;
        queue.clear();
        for (int i = 0; i < edges.size(); i++) {
            if (i != ignored) {
                queue.offer(edges.get(i));
            }
        }
        UnionFind unionFind = new UnionFind(nodeNum);
        if (accEdge >= 0) {
            msl += globalEdges[accEdge][2];
            unionFind.add(globalEdges[accEdge][0], globalEdges[accEdge][1]);
        }

        while (!queue.isEmpty()) {
            int[] edge = globalEdges[queue.poll()];
            if (unionFind.add(edge[0], edge[1])) {
                msl += edge[2];
            }
            if (unionFind.getCnt() == 1) {
                break;
            }
        }
        return unionFind.getCnt() == 1 ? msl : -1;
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
            int pRoot = find(p), qRoot = find(q);
            if (pRoot == qRoot) {
                return false;
            }
            parent[pRoot] = qRoot;
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
