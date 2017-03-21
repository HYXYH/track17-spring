package track.lessons.lesson5generics;

import track.util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Cypher {

    public static final int SYMBOL_DIST = 32;

    private Map<Character, Integer> readData(String data) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < data.length(); i++) {
            char ch = data.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
                if (ch < 'Z') {
                    ch += SYMBOL_DIST;
                }
                // Если это буква, то собираем частотную информацию
                if (map.containsKey(ch)) {
                    map.put(ch, map.get(ch) + 1);
                } else {
                    map.put(ch, 1);
                }


            }
        }
        return map;
    }

    /**
    На вход приходит текст
    1. Считываем readData() и получаем мапу {Символ -> Кол-во употреблений}
    2. Далее нам нужно отсортировать пары ключ-значение по значению
     (Называются{@code List<Map.Entry<Character, Integer>>})
     (то есть по частоте употребления). Для этого можно создать список этих пар и отсортировать список.
     У java.lang.List есть вспомогательный метод {@link List#sort(Comparator)}
     Где Comparator - это логика сравнения объектов.

     3. После того, как получен отсортированный список {@code List<Map.Entry<Character, Integer>>} нужно превратить его
        обратно в Map для того, чтобы иметь быстрый доступ get().

     */
    public Map<Character, Integer> buildHist(String data) {
        Map<Character, Integer> map = readData(data);
        ArrayList<Map.Entry<Character, Integer>> sorted = new ArrayList<Map.Entry<Character, Integer>>(map.entrySet());
        sorted.sort(Comparator.comparingInt(Map.Entry::getValue));
        map = new LinkedHashMap<Character, Integer>();
        for (Map.Entry<Character, Integer> pair : sorted) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    /**
     * Заменяем символы зашифрованного текста по таблицам частот
     *
     * @param in - отсортированный по частоте алфавит для основного текста
     * @param out - отсортированный по частоте алфавит для шифрованного текста
     * @param encrypted - зашифрованный текст
     * @return расшифрованный текст
     */
    public String merge(List<Character> in, List<Character> out, String encrypted) {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < encrypted.length(); i++) {
            if (encrypted.charAt(i) == ' ') {
                builder.append(" ");
                continue;
            }

            int pos = 0;
            for (Character c : out) {
                pos++;
                if (c.compareTo(encrypted.charAt(i)) == 0) {
                    break;
                }
            }
            if (pos >= in.size()) {
                builder.append(" ");
            } else {
                builder.append(in.get(pos));
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        Cypher cypher = new Cypher();

        Map<Character, Integer> dataHist = cypher.buildHist(Util.readFile("data.txt"));

        String encryptedText = Util.encrypt(Util.readFile("toEncrypt.txt"));
        Map<Character, Integer> encryptedHist = cypher.buildHist(encryptedText);

        String result = cypher.merge(
                new LinkedList<>(dataHist.keySet()),
                new LinkedList<>(encryptedHist.keySet()),
                encryptedText);
        System.out.println(result);

    }

}



