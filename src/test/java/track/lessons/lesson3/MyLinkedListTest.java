package track.lessons.lesson3;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MyLinkedListTest {

    @Test(expected = NoSuchElementException.class)
    public void emptyList() throws Exception {
        List list = new MyLinkedList();
        Assert.assertTrue(list.size() == 0);
        list.get(0);
    }

    @Test
    public void listAdd() throws Exception {
        List list = new MyLinkedList();
        list.add(1);

        Assert.assertTrue(list.size() == 1);
    }

    @Test
    public void listAddRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));

        list.remove(1);
        Assert.assertEquals(3, list.get(1));
        Assert.assertEquals(1, list.get(0));

        list.remove(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void listRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void listRemove2() throws Exception {
        List list = new MyLinkedList();
        list.remove(0);
    }

    @Test(expected = NoSuchElementException.class)
    public void stackPopTest() throws Exception {
        Stack stack = new MyLinkedList();
        stack.pop();
    }

    @Test
    public void stackPushPopTest1() throws Exception {
        Stack stack = new MyLinkedList();
        stack.push(10);
        Assert.assertEquals(10, stack.pop());
    }

    @Test
    public void stackPushPopTest2() throws Exception {
        Stack stack = new MyLinkedList();
        for (int i = 0; i <= 100; i++) {
            stack.push(i);
        }
        for (int i = 100; i >= 0; i--) {
            Assert.assertEquals(i, stack.pop());
        }
    }


    @Test(expected = NoSuchElementException.class)
    public void queueEnqueTest() throws Exception {
        Queue queue = new MyLinkedList();
        queue.dequeue();
    }

    @Test
    public void queueEnqueDequeTest1() throws Exception {
        Queue queue = new MyLinkedList();
        queue.enqueue(10);
        Assert.assertEquals(10, queue.dequeue());
    }

    @Test
    public void queueEnqueDequeTest2() throws Exception {
        Queue queue = new MyLinkedList();
        for (int i = 0; i <= 100; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i <= 100; i++) {
            Assert.assertEquals(i, queue.dequeue());
        }
    }

}