package com.courses.utils;

import com.courses.services.specs.FilterCriteria;
import com.courses.services.specs.SortCriteria;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static List<FilterCriteria> convertJsonStringToListFilterCriteria(String jsonString){
        List<FilterCriteria> searchCriteriaList = null;
        try {
            searchCriteriaList = mapper.readValue(jsonString, new TypeReference<List<FilterCriteria>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchCriteriaList;
    }

    public static List<SortCriteria> convertJsonStringToListSortCriteria(String jsonString){
        List<SortCriteria> searchCriteriaList = null;
        try {
            searchCriteriaList = mapper.readValue(jsonString, new TypeReference<List<SortCriteria>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchCriteriaList;
    }
}
