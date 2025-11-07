package com.fastline.vendorservice.domain.entity;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.jpa.TimeBaseEntity;
import com.fastline.vendorservice.application.command.UpdateVendorCommand;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

@Entity
@Table(name = "p_vendor")
@Getter
@FilterDef(
		name = "softDeleteFilter",
		defaultCondition = "deleted_at IS NULL",
		autoEnabled = true,
		applyToLoadByKey = true)
@Filter(name = "softDeleteFilter")
public class Vendor extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vendor_id")
	private UUID id;

	@Column(nullable = false, length = 30)
	private String name;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private VendorType type;

	@Embedded
	@Column(nullable = false, unique = true)
	private VendorAddress address;

	@Column(nullable = false)
	private UUID hubId;

	protected Vendor() {}

	public static Vendor create(String name, VendorType type, VendorAddress address, UUID hubId) {
		if (name == null || type == null || address == null || hubId == null)
			throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Vendor vendor = new Vendor();
		vendor.name = name;
		vendor.type = type;
		vendor.address = address;
		vendor.hubId = hubId;
		return vendor;
	}

	public Vendor update(UpdateVendorCommand updateCommand) {

		this.name = updateCommand.name() == null ? name : updateCommand.name();
		this.type = updateCommand.type() == null ? type : VendorType.fromString(updateCommand.type());
		this.address = address.update(updateCommand);
		this.hubId = updateCommand.hubId() == null ? hubId : updateCommand.hubId();

		return this;
	}
}
