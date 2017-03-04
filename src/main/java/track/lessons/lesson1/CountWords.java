package track.lessons.lesson1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Задание 1: Реализовать два метода
 *
 * Формат файла: текстовый, на каждой его строке есть (или/или)
 * - целое число (int)
 * - текстовая строка
 * - пустая строка (пробелы)
 *
 *
 * Пример файла - words.txt в корне проекта
 *
 * ******************************************************************************************
 *  Пожалуйста, не меняйте сигнатуры методов! (название, аргументы, возвращаемое значение)
 *
 *  Можно дописывать новый код - вспомогательные методы, конструкторы, поля
 *
 * ******************************************************************************************
 *
 */
public class CountWords {

    /**
     * Метод на вход принимает объект File, изначально сумма = 0
     * Нужно пройти по всем строкам файла, и если в строке стоит целое число,
     * то надо добавить это число к сумме
     * @param file - файл с данными
     * @return - целое число - сумма всех чисел из файла
     */
    public long countNumbers(File file) throws Exception {
        BufferedReader reader = null;
        int rezultInt = 0;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                try {
                    int foo = Integer.parseInt(text);
                    rezultInt += foo;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return rezultInt;
    }


    /**
     * Метод на вход принимает объект File, изначально результат= ""
     * Нужно пройти по всем строкам файла, и если в строка не пустая и не число
     * то надо присоединить ее к результату через пробел
     * @param file - файл с данными
     * @return - результирующая строка
     */
    public String concatWords(File file) throws Exception {

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder("");

        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                if (text.length() == 0) {
                    continue;
                }
                try {
                    int foo = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    if (builder.toString().equals("")) {
                        builder.append(text);
                    } else {
                        builder.append(" ").append(text);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

}
