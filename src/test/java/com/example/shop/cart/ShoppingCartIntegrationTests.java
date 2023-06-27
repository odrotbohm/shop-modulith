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
package com.example.shop.cart;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import com.example.shop.user.UserIdentifier;

@ApplicationModuleTest
@RequiredArgsConstructor
class ShoppingCartIntegrationTests {

	private final ShoppingCart shoppingCart;

	@Test
	void checkingOutACartPublishesEvent(Scenario scenario) {

		var article = shoppingCart.add(new Article("Some article", 2000));

		var userId = new UserIdentifier(UUID.randomUUID());
		var cart = new Cart(userId).add(article, 2);

		shoppingCart.add(cart);

		scenario.stimulate(() -> shoppingCart.checkout(userId))
				.andWaitForEventOfType(CartCheckedOut.class)
				.toArrive();
	}
}
