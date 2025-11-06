package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VendorRepositoryAdapter implements VendorRepository{

    private final JpaVendorRepository jpaVendorRepository;

    @Override
    public Vendor insert(Vendor vendor) {

        Vendor insertedVendor = jpaVendorRepository.save(vendor);

        try{
            jpaVendorRepository.flush();
        } catch (DataIntegrityViolationException de){
            throw new CustomException(ErrorCode.ADDRESS_DUPLICATED);
        }

        return insertedVendor;
    }
}
