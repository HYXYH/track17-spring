package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * <p>
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {
    static final int DEFAULT_CAPACITY = 8;
    private int myCapacity;
    private int[] array;

    public MyArrayList() {
        myCapacity = DEFAULT_CAPACITY;
        array = new int[myCapacity];
    }

    public MyArrayList(int capacity) {
        if (capacity == 0) {
            myCapacity = DEFAULT_CAPACITY;
        } else {
            myCapacity = capacity;
        }
        array = new int[myCapacity];
    }

    @Override
    public void add(int item) {
        checkArrayCapacity();
        array[mySize] = item;
        mySize++;
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= mySize) {
            throw new NoSuchElementException();
        }
        final int val = array[idx];
        if (mySize - 1 - idx > 0) {
            System.arraycopy(array, idx + 1, array, idx, mySize - 1 - idx);
        }
        checkArrayCapacity();
        mySize--;
        return val;
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= mySize) {
            throw new NoSuchElementException();
        }
        return array[idx];
    }

    private void checkArrayCapacity() {
        if (myCapacity == mySize) {
            realloc(myCapacity * 2);
        } else if ((mySize < myCapacity / 2) &&  (myCapacity < DEFAULT_CAPACITY)) {
            realloc(myCapacity / 2);
        }
    }

    private void realloc(int newCapacity) {
        int[] oldArray = array;
        array = new int[newCapacity];
        System.arraycopy(oldArray, 0, array, 0, mySize);
        myCapacity = newCapacity;
    }
}
