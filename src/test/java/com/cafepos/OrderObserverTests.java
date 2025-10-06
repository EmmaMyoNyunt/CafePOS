package com.cafepos;

import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderObserverTests {

    @Test void observers_notified_on_item_add() {
        var product = new SimpleProduct("A","A", Money.of(2));
        var order = new Order(1);
        List<String> events = new ArrayList<>();
        order.register((o, evt) -> events.add(evt));

        order.addItem(new LineItem(product, 1));

        assertTrue(events.contains("itemAdded"));
    }

    @Test void multiple_observers_receive_ready_event() {
        var product = new SimpleProduct("A","A", Money.of(2));
        var order = new Order(1);
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();

        order.register((o, evt) -> a.add(evt));
        order.register((o, evt) -> b.add(evt));

        order.markReady();

        assertTrue(a.contains("ready"));
        assertTrue(b.contains("ready"));
    }
}
