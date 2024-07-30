package com.courses.services.specs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortCriteria {
    private String key;
    private String criteria;
}
