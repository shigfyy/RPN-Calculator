package com.airwallex;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.text.DecimalFormat;

public class RPNCalc {
    private ArrayList<Stack<Double>> snapShot;
    public static final DecimalFormat DECIMAL_FORMAT_TEN = new DecimalFormat("#.##########");
    public static final String STACK_PRINT_START = "stack:";

    public RPNCalc() {
        snapShot = new ArrayList<>();
    }

    public ArrayList<Stack<Double>> getSnapShot() {
        return snapShot;
    }
    static ArrayList<CalcItem> splitStr(String raw) {
        ArrayList<CalcItem> items = new ArrayList<>();
        String str = raw.trim().toLowerCase();
        if (str.isEmpty()) {
            return items;
        }
        int off = 0;
        int next;
        while ((next = str.indexOf(' ', off)) != -1) {
            CalcItem item = new CalcItem(off + 1, str.substring(off, next));
            items.add(item);
            while (str.charAt(next) == ' ') {
                next++;
            }
            off = next;
        }
        CalcItem item = new CalcItem(off + 1, str.substring(off));
        items.add(item);
        return items;
    }

    static String toFormatString(Stack<Double> stack) {
        StringBuilder sb = new StringBuilder(stack.size() << 3);
        sb.append(STACK_PRINT_START);
        for (Double s : stack) {
            sb.append(' ').append(DECIMAL_FORMAT_TEN.format(s));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RPNCalc rpnCalc = new RPNCalc();
        ArrayList<Stack<Double>> snapShot = rpnCalc.getSnapShot();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            ArrayList<CalcItem> items = splitStr(scanner.nextLine());

            for (CalcItem item : items) {
                if (!item.handleCalcItem(snapShot)) {
                    break;
                }
            }
            if (!snapShot.isEmpty()) {
                System.out.println(toFormatString(snapShot.get(snapShot.size() - 1)));
            } else {
                System.out.println(STACK_PRINT_START);
            }
        }
    }
}
