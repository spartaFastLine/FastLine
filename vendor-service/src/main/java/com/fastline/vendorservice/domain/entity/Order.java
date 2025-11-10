package com.fastline.vendorservice.domain.entity;

import com.fastline.vendorservice.application.command.CreateOrderCommand;
import com.fastline.vendorservice.domain.vo.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(name = "p_order")
@Getter
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private UUID id;

	@Column(nullable = false)
	private UUID vendorProducerId;

	@Column(nullable = false)
	private UUID vendorConsumerId;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	private LocalDate arrivalTime;

	@Column(length = 100)
	private String request;

	@Column(nullable = false)
	private UUID deliveryId;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public static Order create(CreateOrderCommand createCommand) {

		Order order = new Order();
		order.vendorProducerId = createCommand.vendorProducerId();
		order.vendorConsumerId = createCommand.vendorConsumerId();
		order.status = OrderStatus.READY;
		order.request = createCommand.request();

		return order;
	}

	public void mappingOrderProduct(OrderProduct orderProduct) {
		this.orderProducts.add(orderProduct);
	}

	public void mappingDeliveryId(UUID deliveryId) {
		this.deliveryId = deliveryId;
	}
}
