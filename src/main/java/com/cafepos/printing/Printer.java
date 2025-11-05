package com.cafepos.printing;

//Declares one method: print(String receiptText).

//This is the target interface that the rest of our POS system will rely on.
public interface Printer {
    void print(String receiptText);
}
