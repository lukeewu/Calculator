package com.example.calculator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jisuanqi {
    //实例化一个计算对象，对象记录着，上一次的所有运算信息，包括运算符号，参与计算的值和计算之后的结果

    private static Calculator calculator = new Calculator();
    /**
     *
     * 为了简化操作，计算器的第一步，直接从：第一次参数运算之后开始。如 6*6=36，
     * @param args
     */
    public static void main(String[]args)
    {
        //初始化所有的操作记录
        List<CalAction> calActions = new ArrayList<>();
        calculator.setCalActions(calActions);
        //1：第一次输入6*6,并计算
        parseFirstInput("6*6");
        System.out.println(JSONUtil.toJsonStr(calculator));
        //2：随便进行一个 + - * / 操作
        calcTwoNum(calculator.getNowValue(),"-",new BigDecimal("2"));
        calcTwoNum(calculator.getNowValue(),"+",new BigDecimal("50"));
        calcTwoNum(calculator.getNowValue(),"/",new BigDecimal("20"));
        calcTwoNum(calculator.getNowValue(),"*",new BigDecimal("20"));
        //3：进行几次redo
        redo();
        redo();
        redo();
        //4：进行几次undo
        undo();
        undo();
        undo();
    }

    /**
     * redo  因为calculator记录着上一次的运算符，和计算的值，已经计算后的值，所以这里很方便就可以直接拿到需要的参数去计算
     */
    public static void redo(){
        calcTwoNum(calculator.getNowValue(),calculator.getLatelyOperator(),calculator.getLatelyCalValue());
    }

    /**
     * undo 就是反向操作
     */
    public static void undo(){
        //获取最后一次的操作符号
        List<CalAction> calActions = calculator.getCalActions();
        CalAction calAction = calActions.get(calActions.size() -1);
        CalAction before = calActions.get(calActions.size() -2);
        BigDecimal newValue = null;
        //反向操作
        if(calAction.getOperator().equals("*")){
            newValue =  calculator.getNowValue().divide(calAction.getCalValue());
        }else if(calAction.getOperator().equals("-")){
            newValue =  calculator.getNowValue().add(calAction.getCalValue());
        }else if(calAction.getOperator().equals("+")){
            newValue =  calculator.getNowValue().subtract(calAction.getCalValue());
        }else if(calAction.getOperator().equals("/")){
            newValue =  calculator.getNowValue().multiply(calAction.getCalValue());
        }
        calculator.setNowValue(newValue);
        //导出第二次运算符
        calculator.setLatelyOperator(before.getOperator());
        calculator.setLatelyCalValue(before.getCalValue());
        //删除掉undo的这个操作记录
        calActions.remove(calAction);
        calculator.setCalActions(calActions);
        //打印 undo的时候不关注latelyValue的值
        System.out.println(JSONUtil.toJsonStr(calculator));

    }
    /**
     * 解析第一次要参数计算的字符串 如  6*6
     * @param input
     */
    public static void parseFirstInput(String input ){
        Map<String,String> caozuofuMap = new HashMap<>();
        caozuofuMap.put("+","+");
        caozuofuMap.put("-","-");
        caozuofuMap.put("*","*");
        caozuofuMap.put("/","/");
        for(String key : caozuofuMap.keySet()){
            if(input.contains(key)){
                List<String> list =  StrUtil.split(input,key);
                calcTwoNum(new BigDecimal(list.get(0)),key,new BigDecimal(list.get(1)));
            }
        }

    }

    /**
     * 计算两个值
     * @param preTotal
     * @param curOperator
     * @param newNum
     * @return
     */
    private static BigDecimal calcTwoNum(BigDecimal preTotal, String curOperator, BigDecimal newNum) {
        BigDecimal ret = BigDecimal.ZERO;
        curOperator = curOperator == null ? "+" : curOperator;
        switch (curOperator){
            case "+":
                ret = preTotal.add(newNum);
                break;
            case "-":
                ret = preTotal.subtract(newNum);
                break;
            case "*":
                ret = preTotal.multiply(newNum);
                break;
            case "/":
                ret = preTotal.divide(newNum);
                break;
        }
        //计算完成之后对对象的内容刷新
        calculator.setLatelyValue(preTotal);
        calculator.setLatelyCalValue(newNum);
        calculator.setLatelyOperator(curOperator);
        calculator.setNowValue(ret);
        //记录每一次的操作符号和参数运算数值
        CalAction calAction = new CalAction();
        calAction.setOperator(curOperator);
        calAction.setCalValue(newNum);
        List<CalAction> calculators = calculator.getCalActions();
        calculators.add(calAction);
        calculator.setCalActions(calculators);
        System.out.println(JSONUtil.toJsonStr(calculator));
        return ret;
    }
}
