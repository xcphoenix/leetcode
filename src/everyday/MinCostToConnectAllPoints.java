package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Problem: 1584 <br />
 * Link:    https://leetcode-cn.com/problems/min-cost-to-connect-all-points/ <br />
 * Level:   Medium <br />
 * Tag:
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/19 上午10:23
 */
public class MinCostToConnectAllPoints {

    @Test
    void testSolution() {
        String json = "[[3,12],[-2,5],[-4,1]]";
        int[][] points = JsonUtils.fromJson(json, int[][].class);
        JsonUtils.echoJson(minCostConnectPoints(points));
    }

    /**
     * PS: 不用Map.Entry快好多... 直接快一小半
     */
    public int minCostConnectPoints(int[][] points) {
        final int pointNum = points.length;
        UnionFind unionFind = new UnionFind(pointNum);
        int minCost = 0;
        List<Line> lineList = new ArrayList<>();
        for (int i = 0; i < pointNum - 1; i++) {
            for (int j = i + 1; j < pointNum; j++) {
                lineList.add(new Line(i, j, Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1])));
            }
        }
        lineList.sort(Comparator.comparingInt(value -> value.weight));
        // Kruskal
        int linkedLine = 0;
        for (Line line : lineList) {
            int pointA = line.pointA, pointB = line.pointB;
            if (unionFind.add(pointA, pointB)) {
                linkedLine++;
                minCost += line.weight;
            }
            if (linkedLine == pointNum - 1) {
                break;
            }
        }
        return minCost;
    }

    private static class Line {
        private final int pointA;
        private final int pointB;
        private final int weight;

        public Line(int pointA, int pointB, int weight) {
            this.pointA = pointA;
            this.pointB = pointB;
            this.weight = weight;
        }
    }

    private static class UnionFind {
        private final int[] parent;

        private UnionFind(int num) {
            this.parent = new int[num];
            for (int i = 0; i < num; i++) {
                this.parent[i] = i;
            }
        }

        private boolean add(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return false;
            }
            parent[qRoot] = pRoot;
            return true;
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
