package com.courses.services.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class CommonSpecification<T> implements Specification<T> {

    @Serial
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
