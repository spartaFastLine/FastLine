package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendorRepositoryAdapter implements VendorRepository {

	private final JpaVendorRepository jpaVendorRepository;

	@Override
	public Vendor insert(Vendor vendor) {

		Vendor insertedVendor = jpaVendorRepository.save(vendor);

		try {
			jpaVendorRepository.flush();
		} catch (DataIntegrityViolationException de) {
			throw new CustomException(ErrorCode.ADDRESS_DUPLICATED);
		}

		return insertedVendor;
	}

	@Override
	public Vendor findByVendorId(UUID vendorId) {
		return jpaVendorRepository
				.findById(vendorId)
				.orElseThrow(() -> new CustomException(ErrorCode.VENDOR_NOT_FOUND));
	}

	@Override
	public UUID deleteByVendorId(UUID vendorId) {

		jpaVendorRepository.deleteById(vendorId);
		return vendorId;
	}
}
