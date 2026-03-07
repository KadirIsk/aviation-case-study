package com.aviation.routing.flight.path.engine.infrastructure.persistence.specification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aviation.routing.flight.path.engine.application.dto.LocationFilterRequest;
import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.LocationEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

class LocationSpecificationsTest {

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_whenNameProvided_addsLikePredicate() {
        // Given
        LocationFilterRequest filter = LocationFilterRequest.builder().name("Taksim").build();
        Specification<LocationEntity> spec = LocationSpecifications.withFilters(filter);

        Root<LocationEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path<String> path = mock(Path.class);
        Expression<String> lowerExpression = mock(Expression.class);
        Predicate likePredicate = mock(Predicate.class);

        when(root.<String>get(LocationEntity.Fields.name)).thenReturn(path);
        when(cb.lower(path)).thenReturn(lowerExpression);
        when(cb.like(eq(lowerExpression), eq("%taksim%"))).thenReturn(likePredicate);
        when(cb.and(any())).thenReturn(mock(Predicate.class));

        // When
        spec.toPredicate(root, query, cb);

        // Then
        verify(cb).like(eq(lowerExpression), eq("%taksim%"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void withFilters_whenAllFiltersProvided_addsAllLikePredicates() {
        // Given
        LocationFilterRequest filter = LocationFilterRequest.builder()
            .name("Taksim")
            .country("Turkey")
            .city("Istanbul")
            .locationCode("IST")
            .build();
        
        Specification<LocationEntity> spec = LocationSpecifications.withFilters(filter);

        Root<LocationEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        Path<String> namePath = mock(Path.class);
        Path<String> countryPath = mock(Path.class);
        Path<String> cityPath = mock(Path.class);
        Path<String> codePath = mock(Path.class);
        
        when(root.<String>get(LocationEntity.Fields.name)).thenReturn(namePath);
        when(root.<String>get(LocationEntity.Fields.country)).thenReturn(countryPath);
        when(root.<String>get(LocationEntity.Fields.city)).thenReturn(cityPath);
        when(root.<String>get(LocationEntity.Fields.locationCode)).thenReturn(codePath);
        
        when(cb.lower(any(Expression.class))).thenReturn(mock(Expression.class));
        when(cb.like(any(Expression.class), any(String.class))).thenReturn(mock(Predicate.class));
        when(cb.and(any())).thenReturn(mock(Predicate.class));

        // When
        spec.toPredicate(root, query, cb);

        // Then
        verify(cb).like(any(), eq("%taksim%"));
        verify(cb).like(any(), eq("%turkey%"));
        verify(cb).like(any(), eq("%istanbul%"));
        verify(cb).like(any(), eq("%ist%"));
        verify(cb, times(4)).like(any(), any(String.class));
    }
}
