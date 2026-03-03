package com.aviation.routing.flight.path.engine.infrastructure.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.LocationRequest;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class LocationSpecifications {

    public static Specification<LocationEntity> withFilters(LocationRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(filter.name())) {
                predicates.add(cb.equal(root.get(LocationEntity.Fields.name), filter.name()));
            }
            if (StringUtils.isNotBlank(filter.country())) {
                predicates.add(cb.equal(root.get(LocationEntity.Fields.country), filter.country()));
            }
            if (StringUtils.isNotBlank(filter.city())) {
                predicates.add(cb.equal(root.get(LocationEntity.Fields.city), filter.city()));
            }
            if (StringUtils.isNotBlank(filter.locationCode())) {
                predicates.add(cb.like(root.get(LocationEntity.Fields.locationCode), filter.locationCode()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}