package everyday;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2021/1/14 上午9:41
 */
public class BinaryPrefixDivisibleByFive {

    private final Gson gson = new Gson();

    @Test
    void test5Bin() {
        for (int i = 0; 5 * i < 256; i++) {
            System.out.println(Integer.toBinaryString(5 * i));
        }
    }

    @Test
    void testSolution() {
        int[] A = new int[]{
                0, 1, 1
        };
        System.out.println(gson.toJson(prefixesDivBy5(A)));
    }

    public List<Boolean> prefixesDivBy5(int[] A) {
        final int num = A.length;
        int modRes = 0;
        List<Boolean> res = new ArrayList<>(num);
        for (int item : A) {
            modRes = (modRes << 1) + item;
            modRes %= 5;
            res.add(modRes == 0);
        }
        return res;
    }

}
