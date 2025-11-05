package com.cafepos.printing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LegacyPrinterAdapterTest {

    static class FakeLegacy {
        int lastLen = -1;
        void legacyPrint(byte[] payload) { lastLen = payload.length; }
    }

    @Test
    void adapter_converts_text_to_bytes() {
        var fake = new FakeLegacy();

        // Use an anonymous inner class to intercept the call
        Printer p = new LegacyPrinterAdapter(new vendor.legacy.LegacyThermalPrinter() {
            @Override
            public void legacyPrint(byte[] payload) {
                fake.legacyPrint(payload);
            }
        });

        p.print("ABC");

        assertTrue(fake.lastLen >= 3, "Adapter should convert text into bytes");
    }
}
