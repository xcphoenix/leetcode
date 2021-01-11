package everyday;

import java.util.*;

/**
 * Problem: 1202<br />
 * Link:    https://leetcode-cn.com/problems/smallest-string-with-swaps/
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/11 上午9:51
 */
public class SmallestStringWithSwaps {

    public static void main(String[] args) {
        List<List<Integer>> pairs = new ArrayList<>();
        pairs.add(Arrays.asList(5, 3));
        pairs.add(Arrays.asList(3, 0));
        pairs.add(Arrays.asList(5, 1));
        pairs.add(Arrays.asList(1, 1));
        pairs.add(Arrays.asList(1, 5));
        pairs.add(Arrays.asList(3, 0));
        pairs.add(Arrays.asList(0, 2));
        String str = new Solution2().smallestStringWithSwaps("pwqlmqm", pairs);
        System.out.println(str);
    }

    private static class Solution2 {
        public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
            if (pairs == null || pairs.size() == 0) {
                return s;
            }
            final int len = s.length();

            UnionFind unionFind = new UnionFind(len);
            pairs.forEach(pair -> unionFind.union(pair.get(0), pair.get(1)));

            char[] str = s.toCharArray();
            Map<Integer, Queue<Character>> root2Queue = new HashMap<>(len);
            for (int i = 0; i < len; i++) {
                int root = unionFind.find(i);
                root2Queue.computeIfAbsent(root, e -> new PriorityQueue<>()).offer(str[i]);
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                int root = unionFind.find(i);
                stringBuilder.append(root2Queue.get(root).poll());
            }

            return stringBuilder.toString();
        }
    }

    /**
     * 并查集
     */
    private static class UnionFind {

        /**
         * 连通分量个数
         */
        private int count = 0;
        /**
         * 父节点下标，根节点是他自己
         */
        private final int[] parent;
        /**
         * 以指定下标为根节点的树的高度
         */
        private final int[] size;

        public UnionFind(int n) {
            this.count = n;
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public void union(int p, int q) {
            int pRoot = find(p);
            int qRoot = find(q);
            if (pRoot == qRoot) {
                return;
            }
            if (size[pRoot] == size[qRoot]) {
                parent[pRoot] = qRoot;
                size[qRoot]++;
            } else {
                int lowRoot = size[pRoot] > size[qRoot] ? qRoot : pRoot;
                int highRoot = size[pRoot] > size[qRoot] ? pRoot : qRoot;
                parent[lowRoot] = highRoot;
            }
            count--;
        }

        public int find(int x) {
            while (parent[x] != x) {
                x = parent[x];
            }
            return x;
        }

        public int count() {
            return count;
        }

    }

    private static class Solution {

        private final static int ITEM_SIZE = 'z' - 'a' + 1;

        public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
            if (pairs == null || pairs.size() == 0) {
                return s;
            }
            List<Set<Integer>> linkableSetList = new ArrayList<>();
            pairs.forEach(pair -> putMatchSet(linkableSetList, pair));
            return arrayStr(linkableSetList, s);
        }

        private String arrayStr(List<Set<Integer>> linkableSetList, String s) {
            int[] tub = new int[ITEM_SIZE];
            char[] str = s.toCharArray();
            linkableSetList.forEach(set -> {
                set.forEach(val -> tub[str[val] - 'a']++);
                int pTub = 0, pDealIdx = 0;
                final Integer[] dealIdxList = set.toArray(new Integer[0]);
                while (pTub < tub.length) {
                    if (tub[pTub] > 0) {
                        str[dealIdxList[pDealIdx]] = (char) (pTub + 'a');
                        pDealIdx++;
                        tub[pTub]--;
                    } else {
                        pTub++;
                    }
                }
                Arrays.fill(tub, 0);
            });
            return new String(str);
        }

        private void putMatchSet(List<Set<Integer>> setList, List<Integer> valList) {
            List<Set<Integer>> matchSetList = new ArrayList<>();
            for (Set<Integer> set : setList) {
                if (valList.stream().anyMatch(set::contains)) {
                    matchSetList.add(set);
                }
            }
            if (matchSetList.size() == 0) {
                setList.add(new TreeSet<>(valList));
            } else if (matchSetList.size() == 1) {
                matchSetList.get(0).addAll(valList);
            } else {
                matchSetList.forEach(setList::remove);
                Set<Integer> matchedSet = new TreeSet<>();
                matchSetList.forEach(matchedSet::addAll);
                matchedSet.addAll(valList);
                setList.add(matchedSet);
            }
        }
    }


}
