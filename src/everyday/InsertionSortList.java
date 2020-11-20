package everyday;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/20 下午1:26
 * @see <a href="https://leetcode-cn.com/problems/insertion-sort-list/">147</a>
 */
public class InsertionSortList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

    public static ListNode insertionSortList(ListNode head) {
        if (head == null) {
            return head;
        }
        // 虚拟节点
        final ListNode virtualNode = new ListNode(0);
        virtualNode.next = head;
        // 第一个开始是有序的
        ListNode sortedNode = head;
        ListNode nextItem;
        while ((nextItem = sortedNode.next) != null) {
            if (nextItem.val >= sortedNode.val) {
                sortedNode = sortedNode.next;
            } else {
                // 开始重头遍历
                ListNode it = virtualNode;
                while (it.next.val <= nextItem.val) {
                    it = it.next;
                }
                sortedNode.next = nextItem.next;
                nextItem.next = it.next;
                it.next = nextItem;
            }
        }
        return virtualNode.next;
    }

    public static void main(String[] args) {
        ListNode root = new ListNode(-1);
        ListNode node = root;
        node.next = new ListNode(5);
        node = node.next;
        node.next = new ListNode(3);
        node = node.next;
        node.next = new ListNode(4);
        node = node.next;
        node.next = new ListNode(0);
        node = node.next;
        ListNode orderHead = insertionSortList(root);
        System.out.println(orderHead);
    }

}
