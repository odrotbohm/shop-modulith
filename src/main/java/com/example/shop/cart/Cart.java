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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

import com.example.shop.cart.Article.ArticleIdentifier;
import com.example.shop.cart.Cart.CartIdentifier;
import com.example.shop.cart.Cart.CartItem.CartItemIdentifier;
import com.example.shop.user.UserIdentifier;

public class Cart implements AggregateRoot<Cart, CartIdentifier> {

	private final @Getter CartIdentifier id;
	private final @Getter UserIdentifier userId;
	private final List<CartItem> items;

	public Cart(UserIdentifier userId) {

		this.id = new CartIdentifier(UUID.randomUUID());
		this.userId = userId;
		this.items = new ArrayList<>();
	}

	public Cart add(Article article, int amount) {

		this.items.add(new CartItem(article, amount));

		return this;
	}

	public Stream<CartItem> stream() {
		return items.stream();
	}

	public record CartIdentifier(UUID cartId) implements Identifier {}

	@Getter
	public static class CartItem implements Entity<Cart, CartItemIdentifier> {

		private final CartItemIdentifier id;
		private final Association<Article, ArticleIdentifier> article;
		private final int amount;
		private final long priceInCents;

		public CartItem(Article article, int amount) {

			this.id = new CartItemIdentifier(UUID.randomUUID());
			this.article = Association.forAggregate(article);
			this.amount = amount;
			this.priceInCents = article.getPriceInCents();
		}

		public record CartItemIdentifier(UUID id) implements Identifier {}
	}
}
