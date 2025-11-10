package com.fastline.vendorservice.domain.entity;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.jpa.TimeBaseEntity;
import com.fastline.vendorservice.domain.vo.Money;
import com.fastline.vendorservice.domain.vo.Stock;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "p_product")
@Getter
@SQLDelete(sql = "UPDATE p_product SET deleted_at = CURRENT_TIMESTAMP WHERE product_id = ?")
@Filter(name = "softDeleteFilter")
public class Product extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private UUID id;

	@Column(nullable = false, length = 30)
	private String name;

	@Embedded
	@Column(nullable = false)
	private Stock stock;

	@Embedded
	@Column(nullable = false)
	private Money price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	protected Product() {}

	public static Product create(String name, Stock stock, Double price, Vendor vendor) {
		if (name == null || stock == null || price == null)
			throw new CustomException(ErrorCode.VALIDATION_ERROR);

		Product product = new Product();
		product.name = name;
		product.stock = stock;
		product.price = Money.of(price);
		product.vendor = vendor;
		return product;
	}

	public Product update(String newName, Integer newStock, Double newPrice) {

		this.name = newName == null ? name : newName;
		this.stock = newStock == null ? stock : stock.adjust(Stock.of(newStock));
		this.price = newPrice == null ? price : Money.of(newPrice);

		return this;
	}
}
