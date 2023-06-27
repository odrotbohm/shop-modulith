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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

import com.example.shop.cart.Article.ArticleIdentifier;
import com.example.shop.order.Order.OrderIdentifier;
import com.example.shop.order.Order.OrderItem.OrderItemIdentifier;
import com.example.shop.user.UserIdentifier;

class Order implements AggregateRoot<Order, OrderIdentifier> {

	private final @Getter OrderIdentifier id;
	private final @Getter UserIdentifier userId;
	private final List<OrderItem> items;

	Order(UserIdentifier userId, List<OrderItem> items) {

		this.id = new OrderIdentifier(UUID.randomUUID());
		this.userId = userId;
		this.items = new ArrayList<>(items);
	}

	public long getTotal() {

		return items.stream()
				.map(OrderItem::getPriceInCents)
				.reduce(0L, (l, r) -> l + r);
	}

	public record OrderIdentifier(UUID id) implements Identifier {}

	@Getter
	public static class OrderItem implements Entity<Order, OrderItemIdentifier> {

		private final OrderItemIdentifier id;
		private final ArticleIdentifier article;
		private final int amount;
		private final long priceInCents;

		public OrderItem(ArticleIdentifier article, int amount, long priceInCents) {

			this.id = new OrderItemIdentifier(UUID.randomUUID());
			this.article = article;
			this.amount = amount;
			this.priceInCents = priceInCents;
		}

		public record OrderItemIdentifier(UUID orderItemId) implements Identifier {}
	}
}
