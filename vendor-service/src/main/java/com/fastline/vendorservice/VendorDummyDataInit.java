package com.fastline.vendorservice;

import com.fastline.vendorservice.application.command.CreateOrderCommand;
import com.fastline.vendorservice.application.command.CreateOrderProductCommand;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.OrderProduct;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.entity.Vendor;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import com.fastline.vendorservice.domain.repository.ProductRepository;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.vo.Stock;
import com.fastline.vendorservice.domain.vo.VendorAddress;
import com.fastline.vendorservice.domain.vo.VendorType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DummyData")
public class VendorDummyDataInit implements ApplicationRunner {

	private final VendorRepository vendorRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Vendor vendor =
				vendorRepository.insert(
						Vendor.create(
								"testVendor",
								VendorType.PRODUCER,
								VendorAddress.create("경기도", "분당구", "정자동", "12385"),
								UUID.randomUUID()));

		Product product =
				productRepository.insert(Product.create("testProduct", Stock.of(1000), 5000.0, vendor));
		Product product1 =
				productRepository.insert(Product.create("testProduct1", Stock.of(2000), 2000.0, vendor));

		Order order =
				Order.create(
						new CreateOrderCommand(
								vendor.getId(),
								vendor.getId(),
								"리퀘스트",
								List.of(
										new CreateOrderProductCommand(product.getId(), 1),
										new CreateOrderProductCommand(product.getId(), 1))));
		order.mappingDeliveryId(UUID.randomUUID());
		order.mappingOrderProduct(OrderProduct.create(order, product, 1));
		order.mappingOrderProduct(OrderProduct.create(order, product1, 1));
		Order insertedOrder = orderRepository.insert(order);

		log.info("vendorId = {}", vendor.getId());
		log.info("productId = {}", product.getId());
		log.info("product1Id = {}", product1.getId());
		log.info("orderId = {}", insertedOrder.getId());
	}
}
