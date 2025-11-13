package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaVendorRepository extends JpaRepository<Vendor, UUID> {

	@Query(
			"select new com.fastline.vendorservice.infrastructure.external.dto.delivery.VendorHubIdMappingObj(v.id, v.hubId) from Vendor v where v.id in :vendorIds")
	List<VendorHubIdMappingObj> findHubIdInVendor(@Param("vendorIds") List<UUID> vendorIds);

	@Query(
			"select v from Vendor v join Order o on o.vendorConsumerId = :vendorConsumerId where v.userId = :userId")
	List<Vendor> findVendorByUserIdAndConsumerId(
			@Param("userId") Long userId, @Param("vendorConsumerId") UUID vendorConsumerId);

	List<Vendor> findAllByHubId(UUID hubId);
}
