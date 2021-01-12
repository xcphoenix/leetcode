package everyday;

import java.util.*;

/**
 * Problem: 1203
 * Link:    https://leetcode-cn.com/problems/sort-items-by-groups-respecting-dependencies/
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/12 上午9:37
 */
public class SortItemsByGroupsRespectingDependencies {

    public static void main(String[] args) {
        List<List<Integer>> items = new ArrayList<>();
        items.add(Arrays.asList(2, 1, 3));
        items.add(Arrays.asList(2, 4));
        items.add(Arrays.asList());
        items.add(Arrays.asList());
        items.add(Arrays.asList());
        System.out.println(
                Arrays.toString(
                        new everyday.SortItemsByGroupsRespectingDependencies.Solution().sortItems(5, 5, new int[]{2, 0, -1, 3, 0}, items)
                )
        );
    }

    private static class Solution {
        public int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
            // 1. 预处理，由于-1表示项目没有被列出的组所分配，每个都是不一样的，所以将其值设置为不同的值
            for (int i = 0; i < group.length; i++) {
                if (group[i] == -1) {
                    group[i] = m;
                    m++;
                }
            }

            // 2. 分别对组和项目执行拓扑排序
            Graph grpGraph = new Graph(m);
            for (int i = 0; i < n; i++) {
                int curGrp = group[i];
                for (Integer item : beforeItems.get(i)) {
                    int depGrp = group[item];
                    if (depGrp != curGrp) {
                        grpGraph.addLine(depGrp, curGrp);
                    }
                }
            }
            List<Integer> grpRes = grpGraph.topologicalSort();
            if (grpRes.size() == 0) {
                return new int[0];
            }

            Graph itemGraph = new Graph(n);
            for (int i = 0; i < n; i++) {
                for (Integer item : beforeItems.get(i)) {
                    itemGraph.addLine(item, i);
                }
            }
            List<Integer> itemRes = itemGraph.topologicalSort();
            if (itemRes.size() == 0) {
                return new int[0];
            }

            // 组的优先级更高，当先按照组的优先级进行排序后，对于每个组，按照项目的优先级排列
            // 这个过程中需要知道，对于一个组，他包含的项目有哪些
            Map<Integer, List<Integer>> grp2Item = new HashMap<>(m);
            for (Integer item : itemRes) {
                grp2Item.computeIfAbsent(group[item], e -> new ArrayList<>()).add(item);
            }

            // 对于每个组，按照项目的优先级排列
            int pRes = 0;
            int[] sortRes = new int[n];
            for (Integer grp : grpRes) {
                List<Integer> items = grp2Item.getOrDefault(grp, new ArrayList<>());
                for (Integer item : items) {
                    sortRes[pRes] = item;
                    pRes++;
                }
                ;
            }

            return sortRes;
        }

        private static class Graph {
            /* 入度 */
            private final int[] inDegree;
            /* 邻接表 */
            private final List<List<Integer>> adjTable;

            public Graph(int num) {
                this.inDegree = new int[num];
                this.adjTable = new ArrayList<>(num);
                for (int i = 0; i < num; i++) {
                    this.adjTable.add(new ArrayList<>());
                }
            }

            /**
             * 添加有向边 source -> target
             */
            public void addLine(int source, int target) {
                this.adjTable.get(source).add(target);
                inDegree[target]++;
            }

            /**
             * 拓扑排序
             *
             * @return 返回拓扑排序的结果，若图有环返回空
             */
            public List<Integer> topologicalSort() {
                int num = inDegree.length;
                List<Integer> res = new ArrayList<>(num);
                Queue<Integer> queue = new LinkedList<>();

                // 获取入度为0的点
                for (int i = 0; i < inDegree.length; i++) {
                    if (inDegree[i] == 0) {
                        queue.offer(i);
                    }
                }

                while (!queue.isEmpty()) {
                    int zeroNode = queue.poll();
                    res.add(zeroNode);
                    List<Integer> linkedNodes = adjTable.get(zeroNode);
                    for (Integer node : linkedNodes) {
                        inDegree[node]--;
                        if (inDegree[node] == 0) {
                            queue.offer(node);
                        }
                    }
                }

                if (res.size() == num) {
                    return res;
                }
                return Collections.emptyList();
            }
        }
    }

    /**
     * 暴力实现 超时 拓扑硬是变成了混在一起的 dfs
     */
    private static class ErrSolution {

        private Map<Integer, Node> idx2Node;
        private Map<Integer, Integer> grp2Num;

        public int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
            Graph graph = new Graph(n);
            idx2Node = new HashMap<>(n);
            grp2Num = new HashMap<>(m + 1);
            Arrays.stream(group).forEach(grp -> grp2Num.put(grp, grp2Num.getOrDefault(grp, 0) + 1));
            for (int i = 0; i < beforeItems.size(); i++) {
                Node target = new Node(i, group[i]);
                idx2Node.putIfAbsent(i, target);
                beforeItems.get(i).forEach(item -> {
                    Node sourceNode = new Node(item, group[item]);
                    idx2Node.putIfAbsent(item, sourceNode);
                    graph.addLine(sourceNode, target);
                });
            }
            // 由于-1的特殊性
            grp2Num.put(-1, 0);
            return graph.topologicalSort().stream().mapToInt(e -> e.val).toArray();
        }

        private class Node {
            int val;
            int group;

            public Node(int val, int group) {
                this.val = val;
                this.group = group;
            }
        }

        private class Graph {
            private final int num;
            private final List<List<Integer>> adjList;
            private final int[] entry;
            private List<Node> resNodeList;
            private final int[] invalidNode;

            public Graph(int num) {
                this.num = num;
                this.adjList = new ArrayList<>(num);
                for (int i = 0; i < num; i++) {
                    this.adjList.add(new ArrayList<>());
                }
                this.entry = new int[num];
                this.invalidNode = new int[num];
            }

            public void addLine(Node source, Node target) {
                this.adjList.get(source.val).add(target.val);
                entry[target.val]++;
            }

            public List<Node> topologicalSort() {
                // 入度为0的点
                List<Integer> zeroEntries = new ArrayList<>(num);
                List<Node> sortedNodes = new ArrayList<>(num);
                for (int i = 0; i < entry.length; i++) {
                    if (entry[i] == 0) {
                        zeroEntries.add(i);
                    }
                }

                dfs(-1, zeroEntries, sortedNodes);

                if (resNodeList == null || resNodeList.size() < num) {
                    return Collections.emptyList();
                } else {
                    return resNodeList;
                }
            }

            private void dfs(int lastGrp, List<Integer> zeroEntries, List<Node> sortedNodes) {
                if (zeroEntries.size() == 0) {
                    resNodeList = new ArrayList<>(sortedNodes);
                    return;
                }
                List<Integer> nowZeroEntries = new ArrayList<>(zeroEntries);
                for (Integer zeroEntry : nowZeroEntries) {
                    if (invalidNode[zeroEntry] > 0) {
                        continue;
                    }
                    Node rmNode = idx2Node.get(zeroEntry);
                    // 如果上一组还没排列完毕，这次的和上次的不同，就认为是非法排列
                    if (grp2Num.get(lastGrp) > 0 && lastGrp != rmNode.group) {
                        continue;
                    }
                    List<Integer> nodes = adjList.get(zeroEntry);
                    invalidNode[zeroEntry] = 1;
                    final int lastGrpNum = grp2Num.get(rmNode.group);
                    grp2Num.put(rmNode.group, Math.max(0, lastGrpNum - 1));
                    zeroEntries.remove(zeroEntry);
                    sortedNodes.add(rmNode);
                    // System.out.println(Arrays.toString(sortedNodes.stream().mapToInt(value -> value.val).toArray()));
                    nodes.forEach(node -> {
                        entry[node]--;
                        if (entry[node] == 0) {
                            zeroEntries.add(node);
                        }
                    });
                    dfs(rmNode.group, zeroEntries, sortedNodes);
                    invalidNode[zeroEntry] = 0;
                    grp2Num.put(rmNode.group, lastGrpNum);
                    zeroEntries.add(zeroEntry);
                    sortedNodes.remove(rmNode);
                    nodes.forEach(node -> {
                        if (entry[node] == 0) {
                            zeroEntries.remove(node);
                        }
                        entry[node]++;
                    });
                }
            }
        }
    }

}
