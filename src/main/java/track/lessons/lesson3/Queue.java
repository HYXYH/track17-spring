package track.lessons.lesson3;

/**
 * Created by Oskar on 14.03.17.
 */
// Очередь - структура данных, удовлетворяющая правилу First IN First OUT
interface Queue {
    void enqueue(int value); // поместить элемент в очередь

    int dequeue(); // вытащить первый элемент из очереди
}
