package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Stack, Queue  {

    private Node head = null;
    private Node tail = null;

    @Override
    public void push(int value) {
        add(value);
    }

    @Override
    public int pop() {
        return remove(mySize - 1);
    }

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeue() {
        return remove(0);
    }


    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    @Override
    public void add(int item) {
        if (mySize == 0) {
            head = new Node(null, null, item);
            tail = head;
        } else {
            tail.next = new Node(tail, null, item);
            tail = tail.next;
        }
        mySize++;
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= mySize) {
            throw new NoSuchElementException();
        } else {
            Node nodeToDelete = head;
            for (int i = 0; i < idx; i++) {
                nodeToDelete = nodeToDelete.next;
            }

            if (nodeToDelete == head) {
                head = head.next;
            } else {
                nodeToDelete.prev.next = nodeToDelete.next;
            }

            if (nodeToDelete == tail) {
                tail = tail.prev;
            }
            mySize--;
            return nodeToDelete.val;
        }
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        if (idx < 0 || idx >= mySize) {
            throw new NoSuchElementException();
        } else {
            Node nodeToDelete = head;
            for (int i = 0; i < idx; i++) {
                nodeToDelete = nodeToDelete.next;
            }
            return nodeToDelete.val;
        }
    }

}
