package com.courses.services.specs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {
    private String key;
    private Object value1;
    private Object value2;
    private FilterOperator operator;
}
