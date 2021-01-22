package everyday;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static utils.JsonUtils.toJson;

/**
 * Problem: 989 <br />
 * Link:    https://leetcode-cn.com/problems/add-to-array-form-of-integer/submissions/ <br />
 * Medium:  Simple <br />
 * Tag:     数组
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/22 上午9:49
 */
public class AddToArrayFormOfInteger {

    @Test
    void testSolution() {
        Assertions.assertEquals("[1,2,3,4]", toJson(addToArrayForm(new int[]{1, 2, 0, 0}, 34)));
        Assertions.assertEquals("[4,5,5]", toJson(addToArrayForm(new int[]{2, 7, 4}, 181)));
        Assertions.assertEquals("[1,0,2,1]", toJson(addToArrayForm(new int[]{2, 1, 5}, 806)));
        Assertions.assertEquals("[1,0,0,0,0,0,0,0,0,0,0]", toJson(addToArrayForm(new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, 1)));
        Assertions.assertEquals("[1]", toJson(addToArrayForm(new int[]{}, 1)));
        Assertions.assertEquals("[1]", toJson(addToArrayForm(new int[]{1}, 0)));
        Assertions.assertEquals("[0]", toJson(addToArrayForm(new int[]{0}, 0)));
    }


    public List<Integer> addToArrayForm(int[] A, int K) {
        List<Integer> result = new ArrayList<>();
        int carry = 0;
        for (int i = A.length - 1; i >= 0 || K > 0; i--) {
            int sum = (i >= 0 ? A[i] : 0) + (K > 0 ? K % 10 : 0) + carry;
            if (sum > 9) {
                carry = 1;
                result.add(sum - 10);
            } else {
                carry = 0;
                result.add(sum);
            }
            K /= 10;
        }
        if (carry != 0) {
            result.add(carry);
        }
        Collections.reverse(result);
        return result;
    }

}
