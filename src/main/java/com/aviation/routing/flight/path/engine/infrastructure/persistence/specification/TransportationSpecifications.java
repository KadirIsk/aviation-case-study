package com.aviation.routing.flight.path.engine.infrastructure.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationRequest;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class TransportationSpecifications {

    public static Specification<TransportationEntity> withFilters(TransportationRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.originLocationId() != null) {
                Path<Object> originLocation = root.get(TransportationEntity.Fields.originLocationEntity);
                predicates.add(cb.equal(originLocation.get(LocationEntity.Fields.id), filter.originLocationId()));
            }
            if (filter.destinationLocationId() != null) {
                Path<Object> destinationLocation = root.get(TransportationEntity.Fields.destinationLocationEntity);
                predicates.add(cb.equal(destinationLocation.get(LocationEntity.Fields.id), filter.destinationLocationId()));
            }
            if (filter.transportationType() != null && !filter.transportationType().isBlank()) {
                predicates.add(cb.equal(root.get(TransportationEntity.Fields.transportationType), filter.transportationType()));
            }
            if (filter.operatingDays() != null) {   // todo: bu kismi duzeltecegiz!!!
                predicates.add(cb.like(root.get(TransportationEntity.Fields.operatingDays), "%" + filter.operatingDays() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}