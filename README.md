# RPN-Calculator


Build By maven


计算器设计

1. 输入的解析
2. 对于解析中的每个字符构建CalcItem对象
3. 每个CalcItem对象有相应的方法接口来完成数据栈上的操作
4. 对于undo，通过保留一个全局数据结构来保存任何栈的变化
5. clear操作清空栈
