package com.fastline.vendorservice.domain.service;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.ProductService;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.OrderProduct;
import com.fastline.vendorservice.domain.entity.Product;
import com.fastline.vendorservice.domain.vo.OrderStatus;
import com.fastline.vendorservice.domain.vo.Stock;
import com.fastline.vendorservice.presentation.request.OrderProductCreateRequest;
import com.fastline.vendorservice.presentation.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderProductService {

	private final ProductService productService;

	public List<OrderProduct> createOrderProducts(
			Order order, List<OrderProductCreateRequest> createCommands) {

		List<UUID> productIds =
				createCommands.stream().map(OrderProductCreateRequest::productId).toList();
		List<Product> products = productService.findAllById(productIds);
		Map<UUID, Product> productIdMap =
				products.stream().collect(Collectors.toMap(Product::getId, p -> p));

		validateStock(productIdMap, createCommands);

		ArrayList<OrderProduct> orderProducts = new ArrayList<>();
		createCommands.forEach(
				command ->
						orderProducts.add(
								OrderProduct.create(
										order, productIdMap.get(command.productId()), command.quantity())));
		adjustQuantity(order, orderProducts);
		return orderProducts;
	}

	private void validateStock(
			Map<UUID, Product> productStocks, List<OrderProductCreateRequest> createCommands) {

		for (OrderProductCreateRequest command : createCommands) {
			Product product = productStocks.get(command.productId());
			Stock stock = product.getStock();
			if (stock.isLessThan(command.quantity()))
				throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_ENOUGH);
		}
	}

	public void adjustQuantity(Order order, List<OrderProduct> orderProducts) {

        int sign = order.getStatus() == OrderStatus.CANCELLED ? 1 : -1;

		for (OrderProduct orderProduct : orderProducts) {
			Product product = orderProduct.getProduct();
			Integer quantity = orderProduct.getQuantity();

            product.adjustStock(Stock.of(quantity * sign));
		}
	}
}
