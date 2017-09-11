package com.msi.diagnostic.data;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class MapTestSet<K, V> {

    private LinkedHashMap<K, WeakReference<V>> mMaps =
            new LinkedHashMap<K, WeakReference<V>>();

    public void put(K key, V v) {
        WeakReference<V> vWeakRef = new WeakReference<V>(v);
        mMaps.put(key, vWeakRef);
    }

    public V get(K key) {
        return mMaps.get(key).get();
    }

    public ArrayList<V> getAllTestItems() {
        ArrayList<V> items = new ArrayList<V>();

        Collection<WeakReference<V>> itemCollection = mMaps.values();
        Iterator<WeakReference<V>> it = itemCollection.iterator();
        while(it.hasNext()) {
            V item = it.next().get();
            items.add(item);
        }

        return items;
    }
}
