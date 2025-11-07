package com.fastline.vendorservice.domain.entity;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.jpa.TimeBaseEntity;
import com.fastline.vendorservice.domain.vo.Money;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Filter;

@Entity
@Table(
		name = "p_product",
		uniqueConstraints =
				@UniqueConstraint(
						name = "oneVendorOneName",
						columnNames = {"name", "vendor_id"}))
@Getter
@Filter(name = "softDeleteFilter")
public class Product extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private UUID id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false)
	private Integer stock;

	@Embedded
	@Column(nullable = false)
	private Money price;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	protected Product() {}

	public static Product create(String name, Integer stock, Double price, Vendor vendor) {
		if (name == null || stock == null || price == null)
			throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Product product = new Product();
		product.name = name;
		product.stock = stock;
		product.price = Money.of(price);
		product.vendor = vendor;
		return product;
	}
}
