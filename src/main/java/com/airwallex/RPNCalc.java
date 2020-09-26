package com.airwallex;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.text.DecimalFormat;

public class RPNCalc {
    private static final ArrayList<Stack<Double>> snapShot = new ArrayList<>();
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##########");
    private static final String OPERATOR_UNDO = "undo";
    private static final String OPERATOR_CLEAR = "clear";
    private static final String OPERATOR_SQRT = "sqrt";
    private static final String OPERATOR_ADD = "+";
    private static final String OPERATOR_SUB = "-";
    private static final String OPERATOR_MUL = "*";
    private static final String OPERATOR_DEV = "/";
    private static final String STACK_PRINT_START = "stack:";

    enum ErrCode {
        INSUFFICIENT_PARAMS("insufficient parameters"),
        INVALID_OPERATOR("invalid operator"),
        ILLEGAL_DIVISOR("illegal divisor"),
        ILLEGAL_ROOT("illegal root"),
        NO_MORE_HISTORY("invalid operation, no more history");

        private final String errMsg;

        ErrCode(String errMsg) {
            this.errMsg = errMsg;
        }

        public String GetErrMsg() {
            return errMsg;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            ArrayList<CalcItem> items = new ArrayList<>();
            splitStr(scanner.nextLine(), items);

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

    static void splitStr(String raw, ArrayList<CalcItem> items) {
        String str = raw.trim().toLowerCase();
        if (str.isEmpty()) {
            return;
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
    }

    static Boolean isNum(String str) {
        return str.matches("-?[0-9]+(.[0-9])?[0-9]*");
    }

    static String toFormatString(Stack<Double> stack) {
        StringBuilder sb = new StringBuilder(stack.size() << 3);
        sb.append(STACK_PRINT_START);
        for (Double s : stack) {
            sb.append(' ').append(decimalFormat.format(s));
        }
        return sb.toString();
    }

    static class CalcItem {
        private final int index;
        private final String value;
        private final Stack<Double> dataStack;

        CalcItem(int i, String v) {
            index = i;
            value = v;
            dataStack = new Stack<>();
        }

        CalcItem(int i, String v, Stack<Double> s) {
            index = i;
            value = v;
            dataStack = s;
        }

        public int getIndex() {
            return index;
        }

        public String getValue() {
            return value;
        }

        public static double calc(double data, String op) {
            double res = 0;
            if (OPERATOR_SQRT.equals(op)) {
                res = Math.sqrt(data);
            }
            return res;
        }

        public static double calc(double x, double y, String op) {
            double res = 0;
            switch (op) {
                case OPERATOR_ADD: {
                    res = y + x;
                    break;
                }
                case OPERATOR_SUB: {
                    res = y - x;
                    break;
                }
                case OPERATOR_MUL: {
                    res = y * x;
                    break;
                }
                case OPERATOR_DEV: {
                    res = y / x;
                    break;
                }
                default:
                    break;
            }
            return res;
        }

        public Boolean handleOperators(ArrayList<Stack<Double>> snapShot) {
            switch (value) {
                case OPERATOR_ADD:
                case OPERATOR_SUB:
                case OPERATOR_MUL:
                case OPERATOR_DEV: {
                    if (dataStack.size() < 2) {
                        printErrMsg(ErrCode.INSUFFICIENT_PARAMS);
                        return false;
                    }
                    if (OPERATOR_DEV.equals(value) && (dataStack.peek() == 0)) {
                        printErrMsg(ErrCode.ILLEGAL_DIVISOR);
                        return false;
                    }
                    dataStack.push(calc(dataStack.pop(), dataStack.pop(), value));
                    snapShot.add(dataStack);
                    break;
                }
                case OPERATOR_SQRT: {
                    if (dataStack.isEmpty()) {
                        printErrMsg(ErrCode.INSUFFICIENT_PARAMS);
                        return false;
                    }
                    if (dataStack.peek() == 0) {
                        printErrMsg(ErrCode.ILLEGAL_ROOT);
                        return false;
                    }
                    dataStack.push(calc(dataStack.pop(), value));
                    snapShot.add(dataStack);
                    break;
                }
                case OPERATOR_CLEAR: {
                    dataStack.clear();
                    snapShot.add(dataStack);
                    break;
                }
                case OPERATOR_UNDO: {
                    if (snapShot.isEmpty()) {
                        printErrMsg(ErrCode.NO_MORE_HISTORY);
                        return false;
                    }
                    snapShot.remove(snapShot.size() - 1);
                    if (!snapShot.isEmpty()) {
                        dataStack.addAll(snapShot.get(snapShot.size() - 1));
                    }
                    break;
                }
                default: {
                    printErrMsg(ErrCode.INVALID_OPERATOR);
                    return false;
                }
            }
            return true;
        }

        public Boolean handleCalcItem(ArrayList<Stack<Double>> snapShot) {
            if (!snapShot.isEmpty()) {
                dataStack.addAll(snapShot.get(snapShot.size() - 1));
            }

            if (isNum(value)) {
                dataStack.push(Double.parseDouble(value));
                snapShot.add(dataStack);
                return true;
            } else {
                return handleOperators(snapShot);
            }
        }

        public void printErrMsg(ErrCode errCode) {
            System.out.println("operator <" + value + "> (position: <" + index + ">): " + errCode.GetErrMsg());
        }
    }
}
