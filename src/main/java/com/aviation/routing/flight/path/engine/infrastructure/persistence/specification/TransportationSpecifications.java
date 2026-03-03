package com.aviation.routing.flight.path.engine.infrastructure.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class TransportationSpecifications {

    public static Specification<TransportationEntity> withFilters(TransportationRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.originLocationId() != null) {
                predicates.add(cb.equal(root.get("originLocation").get("id"), filter.originLocationId()));
            }
            if (filter.destinationLocationId() != null) {
                predicates.add(cb.equal(root.get("destinationLocation").get("id"), filter.destinationLocationId()));
            }
            if (filter.transportationType() != null && !filter.transportationType().isBlank()) {
                predicates.add(cb.equal(root.get("transportationType"), filter.transportationType()));
            }
            if (filter.operatingDays() != null && !filter.operatingDays().isBlank()) {
                predicates.add(cb.like(root.get("operatingDays"), "%" + filter.operatingDays() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}