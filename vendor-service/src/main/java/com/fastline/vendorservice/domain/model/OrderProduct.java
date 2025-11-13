package com.fastline.vendorservice.domain.model;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.domain.vo.Money;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(name = "p_order_product")
@Getter
public class OrderProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Embedded
	@Column(nullable = false)
	private Money price;

	@Column(nullable = false)
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	public static OrderProduct create(Order order, Product product, Integer quantity) {
		if (order == null || product == null || quantity == null || quantity < 0)
			throw new CustomException(ErrorCode.VALIDATION_ERROR);

		OrderProduct orderProduct = new OrderProduct();
		orderProduct.price = product.getPrice();
		orderProduct.quantity = quantity;
		orderProduct.order = order;
		orderProduct.product = product;

		return orderProduct;
	}
}
