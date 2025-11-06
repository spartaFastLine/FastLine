package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.VendorService;
import com.fastline.vendorservice.application.command.CreateVendorCommand;
import com.fastline.vendorservice.application.command.UpdateVendorCommand;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.presentation.request.VendorCreateRequest;
import com.fastline.vendorservice.presentation.request.VendorUpdateRequest;
import com.fastline.vendorservice.presentation.response.vendor.VendorCreateResponse;
import com.fastline.vendorservice.presentation.response.vendor.VendorFindResponse;
import com.fastline.vendorservice.presentation.response.vendor.VendorUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService service;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorCreateResponse>> insertVendor(@RequestBody @Valid VendorCreateRequest createRequest) {

        CreateVendorCommand createCommand = new CreateVendorCommand(
                createRequest.name(),
                createRequest.type(),
                createRequest.city(),
                createRequest.district(),
                createRequest.roadName(),
                createRequest.zipCode(),
                createRequest.hubId()
        );

        Vendor vendor = service.insert(createCommand);
        VendorCreateResponse response = new VendorCreateResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getType(),
                vendor.getAddress(),
                vendor.getHubId()
        );
        return ResponseUtil.successResponse(SuccessCode.VENDOR_SAVE_SUCCESS , response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<VendorFindResponse>> findVendor(@RequestParam UUID vendorId) {

        Vendor vendor = service.findByVendorId(vendorId);
        VendorFindResponse response = new VendorFindResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getType(),
                vendor.getAddress(),
                vendor.getHubId()
        );

        return ResponseUtil.successResponse(SuccessCode.VENDOR_FIND_SUCCESS, response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<VendorUpdateResponse>> updateVendor(@RequestBody @Valid VendorUpdateRequest updateRequest,
                                                                          @RequestParam UUID vendorId) {

        if(updateRequest == null)
            throw new CustomException(ErrorCode.VALIDATION_ERROR);

        UpdateVendorCommand updateCommand = new UpdateVendorCommand(
                updateRequest.name(),
                updateRequest.type(),
                updateRequest.city(),
                updateRequest.district(),
                updateRequest.roadName(),
                updateRequest.zipCode(),
                updateRequest.hubId()
        );

        Vendor vendor = service.updateVendor(vendorId, updateCommand);
        VendorUpdateResponse response = new VendorUpdateResponse(
                vendor.getName(),
                vendor.getType(),
                vendor.getAddress(),
                vendor.getHubId()
        );

        return ResponseUtil.successResponse(SuccessCode.VENDOR_UPDATE_SUCCESS, response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<UUID>> deleteVendor(@RequestParam UUID vendorId) {

        UUID deletedId = service.deleteVendor(vendorId);
        return ResponseUtil.successResponse(SuccessCode.VENDOR_DELETE_SUCCESS, deletedId);
    }
}
