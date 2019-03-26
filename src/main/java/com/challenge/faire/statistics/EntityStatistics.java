package com.challenge.faire.statistics;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

/**
 * Created by weberling on 25/03/19.
 */
@Data
public class EntityStatistics {
    private String key;
    private Integer value = 0;
}
