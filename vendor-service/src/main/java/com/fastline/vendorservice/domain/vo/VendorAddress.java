package com.fastline.vendorservice.domain.vo;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.command.UpdateVendorCommand;
import com.fastline.vendorservice.domain.entity.Vendor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VendorAddress {

    @Column(length = 30, nullable = false)
    private String city;

    @Column(length = 30, nullable = false)
    private String district;

    @Column(length = 30, nullable = false)
    private String roadName;

    @Column(length = 10, nullable = false)
    private String zipCode;

    public static VendorAddress create(String city, String district, String roadName, String zipCode) {
        if(city == null || district == null || roadName == null || zipCode == null)
            throw new CustomException(ErrorCode.VALIDATION_ERROR);

        VendorAddress vendorAddress = new VendorAddress();
        vendorAddress.city = city;
        vendorAddress.district = district;
        vendorAddress.roadName = roadName;
        vendorAddress.zipCode = zipCode;
        return vendorAddress;
    }

    public VendorAddress update(UpdateVendorCommand command) {

        VendorAddress vendorAddress = new VendorAddress();
        vendorAddress.city = command.city() != null ? command.city() : this.city;
        vendorAddress.district = command.district() != null ? command.district() : this.district;
        vendorAddress.roadName = command.roadName() != null ? command.roadName() : this.roadName;
        vendorAddress.zipCode = command.zipCode() != null ? command.zipCode() : this.zipCode;

        return vendorAddress;
    }
}
