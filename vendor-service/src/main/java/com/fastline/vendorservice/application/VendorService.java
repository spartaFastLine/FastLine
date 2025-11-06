package com.fastline.vendorservice.application;

import com.fastline.vendorservice.application.command.CreateVendorCommand;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import com.fastline.vendorservice.presentation.response.VendorResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository repository;
//    private final HubClient hubClient; -- 허브 서비스로 API 호출을 보낼 책임을 갖음

    /**
     * TODO: 허브 서비스로의 API 호출 성공, 실패시 흐름처리 작성 필요
     * TODO: 성공시, Vendor.create의 UUID.randomUUID() 부분에 허브ID. 실패시 적절한 예외처리
     */
    @Transactional
    public VendorResponse insert(CreateVendorCommand createCommand) {

        VendorType vendorType = VendorType.fromString(createCommand.type());

        VendorAddress vendorAddress = VendorAddress.create(
                createCommand.city(),
                createCommand.district(),
                createCommand.roadName(),
                createCommand.zipCode());

//        hubClient.findHub(); -- 존재하는 허브인지 허브 서비스로 API 호출. 없거나 에러시 적절한 처리 필요

        Vendor vendor = Vendor.create(
                createCommand.name(), vendorType, vendorAddress, UUID.randomUUID()
        );

        Vendor insertedVendor = repository.insert(vendor);
        return VendorResponse.fromVendor(insertedVendor);
    }

    public VendorResponse findByVendorId(UUID vendorId) {

        Vendor findVendor = repository.findByVendorId(vendorId);
        return VendorResponse.fromVendor(findVendor);
    }
}
