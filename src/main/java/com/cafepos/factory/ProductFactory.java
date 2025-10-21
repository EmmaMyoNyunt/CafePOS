package com.cafepos.factory;

import com.cafepos.catalog.*;
import com.cafepos.common.Money;
import com.cafepos.decorator.*;

public final class ProductFactory {

    public Product create(String recipe) {
        if (recipe == null || recipe.isBlank()) {
            throw new IllegalArgumentException("Recipe string is required");
        }

        // Split the recipe by "+"
        String[] tokens = recipe.split("\\+");

        // First token: base product
        String base = tokens[0].trim().toUpperCase();

        Product p = switch (base) {
            case "ESP" -> new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
            case "LAT" -> new SimpleProduct("P-LAT", "Latte",    Money.of(3.20));
            case "CAP" -> new SimpleProduct("P-CAP", "Cappuccino", Money.of(3.00));
            default -> throw new IllegalArgumentException("Unknown base product: " + base);
        };

        // Remaining tokens: decorators
        for (int i = 1; i < tokens.length; i++) {
            String addon = tokens[i].trim().toUpperCase();
            p = switch (addon) {
                case "SHOT" -> new ExtraShot(p);
                case "OAT"  -> new OatMilk(p);
                case "SYP"  -> new Syrup(p);
                case "L"    -> new SizeLarge(p);
                default -> throw new IllegalArgumentException("Unknown add-on: " + addon);
            };
        }

        return p;
    }
}
