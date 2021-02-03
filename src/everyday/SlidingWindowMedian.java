package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

import java.util.*;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/2/3 上午9:49
 */
public class SlidingWindowMedian {

    @Test
    void testSolution() {
        JsonUtils.echoJson(medianSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        JsonUtils.echoJson(medianSlidingWindow(new int[]{1, 4, 2, 3}, 4));
        JsonUtils.echoJson(medianSlidingWindow(new int[]{2147483647, 2147483647}, 2));
    }


    /**
     * 暴力勉强过
     */
    public double[] medianSlidingWindow(int[] nums, int k) {
        if (nums.length == 0) {
            return new double[0];
        }
        final int num = nums.length, ansLen = nums.length - k + 1;
        double[] ans = new double[ansLen];
        List<int[]> list = new ArrayList<>(k);
        for (int i = 0; i < k - 1; i++) {
            list.add(new int[]{i, nums[i]});
        }
        int left = 0;
        while (left + k <= num) {
            list.add(new int[]{left + k - 1, nums[left + k - 1]});
            list.sort(Comparator.comparingInt(v -> v[1]));
            int tmpMid = k / 2;
            ans[left] = k % 2 != 0 ? list.get(tmpMid)[1] : (1.0 * list.get(tmpMid)[1] + list.get(tmpMid - 1)[1]) / 2;
            left++;
            int finalLeft = left - 1;
            list.removeIf(v -> v[0] == finalLeft);
        }
        return ans;
    }

    /**
     * 官方题解
     */
    class Solution {
        public double[] medianSlidingWindow(int[] nums, int k) {
            DualHeap dh = new DualHeap(k);
            for (int i = 0; i < k; ++i) {
                dh.insert(nums[i]);
            }
            double[] ans = new double[nums.length - k + 1];
            ans[0] = dh.getMedian();
            for (int i = k; i < nums.length; ++i) {
                dh.insert(nums[i]);
                dh.erase(nums[i - k]);
                ans[i - k + 1] = dh.getMedian();
            }
            return ans;
        }
    }

    @SuppressWarnings("ConstantConditions")
    static class DualHeap {
        // 大根堆，维护较小的一半元素
        private final PriorityQueue<Integer> small;
        // 小根堆，维护较大的一半元素
        private final PriorityQueue<Integer> large;
        // 哈希表，记录「延迟删除」的元素，key 为元素，value 为需要删除的次数
        private final Map<Integer, Integer> delayed;

        private final int k;
        // small 和 large 当前包含的元素个数，需要扣除被「延迟删除」的元素
        private int smallSize, largeSize;

        public DualHeap(int k) {
            // 大根堆，逆序
            this.small = new PriorityQueue<>(Comparator.reverseOrder());
            this.large = new PriorityQueue<>(Comparator.naturalOrder());
            this.delayed = new HashMap<>();
            this.k = k;
            this.smallSize = 0;
            this.largeSize = 0;
        }

        /**
         * 获取中位数
         */
        public double getMedian() {
            return (k & 1) == 1 ? small.peek() : ((double) small.peek() + large.peek()) / 2;
        }

        public void insert(int num) {
            if (small.isEmpty() || num <= small.peek()) {
                small.offer(num);
                ++smallSize;
            } else {
                large.offer(num);
                ++largeSize;
            }
            // 由于插入的策略是优先Small，需维护平衡，使得 0 <= smallSize - largeSize <= 1，来保证中位数获取的正确性
            makeBalance();
        }

        public void erase(int num) {
            // 标记延迟删除
            delayed.put(num, delayed.getOrDefault(num, 0) + 1);
            if (num <= small.peek()) {
                --smallSize;
                // 如果是堆顶元素，执行删除操作
                if (num == small.peek()) {
                    prune(small);
                }
            } else {
                --largeSize;
                if (num == large.peek()) {
                    prune(large);
                }
            }
            // 删除可能会破坏平衡
            makeBalance();
        }

        // 不断地弹出 heap 的堆顶元素，并且更新哈希表
        private void prune(PriorityQueue<Integer> heap) {
            // 删除在堆顶的被标记为延迟删除的元素
            while (!heap.isEmpty()) {
                int num = heap.peek();
                if (delayed.containsKey(num)) {
                    delayed.put(num, delayed.get(num) - 1);
                    if (delayed.get(num) == 0) {
                        delayed.remove(num);
                    }
                    heap.poll();
                } else {
                    break;
                }
            }
        }

        // 调整 small 和 large 中的元素个数，使得二者的元素个数满足要求
        private void makeBalance() {
            if (smallSize > largeSize + 1) {
                // small 比 large 元素多 2 个
                large.offer(small.poll());
                --smallSize;
                ++largeSize;
                // small 堆顶元素被移除，需要进行 prune
                prune(small);
            } else if (smallSize < largeSize) {
                // large 比 small 元素多 1 个
                small.offer(large.poll());
                ++smallSize;
                --largeSize;
                // large 堆顶元素被移除，需要进行 prune
                prune(large);
            }
        }
    }

}
