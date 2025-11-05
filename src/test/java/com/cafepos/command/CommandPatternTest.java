package com.cafepos.command;

import com.cafepos.domain.*;
import com.cafepos.payment.CardPayment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandPatternTest {

    @Test
    void add_and_undo_item() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(2);

        // bind an AddItemCommand to slot 0
        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));

        remote.press(0); // add one espresso
        int afterAdd = order.items().size();

        remote.undo();  // undo the add
        int afterUndo = order.items().size();

        assertEquals(1, afterAdd, "One item should be added");
        assertEquals(0, afterUndo, "Undo should remove the last item");
    }

    @Test
    void pay_order_command_runs_without_error() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        var payCmd = new PayOrderCommand(service,
                new CardPayment("1234567890123456"), 10);

        assertDoesNotThrow(payCmd::execute,
                "PayOrderCommand should execute without throwing errors");
    }
}
