package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

/**
 * <pre>
 *  Problem: 2
 *  Link:    https://leetcode-cn.com/problems/add-two-numbers/
 *  Level:   Medium
 * </pre>
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/14 上午10:14
 */
public class AddTwoNumbers {

    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        public static ListNode build(int[] arr) {
            final ListNode root = new ListNode();
            if (arr == null || arr.length == 0) {
                return root;
            }
            root.val = arr[0];
            ListNode node = root;
            for (int i = 1; i < arr.length; i++) {
                node.next = new ListNode(arr[i]);
                node = node.next;
            }
            return root;
        }
    }

    @Test
    void testSolution() {
        System.out.println(JsonUtils.toJson(addTwoNumbers(
                ListNode.build(new int[]{9}),
                ListNode.build(new int[]{9})
        )));
    }

    /**
     * PS: pRemain的循环可以合并到第一个循环中，Leetcode不支持Optional就算了，效率差不多
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int sum = l1.val + l2.val, carry = sum / 10;
        final ListNode resListNode = new ListNode(sum % 10);
        ListNode nowNode = resListNode, pOne = l1.next, pTwo = l2.next;
        while (pOne != null && pTwo != null) {
            sum = carry + pOne.val + pTwo.val;
            carry = sum / 10;
            nowNode.next = new ListNode(sum % 10);
            nowNode = nowNode.next;
            pOne = pOne.next;
            pTwo = pTwo.next;
        }
        ListNode pRemain = pOne == null ? pTwo : pOne;
        while (pRemain != null) {
            sum = carry + pRemain.val;
            carry = sum / 10;
            nowNode.next = new ListNode(sum % 10);
            nowNode = nowNode.next;
            pRemain = pRemain.next;
        }
        if (carry > 0) {
            nowNode.next = new ListNode(carry);
        }
        return resListNode;
    }


}
