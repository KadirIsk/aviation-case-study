package com.aviation.routing.flight.path.engine.infrastructure.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import com.aviation.routing.flight.path.engine.application.dto.TransportationFilterRequest;
import com.aviation.routing.flight.path.engine.common.util.DayOfWeekBitmaskMapper;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class TransportationSpecifications {

    public static Specification<TransportationEntity> withFilters(TransportationFilterRequest filter) {
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

            if (StringUtils.isNotBlank(filter.transportationType())) {
                predicates.add(cb.equal(root.get(TransportationEntity.Fields.transportationType), filter.transportationType()));
            }

            if (CollectionUtils.isNotEmpty(filter.operatingDays())) {
                short requestedMask = DayOfWeekBitmaskMapper.toBitmask(filter.operatingDays());

                Expression<Integer> bitwiseAnd = cb.function(
                    "bitand",
                    Integer.class,
                    root.get(TransportationEntity.Fields.operatingDays),
                    cb.literal((int) requestedMask)
                );

                predicates.add(cb.greaterThan(bitwiseAnd, 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}