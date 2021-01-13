package everyday;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: 684
 * Link:    https://leetcode-cn.com/problems/redundant-connection/
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/13 上午10:47
 */
public class RedundantConnection {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(
                new UnionFindSolution().findRedundantConnection(
                        new int[][]{
                                {1, 2},
                                {1, 3},
                                {2, 3}
                        }
                )
        ));
    }

    /**
     * 并查集 <br />
     * <blockquote>
     * 时间、空间复复杂度更低
     * </blockquote>
     * <p>
     * 思路：
     * <li>当要为图中的两个点连线时，如果此时这两个点在一个连通分量，那么再连的这一条线无疑是冗余的</li>
     */
    private static class UnionFindSolution {
        public int[] findRedundantConnection(int[][] edges) {
            final int nodeNum = edges.length;
            UnionFind unionFind = new UnionFind(nodeNum);
            for (int[] edge : edges) {
                if (!unionFind.add(edge[0] - 1, edge[1] - 1)) {
                    return edge;
                }
            }
            return new int[0];
        }

        private static class UnionFind {
            private final int[] parent;

            public UnionFind(int num) {
                this.parent = new int[num];
                for (int i = 0; i < num; i++) {
                    this.parent[i] = i;
                }
            }

            public boolean add(int p, int q) {
                int pRoot = find(p);
                int qRoot = find(q);
                if (pRoot == qRoot) {
                    return false;
                }
                parent[pRoot] = qRoot;
                return true;
            }

            public int find(int node) {
                // 这里使用路径压缩，因为只关注两个节点属不属于一个连通分量
                while (parent[node] != node) {
                    parent[node] = parent[parent[node]];
                    node = parent[node];
                }
                return node;
            }
        }
    }

    /**
     * 拓扑排序
     */
    private static class TopologicalSolution {

        /**
         * 思路：分为三步
         * <ol>
         *     <li>使用拓扑排序的大致方法，找到图中的环</li>
         *     <li>获取环中的边</li>
         *     <li>根据边在数组中出现的位置，删除最后出现的边</li>
         * </ol>
         *
         * @param edges 图的边
         * @return 冗余边
         */
        public int[] findRedundantConnection(int[][] edges) {
            // 只有一条冗余链路，意味着Num(节点) == Num(edge)
            final int nodeNum = edges.length;
            // 节点度数
            final int[] nodeDegree = new int[nodeNum + 1];
            final Queue<Integer> singleDegreeQueue = new LinkedList<>();
            // 构建临接矩阵
            int[][] adjMatrix = new int[nodeNum + 1][nodeNum + 1];
            for (int[] edge : edges) {
                adjMatrix[edge[0]][edge[1]] = 1;
                adjMatrix[edge[1]][edge[0]] = 1;
            }
            for (int i = 1; i <= nodeNum; i++) {
                nodeDegree[i] = Arrays.stream(adjMatrix[i]).sum();
                if (nodeDegree[i] == 1) {
                    singleDegreeQueue.offer(i);
                }
            }

            // 寻找环
            while (!singleDegreeQueue.isEmpty()) {
                int rmNode = singleDegreeQueue.poll();
                for (int i = 1; i <= nodeNum; i++) {
                    // 入度处理
                    if (i != rmNode && adjMatrix[rmNode][i] != 0) {
                        nodeDegree[i]--;
                        if (nodeDegree[i] == 1) {
                            singleDegreeQueue.offer(i);
                        }
                    }
                    // 临接矩阵更新
                    adjMatrix[rmNode][i] = 0;
                    adjMatrix[i][rmNode] = 0;
                }
                nodeDegree[rmNode] = 0;
            }

            // 寻找最后出现的边
            for (int i = edges.length - 1; i >= 0; i--) {
                if (adjMatrix[edges[i][0]][edges[i][1]] != 0) {
                    return edges[i];
                }
            }

            return new int[0];
        }

    }

}
