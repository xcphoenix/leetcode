package everyday;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static utils.JsonUtils.fromJson;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/22 上午10:23
 */
public class RecorderRoutesToMakeAllPathsLeadToTheCityZero {

    @Test
    void testSolution() {
        Assertions.assertEquals(0, minReorder(3, fromJson("[[1,0],[2,0]]", int[][].class)));
        Assertions.assertEquals(3, minReorder(6, fromJson("[[0,1],[1,3],[2,3],[4,0],[4,5]]", int[][].class)));
        Assertions.assertEquals(2, minReorder(5, fromJson("[[1,0],[1,2],[3,2],[3,4]]", int[][].class)));
    }

    public int minReorder(int n, int[][] connections) {
        // 只有 n - 1 条线路，意味着两个节点之间有且仅有一条路线
        Map<Integer, List<Integer>> src2Trg = new HashMap<>(n), trg2Src = new HashMap<>(n);
        Queue<Integer> centerQueue = new LinkedList<>();
        centerQueue.offer(0);
        int reversedNum = 0;
        for (int[] connection : connections) {
            src2Trg.computeIfAbsent(connection[0], e -> new ArrayList<>()).add(connection[1]);
            trg2Src.computeIfAbsent(connection[1], e -> new ArrayList<>()).add(connection[0]);
        }

        Set<Integer> ordered = new HashSet<>(n);
        while (!centerQueue.isEmpty()) {
            int center = centerQueue.poll();
            if (ordered.contains(center)) {
                continue;
            }
            // 记录
            ordered.add(center);
            // 目标点指向的所有城市，而这些城市无法到达目标点，需要更改路线
            List<Integer> needReverseCities = src2Trg.getOrDefault(center, Collections.emptyList());
            centerQueue.addAll(trg2Src.getOrDefault(center, Collections.emptyList()));
            // 改变方向，原来的 needReverseCities 成为线路的起始点，那么以这些点为起点的线路需要逆置
            for (Integer needReverseCity : needReverseCities) {
                if (ordered.contains(needReverseCity)) {
                    continue;
                }
                centerQueue.add(needReverseCity);
                reversedNum++;
            }
        }

        return reversedNum;
    }

}
