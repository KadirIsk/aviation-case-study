package com.aviation.routing.flight.path.engine.infrastructure.persistence.repository;

import java.util.List;

import com.aviation.routing.flight.path.engine.infrastructure.persistence.entity.TransportationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTransportationRepository extends JpaRepository<TransportationEntity, Long>,
    JpaSpecificationExecutor<TransportationEntity> {
    @Query(
        value = "SELECT * FROM transportations t WHERE (t.operating_days & :dayMask) > 0",
        countQuery = "SELECT count(*) FROM transportations t WHERE (t.operating_days & :dayMask) > 0",
        nativeQuery = true
    )
    Page<TransportationEntity> findByOperatingDay(@Param("dayMask") Integer dayMask, Pageable pageable);

    List<TransportationEntity> findByOriginLocationEntityId(Long originLocationId);

    @Query("SELECT t FROM TransportationEntity t ORDER BY t.originLocationEntityId")
    Slice<TransportationEntity> findAllByOrderByOriginLocationId(Pageable pageable);

    @Query(
        "SELECT COUNT(t) > 0 FROM TransportationEntity t " +
        "WHERE t.originLocationEntity.id = :originId " +
        "AND t.destinationLocationEntity.id = :destinationId " +
        "AND t.transportationType = :type"
    )
    boolean existsByRouteAndType(
        @Param("originId") Long originId,
        @Param("destinationId") Long destinationId,
        @Param("type") String type
    );

    @Modifying
    @Query("DELETE FROM TransportationEntity t WHERE t.originLocationEntity.id = :id")
    void deleteByOriginLocationEntityId(Long id);

    @Modifying
    @Query("DELETE FROM TransportationEntity t WHERE t.destinationLocationEntity.id = :id")
    void deleteByDestinationLocationEntityId(Long id);
}