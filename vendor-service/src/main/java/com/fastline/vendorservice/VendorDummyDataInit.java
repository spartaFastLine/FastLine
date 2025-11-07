package com.fastline.vendorservice;

import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendorDummyDataInit implements ApplicationRunner {

	private final VendorRepository vendorRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		vendorRepository.insert(
				Vendor.create(
						"testVendor",
						VendorType.PRODUCER,
						VendorAddress.create("경기도", "분당구", "정자동", "12385"),
						UUID.randomUUID()));
	}
}
