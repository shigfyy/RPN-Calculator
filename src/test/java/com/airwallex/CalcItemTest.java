package com.airwallex;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Stack;

public class CalcItemTest {

    @Test
    public void testIsNum() {
        String str = "3.1415926";
        assertTrue(CalcItem.isNum(str));
        assertEquals(3.1415926, Double.parseDouble(str), 0);

        str = "000123.000";
        assertTrue(CalcItem.isNum(str));
        assertEquals(123.0, Double.parseDouble(str), 0);

        str = "-123.00";
        assertTrue(CalcItem.isNum(str));
        assertEquals(-123.0, Double.parseDouble(str), 0);

        str = "+123";
        assertTrue(CalcItem.isNum(str));
        assertEquals(123, Double.parseDouble(str), 0);

        str = "123.";
        assertTrue(CalcItem.isNum(str));
        assertEquals(123, Double.parseDouble(str), 0);

        str = "-.123";
        assertTrue(CalcItem.isNum(str));
        assertEquals(-0.123, Double.parseDouble(str), 0);

        str = "sqrt124";
        assertFalse(CalcItem.isNum(str));

        str = "123test";
        assertFalse(CalcItem.isNum(str));

        str = "123.456.789";
        assertFalse(CalcItem.isNum(str));

        str = ".";
        assertFalse(CalcItem.isNum(str));

        str = " ";
        assertFalse(CalcItem.isNum(str));
    }

    @Test
    public void testHandleOperatorsAdd() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "+", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        s.add(9.0);
        CalcItem item2 = new CalcItem(1, "+", s);
        assertTrue(item2.handleOperators(snapShot));
        assertEquals(25.0, s.peek(), 0);
    }

    @Test
    public void testHandleOperatorsSub() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();
        s.add(16.0);
        CalcItem item1 = new CalcItem(1, "-", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(9.0);
        CalcItem item2 = new CalcItem(1, "-", s);
        assertTrue(item2.handleOperators(snapShot));
        assertEquals(7.0, s.peek(), 0);
    }

    @Test
    public void testHandleOperatorsMul() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "*", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        s.add(9.0);
        CalcItem item2 = new CalcItem(1, "*", s);
        assertTrue(item2.handleOperators(snapShot));
        assertEquals(144.0, s.peek(), 0);
    }

    @Test
    public void testHandleOperatorsDev() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "/", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        s.add(9.0);
        CalcItem item2 = new CalcItem(1, "/", s);
        assertTrue(item2.handleOperators(snapShot));
        assertEquals(1.7777777777777777, s.peek(), 0);

        s.clear();
        s.add(16.0);
        s.add(0.0);
        CalcItem item3 = new CalcItem(1, "/", s);
        assertFalse(item3.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsSqrt() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "sqrt", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        CalcItem item2 = new CalcItem(1, "sqrt", s);
        assertTrue(item2.handleOperators(snapShot));
        assertEquals(4.0, s.peek(), 0);

        s.clear();
        s.add(0.0);
        CalcItem item3 = new CalcItem(1, "sqrt", s);
        assertFalse(item3.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsClear() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "clear", s);
        assertTrue(item1.handleOperators(snapShot));

        s.add(16.0);
        CalcItem item2 = new CalcItem(1, "clear", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsUndo() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "undo", s);
        assertFalse(item1.handleOperators(snapShot));

        Stack<Double> s1 = new Stack<>();
        s1.add(16.0);
        snapShot.add(s1);
        CalcItem item2 = new CalcItem(1, "undo", s);
        assertTrue(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleOperatorsOther() {
        Stack<Double> s = new Stack<>();
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();

        CalcItem item1 = new CalcItem(1, "test", s);
        assertFalse(item1.handleOperators(snapShot));

        s.add(16.0);
        CalcItem item2 = new CalcItem(1, "union", s);
        assertFalse(item2.handleOperators(snapShot));
    }

    @Test
    public void testHandleCalcItem() {
        CalcItem item = new CalcItem(1, "123");
        ArrayList<Stack<Double>> snapShot = new ArrayList<>();
        assertTrue(item.handleCalcItem(snapShot));
    }
}
