/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.shop.order;

import static java.util.stream.Collectors.*;

import lombok.RequiredArgsConstructor;

import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.example.shop.cart.CartCheckedOut;
import com.example.shop.cart.ShoppingCart;
import com.example.shop.order.Order.OrderItem;

@Component
@RequiredArgsConstructor
class OrderEventListeners {

	private final ShoppingCart shoppingCart;
	private final OrderRepository orders;

	@ApplicationModuleListener
	void on(CartCheckedOut event) {

		var cart = shoppingCart.findById(event.identifier()).orElseThrow();

		var order = cart.stream()
				.map(it -> new OrderItem(it.getArticle().getId(), it.getAmount(), it.getPriceInCents()))
				.collect(collectingAndThen(toList(), it -> new Order(cart.getUserId(), it)));

		orders.save(order);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException o_O) {}
	}
}
