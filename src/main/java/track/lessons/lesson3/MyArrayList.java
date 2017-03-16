package track.lessons.lesson3;

import java.util.NoSuchElementException;

import static java.lang.System.arraycopy;

/**
 * Должен наследовать List
 * <p>
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {
    private int myCapacity = 100;
    private int[] array;

    public MyArrayList() {
        array = new int[myCapacity];
    }

    public MyArrayList(int capacity) {
        if (capacity == 0) {
            myCapacity = 1;
        } else {
            myCapacity = capacity;
        }
        array = new int[myCapacity];
    }

    @Override
    public void add(int item) {
        if (myCapacity == mySize) {
            myCapacity *= 2;
            realloc();
        }
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
            arraycopy(array, idx + 1, array, idx, mySize - 1 - idx);
        }
        if ((mySize < myCapacity / 2) &&  (myCapacity < 10)) {
            myCapacity /= 2;
            realloc();
        }
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

    private void realloc() {
        int[] oldArray = array;
        array = new int[myCapacity];
        arraycopy(oldArray, 0, array, 0, mySize);
    }
}
