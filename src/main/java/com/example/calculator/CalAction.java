package com.example.calculator;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 所有的操作情况
 */
@Data
public class CalAction implements Serializable {
    /**
     * 操作符
     */
    private String operator;
    /**
     * 上一次参与计算的数值
     */
    private BigDecimal calValue;



}
