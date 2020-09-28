package com.airwallex;

import java.util.ArrayList;
import java.util.Stack;

public class CalcItem {
    private static final String OPERATOR_UNDO = "undo";
    private static final String OPERATOR_CLEAR = "clear";
    private static final String OPERATOR_SQRT = "sqrt";
    private static final String OPERATOR_ADD = "+";
    private static final String OPERATOR_SUB = "-";
    private static final String OPERATOR_MUL = "*";
    private static final String OPERATOR_DEV = "/";

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

    static Boolean isNum(String str) {
        return str.matches("[+-]?[0-9]+.?[0-9]*") || str.matches("[+-]?[0-9]*.?[0-9]+");
    }

    private double calc(double data) {
        double res = 0;
        if (OPERATOR_SQRT.equals(value)) {
            res = Math.sqrt(data);
        }
        return res;
    }

    private double calc(double x, double y) {
        double res = 0;
        switch (value) {
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
                dataStack.push(calc(dataStack.pop(), dataStack.pop()));
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
                dataStack.push(calc(dataStack.pop()));
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
