package com.challenge.faire.statistics;

import lombok.Builder;
import lombok.Value;

/**
 * Created by weberling on 25/03/19.
 */
@Builder
@Value
public class StatisticsDto {

    private EntityStatistics bestSellingProductOption;

    private EntityStatistics largestOrderByDollarAmmount;

    private EntityStatistics stateMostOrder;

    private EntityStatistics bestSellingSku;

    private EntityStatistics stateMostAmountOrder;

}
