package com.cafepos.demo;

import com.cafepos.pricing.*;
import com.cafepos.receipt.*;
import com.cafepos.smells.OrderManagerGod;

public class Week6Demo {
    public static void main(String[] args) {
        DiscountPolicy discount = new LoyaltyPercentDiscount(5);
        TaxPolicy tax = new FixedRateTaxPolicy(10);
        ReceiptPrinter printer = new ReceiptPrinter();

        // Inject dependencies
        OrderManagerGod manager = new OrderManagerGod(discount, tax, printer);

        //  Example 1: Latte with loyalty discount
        String output1 = manager.process("LAT+L", 1, "CASH", false);

        //  Example 2: Espresso with extra shot
        String output2 = manager.process("ESP+SHOT", 2, "CARD", false);

        System.out.println(output1);
        System.out.println(output2);
    }
}