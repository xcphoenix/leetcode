package everyday;

import org.junit.jupiter.api.Test;

import java.util.*;

import static utils.JsonUtils.echoJson;
import static utils.JsonUtils.fromJson;

/**
 * Problem: 721 <br />
 * Link:    https://leetcode-cn.com/problems/accounts-merge/ <br />
 * Level:   Medium <br />
 * Tag:     并查集
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/18 上午9:45
 */
public class AccountsMerge {

    @Test
    void testSolution() {
        String json = "[[\"John\", \"johnsmith@mail.com\", \"john00@mail.com\"], [\"John\", \"johnnybravo@mail.com\"], [\"John\", \"johnsmith@mail.com\", \"john_newyork@mail.com\"], [\"Mary\", \"mary@mail.com\"]]";
        @SuppressWarnings("unchecked") List<List<String>> accounts = fromJson(json, List.class);
        echoJson(accountsMerge(accounts));
    }

    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        final int accountNum = accounts.size();
        Map<String, Integer> email2User = new HashMap<>();
        UnionFind unionFind = new UnionFind(accountNum);

        // 并查集合并
        for (int i = 0; i < accounts.size(); i++) {
            List<String> account = accounts.get(i);
            for (int j = 1; j < account.size(); j++) {
                String email = account.get(j);
                if (email2User.containsKey(email)) {
                    unionFind.add(email2User.get(email), i);
                } else {
                    email2User.put(email, i);
                }
            }
        }

        // 数据合并
        Map<Integer, Set<String>> root2List = new HashMap<>(unionFind.getCount());
        List<List<String>> res = new ArrayList<>(unionFind.getCount());
        for (int i = 0; i < accounts.size(); i++) {
            // 这里使用 TreeSet 实现去重和排序，但代价是需要额外的空间
            Set<String> data = root2List.computeIfAbsent(unionFind.find(i), e -> new TreeSet<>());
            data.addAll(accounts.get(i).subList(1, accounts.get(i).size()));
        }

        root2List.forEach((k, v) -> {
            List<String> data = new ArrayList<>();
            data.add(accounts.get(k).get(0));
            data.addAll(v);
            res.add(data);
        });

        return res;
    }

    private static class UnionFind {
        private final int[] parent;
        private int count;

        public UnionFind(int num) {
            this.parent = new int[num];
            this.count = num;
            for (int i = 0; i < num; i++) {
                this.parent[i] = i;
            }
        }

        public void add(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return;
            }
            // 索引小的优先，不是必须，因为前面用 map 去统一维护，无所谓节点的选择策略
            int priorityRoot = Math.min(qRoot, pRoot), behindRoot = Math.max(qRoot, pRoot);
            parent[behindRoot] = priorityRoot;
            count--;
        }

        public int find(int node) {
            // 启用路径压缩
            while (parent[node] != node) {
                parent[node] = find(parent[node]);
                node = parent[node];
            }
            return node;
        }

        public int getCount() {
            return count;
        }
    }

}
