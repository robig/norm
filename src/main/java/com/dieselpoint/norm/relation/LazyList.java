package com.dieselpoint.norm.relation;

import java.lang.reflect.Method;
import java.util.*;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Query;

public class LazyList implements List<Object> {

    private Database db;
    private Query query;
    private Class<?> entityClass;

    public LazyList(Database db, Query q, Class<?> entityClass) {
        this.db = db;
        this.query = q;
        this.entityClass = entityClass;
    }

    List<Object> data = null;

    private List<Object> fetchData() {
        if (data != null)
            return data;
        data = (List<Object>) query.results(entityClass);
        return data;
    }

    @Override
    public int size() {
        return fetchData().size();
    }

    @Override
    public boolean isEmpty() {
        return fetchData().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return fetchData().contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return fetchData().iterator();
    }

    @Override
    public Object[] toArray() {
        return fetchData().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return fetchData().toArray(a);
    }

    @Override
    public boolean add(Object e) {
        return fetchData().add(e);
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return fetchData().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public Object get(int index) {
        return fetchData().get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return fetchData().set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return fetchData().listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return fetchData().listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return fetchData().subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return fetchData().toString();
    }
}
