package everyday;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/31 上午10:25
 */
public class SimilarStringGroups {

    @Test
    void testSolution() {
        // assertEquals(2, numSimilarGroups(fromJson("[\"tars\",\"rats\",\"arts\",\"star\"]", String[].class)));
        // assertEquals(1, numSimilarGroups(fromJson("[\"omv\",\"ovm\"]", String[].class)));
        assertEquals(5, numSimilarGroups(fromJson("[\"kccomwcgcs\",\"socgcmcwkc\",\"sgckwcmcoc\",\"coswcmcgkc\",\"cowkccmsgc\",\"cosgmccwkc\",\"sgmkwcccoc\",\"coswmccgkc\",\"kowcccmsgc\",\"kgcomwcccs\"]", String[].class)));
    }

    public int numSimilarGroups(String[] strs) {
        final int strNum = strs.length, strLen = strs[0].length();
        UnionFind unionFind = new UnionFind(strNum);
        Map<String, Integer> str2Root = new HashMap<>(strNum);

        for (int i = 0; i < strNum; i++) {
            String str = strs[i];
            for (Map.Entry<String, Integer> entry : str2Root.entrySet()) {
                String k = entry.getKey();
                int v = entry.getValue(), errNum = 0;
                if (unionFind.find(v) == unionFind.find(i)) {
                    continue;
                }
                for (int m = 0; m < strLen && errNum < 3; m++) {
                    if (k.charAt(m) != str.charAt(m)) {
                        errNum++;
                    }
                }
                if (errNum < 3) {
                    unionFind.unite(i, v);
                }
            }
            str2Root.put(str, unionFind.find(i));
        }

        return unionFind.getCnt();
    }

    private static class UnionFind {
        private final int[] parent;
        private int cnt;

        public UnionFind(int num) {
            this.parent = new int[num];
            for (int i = 0; i < num; i++) {
                this.parent[i] = i;
            }
            this.cnt = num;
        }

        public void unite(int q, int p) {
            int qRoot = find(q), pRoot = find(p);
            if (qRoot == pRoot) {
                return;
            }
            parent[qRoot] = pRoot;
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
    }

}
