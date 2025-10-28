package com.cafepos.demo;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.decorator.Priced;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.*;
import com.cafepos.pricing.*;
import com.cafepos.receipt.ReceiptPrinter;

import java.util.List;
import java.util.Scanner;

public final class CLIDemo {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Café POS (Week 6) — Multi-Item CLI ===");

        ProductFactory factory = new ProductFactory();
        Order order = new Order(OrderIds.next());

        // Defaults (can be changed via menu)
        DiscountPolicy discount = new NoDiscount();
        TaxPolicy tax = new FixedRateTaxPolicy(10);
        PaymentStrategy payment = new CashPayment();
        ReceiptPrinter printer = new ReceiptPrinter();

        boolean running = true;
        while (running) {
            System.out.println("\nOrder #" + order.id());
            System.out.println("1) Add item (recipe)");
            System.out.println("2) View order");
            System.out.println("3) Choose discount");
            System.out.println("4) Choose payment");
            System.out.println("5) Checkout");
            System.out.println("6) Remove item");
            System.out.println("7) Exit");
            System.out.print("Select: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> addItem(factory, order);
                case "2" -> viewOrder(order);
                case "3" -> discount = chooseDiscount();
                case "4" -> payment = choosePayment();
                case "5" -> {
                    checkout(order, discount, tax, printer, payment);
                    order = new Order(OrderIds.next());
                    discount = new NoDiscount();
                    payment = new CashPayment();
                }
                case "6" -> removeItem(order);
                case "7" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Bye!");
    }

    private static void addItem(ProductFactory factory, Order order) {
        System.out.print("Enter recipe (e.g. ESP+SHOT+OAT or LAT+L): ");
        String recipe = sc.nextLine().trim();
        try {
            Product p = factory.create(recipe);
            System.out.print("Quantity: ");
            int qty = readInt();
            order.addItem(new LineItem(p, qty));
            System.out.println("Added: " + qty + " x " + p.name());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewOrder(Order order) {
        List<LineItem> items = order.items();
        if (items.isEmpty()) {
            System.out.println("(Cart empty)");
            return;
        }
        System.out.println("Items:");
        for (int i = 0; i < items.size(); i++) {
            LineItem li = items.get(i);
            Money unit = (li.product() instanceof Priced p) ? p.price() : li.product().basePrice();
            System.out.println(" " + i + ") " + li.product().name() + " x" + li.quantity() + " = " + unit.multiply(li.quantity()));
        }
        System.out.println("Subtotal: " + order.subtotal());
    }

    private static DiscountPolicy chooseDiscount() {
        System.out.println("\nDiscounts:");
        System.out.println("1) None");
        System.out.println("2) Loyalty 5%");
        System.out.println("3) Coupon €1 off");
        System.out.print("Select: ");
        return switch (sc.nextLine().trim()) {
            case "2" -> new LoyaltyPercentDiscount(5);
            case "3" -> new FixedCouponDiscount(Money.of(1.00));
            default -> new NoDiscount();
        };
    }

    private static PaymentStrategy choosePayment() {
        System.out.println("\nPayments:");
        System.out.println("1) Cash");
        System.out.println("2) Card");
        System.out.println("3) Wallet");
        System.out.print("Select: ");
        return switch (sc.nextLine().trim()) {
            case "2" -> new CardPayment("1234567812341234");
            case "3" -> new WalletPayment("user-wallet-789");
            default -> new CashPayment();
        };
    }

    private static void checkout(Order order,
                                 DiscountPolicy discount,
                                 TaxPolicy tax,
                                 ReceiptPrinter printer,
                                 PaymentStrategy payment) {

        if (order.items().isEmpty()) {
            System.out.println("Nothing to checkout.");
            return;
        }

        Money subtotal = order.subtotal();
        PricingService pricing = new PricingService(discount, tax);
        var pr = pricing.price(subtotal);

        int totalQty = order.items().stream().mapToInt(LineItem::quantity).sum();
        String receipt = printer.print("Cart", totalQty, pr.subtotal(), pr.discount(), pr.tax(), pr.total());

        System.out.println("\n" + receipt);
        order.pay(payment);
    }

    private static void removeItem(Order order) {
        List<LineItem> items = order.items();
        if (items.isEmpty()) {
            System.out.println("(Cart empty)");
            return;
        }

        System.out.println("Items:");
        for (int i = 0; i < items.size(); i++) {
            LineItem li = items.get(i);
            System.out.println(" " + i + ") " + li.product().name() + " x" + li.quantity());
        }

        System.out.print("Enter item number to remove: ");
        int index = readInt();
        try {
            order.removeItem(index);
            System.out.println("Item removed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}