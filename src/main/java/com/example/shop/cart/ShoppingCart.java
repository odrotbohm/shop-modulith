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

import java.util.Optional;

import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.cart.Cart.CartIdentifier;
import com.example.shop.user.UserIdentifier;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCart {

	private final CartRepository carts;
	private final ArticleRepository articles;

	private final ApplicationEventPublisher events;

	public Optional<Cart> findById(CartIdentifier id) {
		return carts.findById(id);
	}

	Article add(Article article) {
		return articles.save(article);
	}

	Cart add(Cart cart) {
		return carts.save(cart);
	}

	void checkout(UserIdentifier userId) {

		var cart = carts.findByUserId(userId).orElseThrow();

		events.publishEvent(new CartCheckedOut(cart.getId()));
	}
}
