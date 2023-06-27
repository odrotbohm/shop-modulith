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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import com.example.shop.cart.Article;
import com.example.shop.cart.Cart;
import com.example.shop.cart.CartCheckedOut;
import com.example.shop.cart.ShoppingCart;
import com.example.shop.user.UserIdentifier;

@ApplicationModuleTest
@RequiredArgsConstructor
class OrderManagementIntegrationTests {

	private final OrderRepository repository;

	@MockBean ShoppingCart shoppingCart;

	@Test
	void createsOrderForCheckedOutCart(Scenario scenario) {

		var article = new Article("Some article", 2000);
		var userId = new UserIdentifier(UUID.randomUUID());
		var cart = new Cart(userId).add(article, 2);

		doReturn(Optional.of(cart)).when(shoppingCart).findById(cart.getId());

		var event = new CartCheckedOut(cart.getId());

		scenario.publish(event)
				.andWaitForStateChange(() -> repository.findByUserId(userId))
				.andVerify(order -> {
					assertThat(order).hasValueSatisfying(it -> assertThat(it.getTotal()).isEqualTo(2000));
				});
	}
}
