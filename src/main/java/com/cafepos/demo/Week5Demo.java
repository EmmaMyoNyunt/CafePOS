package com.cafepos.demo;

import com.cafepos.catalog.Product;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;

public final class Week5Demo {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();

        // Create products using factory recipes
        Product drink1 = factory.create("ESP+SHOT+OAT"); // Espresso + Extra Shot + Oat Milk
        Product drink2 = factory.create("LAT+L");        // Large Latte

        // Create a new order
        Order order = new Order(OrderIds.next());

        // Add items to the order
        order.addItem(new LineItem(drink1, 1));
        order.addItem(new LineItem(drink2, 2));

        // Print the receipt
        System.out.println("Order #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(
                    " - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal()
            );
        }
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " + order.taxAtPercent(10));
        System.out.println("Total: " + order.totalWithTax(10));
    }
}
