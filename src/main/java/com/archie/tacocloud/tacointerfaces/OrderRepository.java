package com.archie.tacocloud.tacointerfaces;

import com.archie.tacocloud.dom.Order;

public interface OrderRepository
{
	Order save(Order order);
}
