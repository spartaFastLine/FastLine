package com.fastline.vendorservice.domain.service;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.ProductService;
import com.fastline.vendorservice.application.command.CreateOrderProductCommand;
import com.fastline.vendorservice.application.command.UpdateProductCommand;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.OrderProduct;
import com.fastline.vendorservice.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProductService {

	private final ProductService productService;

	public List<OrderProduct> createOrderProducts(
			Order order, List<CreateOrderProductCommand> createCommands) {

		List<UUID> productIds =
				createCommands.stream().map(CreateOrderProductCommand::productId).toList();
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
		adjustQuantity(orderProducts);
		return orderProducts;
	}

	private void validateStock(
			Map<UUID, Product> productStocks, List<CreateOrderProductCommand> createCommands) {

		for (CreateOrderProductCommand command : createCommands) {
			Product product = productStocks.get(command.productId());
			if (product.getStock() < command.quantity())
				throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_ENOUGH);
		}
	}

	public void adjustQuantity(List<OrderProduct> orderProducts) {

		for (OrderProduct orderProduct : orderProducts) {
			Product product = orderProduct.getProduct();
			productService.updateProduct(
					new UpdateProductCommand(
							product.getName(),
							product.getStock() - orderProduct.getQuantity(),
							product.getPrice().getValue()),
					product.getId());
		}
	}
}
