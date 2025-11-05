package com.cafepos.menu;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MenuCompositeTest {
    @Test
    void depthFirstIterationCollectsAllNodes() {
        Menu root = new Menu("ROOT");
        Menu a = new Menu("A");
        Menu b = new Menu("B");
        root.add(a);
        root.add(b);
        a.add(new MenuItem("x", Money.of(1.0), true));
        b.add(new MenuItem("y", Money.of(2.0), false));

        List<String> names = root.allItems().stream().map(MenuComponent::name).toList();
        assertTrue(names.contains("x"));
        assertTrue(names.contains("y"));
    }

    @Test
    void vegetarianItemsFilterCorrectly() {
        Menu root = new Menu("ROOT");
        root.add(new MenuItem("veg", Money.of(1.0), true));
        root.add(new MenuItem("meat", Money.of(2.0), false));

        List<MenuItem> vegs = root.vegetarianItems();
        assertEquals(1, vegs.size());
        assertEquals("veg", vegs.get(0).name());
    }
}
