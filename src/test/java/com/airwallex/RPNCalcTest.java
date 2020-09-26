package com.airwallex;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Stack;


/**
 * Unit test for RPNCalc.
 */
public class RPNCalcTest {
    Boolean checkCalcItems(ArrayList<RPNCalc.CalcItem> expected, ArrayList<RPNCalc.CalcItem> actual) {
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
        ArrayList<RPNCalc.CalcItem> expected = new ArrayList<>();
        expected.add(new RPNCalc.CalcItem(1, "5"));
        expected.add(new RPNCalc.CalcItem(3, "3.555"));
        expected.add(new RPNCalc.CalcItem(9, "+"));
        expected.add(new RPNCalc.CalcItem(11, "6"));
        expected.add(new RPNCalc.CalcItem(13, "*"));
        expected.add(new RPNCalc.CalcItem(15, "undo"));

        ArrayList<RPNCalc.CalcItem> actual = new ArrayList<>();
        RPNCalc.splitStr(str, actual);

        assertTrue(checkCalcItems(expected, actual));
    }

    @Test
    public void testIsNum() {
        String str = "3.1415926";
        assertTrue(RPNCalc.isNum(str));

        str = "000123.000";
        assertTrue(RPNCalc.isNum(str));

        str = "-123.00";
        assertTrue(RPNCalc.isNum(str));

        str = "123.";
        assertFalse(RPNCalc.isNum(str));

        str = ".123";
        assertFalse(RPNCalc.isNum(str));

        str = "sqrt124";
        assertFalse(RPNCalc.isNum(str));

        str = "123test";
        assertFalse(RPNCalc.isNum(str));
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

    @Test
    public void testHandleOperatorsAdd() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "+", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        s.add(9.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "+", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsSub() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();
        s.add(16.0);
        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "-", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(9.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "-", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsMul() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "*", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        s.add(9.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "*", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsDev() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "/", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        s.add(9.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "/", s);
        assertTrue(item2.handleOperators(snapShot));

        s.clear();
        s.add(16.0);
        s.add(0.0);
        RPNCalc.CalcItem item3 = new RPNCalc.CalcItem(1, "/", s);
        assertFalse(item3.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsSqrt() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "sqrt", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "sqrt", s);
        assertTrue(item2.handleOperators(snapShot));

        s.clear();
        s.add(0.0);
        RPNCalc.CalcItem item3 = new RPNCalc.CalcItem(1, "sqrt", s);
        assertFalse(item3.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsClear() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "clear", s);
        assertTrue(item1.handleOperators(snapShot));

        s.add(16.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "clear", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsUndo() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "undo", s);
        assertFalse(item1.handleOperators(snapShot));

        Stack<Double> s1 = new Stack<>();
        s1.add(16.0);
        snapShot.add(s1);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "undo", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsOther() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        RPNCalc.CalcItem item1 = new RPNCalc.CalcItem(1, "test", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        RPNCalc.CalcItem item2 = new RPNCalc.CalcItem(1, "union", s);
        assertFalse(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleCalcItem() {
        RPNCalc.CalcItem item = new RPNCalc.CalcItem(1, "123");
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();
        assertTrue(item.handleCalcItem(snapShot));
    }
}
