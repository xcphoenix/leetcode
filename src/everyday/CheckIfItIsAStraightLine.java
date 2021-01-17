package everyday;

import org.junit.jupiter.api.Test;
import utils.JsonUtils;

import java.util.Arrays;

/**
 * Problem: 1232 <br />
 * Link:    https://leetcode-cn.com/problems/check-if-it-is-a-straight-line/ <br />
 * LeveL:   Simple
 *
 * @author xuanc
 * @version 1.0
 * @date 2021/1/17 上午9:23
 */
public class CheckIfItIsAStraightLine {

    @Test
    void testSolution() {
        String json = "[[0,0],[1, 0],[2, 0],[5, 0]]";
        int[][] input = JsonUtils.fromJson(json, int[][].class);
        JsonUtils.echoJson(checkStraightLine(input));
    }

    public boolean checkStraightLine(int[][] coordinates) {
        if (coordinates[1][0] - coordinates[0][0] == 0) {
            return Arrays.stream(coordinates).noneMatch(coordinate -> coordinate[0] != coordinates[0][0]);
        }
        final double k = 1.0 * (coordinates[1][1] - coordinates[0][1]) / (coordinates[1][0] - coordinates[0][0]);
        final double b = coordinates[0][1] - k * coordinates[0][0];
        for (int i = 2; i < coordinates.length; i++) {
            if ((int) (k * coordinates[i][0] + b - coordinates[i][1]) != 0) {
                return false;
            }
        }
        return true;
    }

}
