package com.fastline.vendorservice.domain.entity;

import com.fastline.common.jpa.TimeBaseEntity;
import com.fastline.vendorservice.domain.vo.OrderStatus;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_order")
@Getter
@SQLDelete(sql = "UPDATE p_order SET deleted_at = CURRENT_TIMESTAMP WHERE order_id = ?")
@Filter(name = "softDeleteFilter")
public class Order extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_id")
	private UUID id;

	@Column(nullable = false)
	private UUID vendorProducerId;

	@Column(nullable = false)
	private UUID vendorConsumerId;

	@Column(nullable = false)
	private String consumerName;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime arrivalTime;

	@Column(length = 100)
	private String request;

	@Column(nullable = false)
	private UUID deliveryId;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public static Order create(OrderCreateRequest createRequest) {

		Order order = new Order();
		order.vendorProducerId = createRequest.vendorProducerId();
		order.vendorConsumerId = createRequest.vendorConsumerId();
		order.consumerName = createRequest.consumerName();
		order.status = OrderStatus.READY;
		order.request = createRequest.request();

		return order;
	}

	public Order updateStatus(OrderStatus orderStatus) {

		if (orderStatus == OrderStatus.COMPLETED) this.arrivalTime = LocalDateTime.now();
		this.status = orderStatus;

		return this;
	}

	public void mappingOrderProduct(OrderProduct orderProduct) {
		this.orderProducts.add(orderProduct);
	}

	public void mappingDeliveryId(UUID deliveryId) {
		this.deliveryId = deliveryId;
	}
}
