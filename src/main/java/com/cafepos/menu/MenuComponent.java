package com.cafepos.menu;

import com.cafepos.common.Money;
import java.util.Iterator;

public abstract class MenuComponent {
    // Composite operations (safe default: unsupported)
    public void add(MenuComponent c) { throw new UnsupportedOperationException(); }
    public void remove(MenuComponent c) { throw new UnsupportedOperationException(); }
    public MenuComponent getChild(int i) { throw new UnsupportedOperationException(); }

    // Leaf data
    public String name() { throw new UnsupportedOperationException(); }
    public Money price() { throw new UnsupportedOperationException(); }
    public boolean vegetarian() { return false; }

    // Iteration / print hooks
    public Iterator<MenuComponent> iterator() { throw new UnsupportedOperationException(); }
    public void print() { throw new UnsupportedOperationException(); }
}
