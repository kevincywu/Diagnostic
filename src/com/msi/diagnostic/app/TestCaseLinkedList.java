package com.msi.diagnostic.app;

import java.util.AbstractList;
import java.util.LinkedList;

public class TestCaseLinkedList<E> extends LinkedList<E> {

    private static final long serialVersionUID = 1L;

    public TestCaseLinkedList(AbstractList<E> list) {
        for (E item : list) {
            add(item);
        }
    }

    public E next() {
        return iterator().next();
    }
}
