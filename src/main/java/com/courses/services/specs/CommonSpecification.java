package com.courses.services.specs;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CommonSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = -1556420764505549778L;

    private final List<FilterCriteria> list;

    public CommonSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(FilterCriteria criteria) {
        list.add(criteria);
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates;
        predicates = SpecificationHelper.buildPredicates(root, builder, list);
        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
