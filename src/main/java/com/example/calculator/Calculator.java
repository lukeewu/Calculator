package com.example.calculator;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Calculator implements Serializable {
    /**
     * 上一次运算符
     */
    private String latelyOperator;
    /**
     * 上一次的值
     */
    private BigDecimal latelyValue;
    /**
     * 上一次参与计算的数值
     */
    private BigDecimal latelyCalValue;

    /**
     * 计算之后的结果
     */
    private BigDecimal nowValue;

    /**
     * 最近的操作
     */
    private List<CalAction> calActions ;

}
