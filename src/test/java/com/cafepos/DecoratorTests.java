package com.cafepos;

import com.cafepos.catalog.Product;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.decorator.*;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.factory.ProductFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoratorTests {

    @Test
    void singleDecorator_addsPriceAndName() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);

        assertEquals("Espresso + Extra Shot", withShot.name());
        assertEquals(Money.of(3.30), ((Priced) withShot).price());
    }

    @Test
    void stackedDecorators_addUpCorrectly() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        assertEquals("Espresso + Extra Shot + Oat Milk (Large)", decorated.name());
        assertEquals(Money.of(4.50), ((Priced) decorated).price());
    }

    @Test
    void factory_buildsSameProductAsManualChaining() {
        Product manual = new SizeLarge(new OatMilk(new ExtraShot(
                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
        )));
        Product factoryMade = new ProductFactory().create("ESP+SHOT+OAT+L");

        assertEquals(manual.name(), factoryMade.name());
        assertEquals(((Priced) manual).price(), ((Priced) factoryMade).price());
    }

    @Test
    void orderUsesDecoratedPrice() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso); // 3.30 each

        Order order = new Order(1);
        order.addItem(new LineItem(withShot, 2));

        assertEquals(Money.of(6.60), order.subtotal());
    }

    // 🧪 Step 7 Activity: Factory vs. Manual Chaining
    @Test
    void factory_and_manual_build_same_drink_in_order() {
        // Manual chaining
        Product manual = new SizeLarge(
                new OatMilk(
                        new ExtraShot(
                                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
                        )
                )
        );

        // Factory-built
        Product factoryMade = new ProductFactory().create("ESP+SHOT+OAT+L");

        // Compare name & price
        assertEquals(manual.name(), factoryMade.name());
        assertEquals(((Priced) manual).price(), ((Priced) factoryMade).price());

        // Compare behavior inside an order
        Order order1 = new Order(100);
        Order order2 = new Order(101);
        order1.addItem(new LineItem(manual, 2));
        order2.addItem(new LineItem(factoryMade, 2));

        assertEquals(order1.totalWithTax(10), order2.totalWithTax(10),
                "Factory and manual should produce the same total with tax");

    }
}
