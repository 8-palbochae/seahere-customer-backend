package com.seahere.backend.inventory.repository;

import com.seahere.backend.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM InventoryEntity i " +
            "WHERE i.category = :category AND i.product.productName = :productName AND i.company.id = :companyId AND i.naturalStatus = :naturalStatus AND i.country = :country")
    boolean existsByCategoryAndProductNameAndCompanyIdAndNaturalStatusAndCountry(
            @Param("category") String category,
            @Param("productName") String productName,
            @Param("companyId") Long companyId,
            @Param("naturalStatus") String naturalStatus,
            @Param("country") String country);

    @Query("SELECT i FROM InventoryEntity i LEFT JOIN FETCH i.inventoryDetail WHERE i.category = :category AND i.product.productName = :productName AND i.company.id = :companyId AND i.naturalStatus = :naturalStatus AND i.country = :country")
    Optional<InventoryEntity> findByCategoryAndProductNameAndCompanyIdAndNaturalStatusAndCountry(
            @Param("category") String category,
            @Param("productName") String productName,
            @Param("companyId") Long companyId,
            @Param("naturalStatus") String naturalStatus,
            @Param("country") String country);

    @Query("SELECT i FROM InventoryEntity i LEFT JOIN FETCH i.inventoryDetail LEFT JOIN FETCH i.discount WHERE i.inventoryId = :inventoryId")
    InventoryEntity getIdWithDetails(@Param("inventoryId")Long inventoryId);
}
