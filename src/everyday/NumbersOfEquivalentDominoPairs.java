package everyday;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/26 上午9:01
 */
public class NumbersOfEquivalentDominoPairs {

    @Test
    void testSolution() {
        assertEquals(3, numEquivDominoPairs2(fromJson("[[1,2],[1,2],[1,1],[1,2],[2,2]]", int[][].class)));
        assertEquals(1, numEquivDominoPairs2(fromJson("[[1,2],[2,1],[3,4],[5,6]]", int[][].class)));
    }

    /**
     * 因为每个数<=9，可以把两个数拼成一个十位数字来处理
     */
    public int numEquivDominoPairs2(int[][] dominoes) {
        int[] map = new int[100];
        int result = 0;
        for (int[] dominoe : dominoes) {
            int key = dominoe[0] < dominoe[1] ? dominoe[0] * 10 + dominoe[1] : dominoe[1] * 10 + dominoe[0];
            map[key]++;
            result += map[key] - 1;
        }
        return result;
    }

    public int numEquivDominoPairs(int[][] dominoes) {
        int result = 0;
        Map<Node, Integer> map = new HashMap<>(16);
        for (int[] dominoe : dominoes) {
            Node node = new Node(dominoe[0], dominoe[1]);
            map.put(node, map.getOrDefault(node, 0) + 1);
        }
        for (Map.Entry<Node, Integer> nodeIntegerEntry : map.entrySet()) {
            int value = nodeIntegerEntry.getValue();
            if (value > 1) {
                result += 1.0 * (value - 1) * (value) / 2;
            }
        }
        return result;
    }

    private static class Node {
        private final int a, b;

        public Node(int a, int b) {
            this.a = Math.max(a, b);
            this.b = Math.min(a, b);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return a == node.a && b == node.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

}
