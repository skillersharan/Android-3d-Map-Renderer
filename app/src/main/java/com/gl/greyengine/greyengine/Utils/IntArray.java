package com.gl.greyengine.greyengine.Utils;

import java.util.Arrays;

/**
 * Created by Sharan on 06-Jul-15.
 */
public class IntArray {
    public int[] items;
    public int size;
    public boolean ordered;

    public IntArray() {
        this(true, 16);
    }

    public IntArray(int capacity) {
        this(true, capacity);
    }

    public IntArray(boolean ordered, int capacity) {
        this.ordered = ordered;
        items = new int[capacity];
    }

    public IntArray(IntArray array) {
        this.ordered = array.ordered;
        size = array.size;
        items = new int[size];
        System.arraycopy(array.items, 0, items, 0, size);
    }

    public IntArray(int[] array) {
        this(true, array, 0, array.length);
    }


    public IntArray(boolean ordered, int[] array, int startIndex, int count) {
        this(ordered, count);
        size = count;
        System.arraycopy(array, startIndex, items, 0, count);
    }

    static public IntArray with(int... array) {
        return new IntArray(array);
    }

    public void add(int value) {
        int[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size++] = value;
    }

    public void addAll(IntArray array) {
        addAll(array, 0, array.size);
    }

    public void addAll(IntArray array, int offset, int length) {
        if (offset + length > array.size)
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        addAll(array.items, offset, length);
    }

    public void addAll(int... array) {
        addAll(array, 0, array.length);
    }

    public void addAll(int[] array, int offset, int length) {
        int[] items = this.items;
        int sizeNeeded = size + length;
        if (sizeNeeded > items.length) items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
        System.arraycopy(array, offset, items, size, length);
        size += length;
    }

    public int get(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        return items[index];
    }

    public void set(int index, int value) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] = value;
    }

    public void incr(int index, int value) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] += value;
    }

    public void mul(int index, int value) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] *= value;
    }

    public void insert(int index, int value) {
        if (index > size)
            throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        int[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        if (ordered)
            System.arraycopy(items, index, items, index + 1, size - index);
        else
            items[size] = items[index];
        size++;
        items[index] = value;
    }

    public void swap(int first, int second) {
        if (first >= size)
            throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + size);
        if (second >= size)
            throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + size);
        int[] items = this.items;
        int firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    public boolean contains(int value) {
        int i = size - 1;
        int[] items = this.items;
        while (i >= 0)
            if (items[i--] == value) return true;
        return false;
    }

    public int indexOf(int value) {
        int[] items = this.items;
        for (int i = 0, n = size; i < n; i++)
            if (items[i] == value) return i;
        return -1;
    }

    public int lastIndexOf(int value) {
        int[] items = this.items;
        for (int i = size - 1; i >= 0; i--)
            if (items[i] == value) return i;
        return -1;
    }

    public boolean removeValue(int value) {
        int[] items = this.items;
        for (int i = 0, n = size; i < n; i++) {
            if (items[i] == value) {
                removeIndex(i);
                return true;
            }
        }
        return false;
    }

    public int removeIndex(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        int[] items = this.items;
        int value = items[index];
        size--;
        if (ordered)
            System.arraycopy(items, index + 1, items, index, size - index);
        else
            items[index] = items[size];
        return value;
    }

    public void removeRange(int start, int end) {
        if (end >= size)
            throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + size);
        if (start > end)
            throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end);
        int[] items = this.items;
        int count = end - start + 1;
        if (ordered)
            System.arraycopy(items, start + count, items, start, size - (start + count));
        else {
            int lastIndex = this.size - 1;
            for (int i = 0; i < count; i++)
                items[start + i] = items[lastIndex - i];
        }
        size -= count;
    }

    public boolean removeAll(IntArray array) {
        int size = this.size;
        int startSize = size;
        int[] items = this.items;
        for (int i = 0, n = array.size; i < n; i++) {
            int item = array.get(i);
            for (int ii = 0; ii < size; ii++) {
                if (item == items[ii]) {
                    removeIndex(ii);
                    size--;
                    break;
                }
            }
        }
        return size != startSize;
    }

    public int pop() {
        return items[--size];
    }

    public int peek() {
        return items[size - 1];
    }

    public int first() {
        if (size == 0) throw new IllegalStateException("Array is empty.");
        return items[0];
    }

    public void clear() {
        size = 0;
    }

    public int[] shrink() {
        if (items.length != size) resize(size);
        return items;
    }

    public int[] ensureCapacity(int additionalCapacity) {
        int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded > items.length) resize(Math.max(8, sizeNeeded));
        return items;
    }

    protected int[] resize(int newSize) {
        int[] newItems = new int[newSize];
        int[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    public void sort() {
        Arrays.sort(items, 0, size);
    }

    public void reverse() {
        int[] items = this.items;
        for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
            int ii = lastIndex - i;
            int temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public void truncate(int newSize) {
        if (size > newSize) size = newSize;
    }

    public int[] toArray() {
        int[] array = new int[size];
        System.arraycopy(items, 0, array, 0, size);
        return array;
    }

    public int hashCode() {
        if (!ordered) return super.hashCode();
        int[] items = this.items;
        int h = 1;
        for (int i = 0, n = size; i < n; i++)
            h = h * 31 + items[i];
        return h;
    }

    public boolean equals(Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof IntArray)) return false;
        IntArray array = (IntArray) object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        int[] items1 = this.items;
        int[] items2 = array.items;
        for (int i = 0; i < n; i++)
            if (items[i] != array.items[i]) return false;
        return true;
    }

    public String toString() {
        if (size == 0) return "[]";
        int[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    public String toString(String separator) {
        if (size == 0) return "";
        int[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
}
