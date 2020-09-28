package com.airwallex;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Unit test for RPNCalc.
 */
public class RPNCalcTest {
    Boolean checkCalcItems(ArrayList<CalcItem> expected, ArrayList<CalcItem> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }

        for (int i = 0; i < expected.size(); ++i) {
            if ((expected.get(i).getIndex() != actual.get(i).getIndex())
                    || (!expected.get(i).getValue().equals(actual.get(i).getValue()))) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testSplitStr() {
        String str = "5 3.555 + 6 * Undo";
        ArrayList<CalcItem> expected = new ArrayList<>();
        expected.add(new CalcItem(1, "5"));
        expected.add(new CalcItem(3, "3.555"));
        expected.add(new CalcItem(9, "+"));
        expected.add(new CalcItem(11, "6"));
        expected.add(new CalcItem(13, "*"));
        expected.add(new CalcItem(15, "undo"));

        ArrayList<CalcItem> actual = RPNCalc.splitStr(str);

        assertTrue(checkCalcItems(expected, actual));
    }

    @Test
    public void testToFormatString() {
        Stack<Double> stack = new Stack<>();
        stack.add(3.111111111111111111111);
        stack.add(3d);
        stack.add(0.333);
        stack.add(3.8888888888888888888);

        String expected_str = "stack: 3.1111111111 3 0.333 3.8888888889";
        String actual_str = RPNCalc.toFormatString(stack);
        assertEquals(expected_str, actual_str);
    }
}
