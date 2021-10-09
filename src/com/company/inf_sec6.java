package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Matrix {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.print("Введите сообщение: ");
        String text = sc.nextLine();
        System.out.print("Введите резмер матрицы.\nВысота: ");
        Integer height = sc.nextInt();
        System.out.print("Ширина: ");
        Integer width = sc.nextInt();
        sc.nextLine();

        System.out.print("Введите последовательность столбцов без пробелов: ");
        String key1 = sc.nextLine();

        System.out.print("Введите координаты пустых значений(например 1-2,3-2,...): ");
        String key2 = sc.nextLine();


        String encodedString = encode(text, key1, key2, width, height);

        System.out.println(encodedString);

        String decodedString = decode(encodedString, key1, key2, width, height);

        System.out.println(decodedString);
    }

    // шифровка строки
    private static String encode(String text, String key1, String key2, int width, int height) {

        // пишем кодирующие матрицы
        char[][][] matrices = writeEncodeMatrices(text, key2, width, height);

        return collectEncodedMatrices(matrices, key1, height);
    }

    // сборка шифрованных матриц в строку
    private static String collectEncodedMatrices(char[][][] matrices, String key1, int height) {
        StringBuilder dtx = new StringBuilder();

        // собираем символы из матрицы, используя ключ 1, как индексатор столбцов, при этом игнорируем пустые символы и *
        for (char[][] matrix : matrices) {
            for (int keyColumn = 0; keyColumn < key1.length(); keyColumn++) {
                int columnIndex = Integer.parseInt(Character.toString(key1.charAt(keyColumn))) - 1;
                for (int row = 0; row < height; row++) {
                    char value = matrix[row][columnIndex];
                    if (value != '\0' && value != '*')
                        dtx.append(value);
                }
            }
        }
        return dtx.toString();
    }

    // дешифровка строки
    private static String decode(String text, String key1, String key2, int width, int height) {
        char[][][] matrices = writeDecodeMatrices(text, key1, key2, width, height);

        return collectDecodedMatrices(matrices, key1, width, height);
    }

    // сборка дешифрованных матриц в строку
    private static String collectDecodedMatrices(char[][][] matrices, String key1, int width, int height) {
        StringBuilder dtx = new StringBuilder();
        for (char[][] matrix : matrices) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    char c = matrix[i][j];
                    if (c != '\0' && c != '*') {
                        dtx.append(c);
                    }
                }
            }
        }
        return dtx.toString();
    }

    // создание дешифрованных матриц
    private static char[][][] writeDecodeMatrices(String text, String key1, String key2, int width, int height) {
        List<Pair<Integer, Integer>> pairs = parseKey2(key2);

        List<char[][]> matricesList = new ArrayList<>();

        int srcOffset = 0;

        while (srcOffset < text.length()) {
            char[][] chars = new char[height][width];

            // Определяем, сколько строк нужно записать в текущей матрице
            int writeRows = height;
            int left = text.length() - srcOffset;
            // Если писать осталось меньше, чем размер матрицы, то уменьшаем количество строк до нужного количества
            if (left < width * height) {
                writeRows = left / width;
                if (left % width != 0) {
                    writeRows++;
                }
            }

            // проходим по колонкам в ключе
            for (int keyColumn = 0; keyColumn < key1.length(); keyColumn++) {
                // парсим номер колонки из ключа
                int col = Integer.parseInt(Character.toString(key1.charAt(keyColumn))) - 1;

                // пишем строку
                for (int row = 0; row < height; row++) {
                    int finalRow = row;
                    // если это ячейка второго ключа - пишем *
                    if (pairs.stream().anyMatch(pair -> pair.getKey() == finalRow && pair.getValue() == col)) {
                        chars[row][col] = '*';
                    } else {
                        // если не второй ключ

                        if (srcOffset == text.length()) {
                            // если ещё не достигли конца строки
                            chars[row][col] = '\0';
                        } else {
                            // если в эту строку что-то нужно писать
                            if (row < writeRows) {
                                chars[row][col] = text.charAt(srcOffset);
                                srcOffset++;
                            }
                        }
                    }
                }
            }
            matricesList.add(chars);
        }

        return toArray(matricesList, width, height);
    }

    // Сборка списка матриц в массив матриц
    private static char[][][] toArray(List<char[][]> list, int width, int height) {
        char[][][] matrices = new char[list.size()][height][width];
        for (int i = 0; i < list.size(); i++) {
            matrices[i] = list.get(i);
        }
        return matrices;
    }

    // создание матриц шифровки
    private static char[][][] writeEncodeMatrices(String text, String key2, int width, int height) {

        List<Pair<Integer, Integer>> pairs = parseKey2(key2);

        List<char[][]> matricesList = new ArrayList<>();

        int srcOffset = 0;

        // пробегаемся по всем символам и пишем их в матрицу
        // если встречаем ключ2, пишем * и движемся дальше
        // если строка кончилась - пишем \0

        while (srcOffset < text.length()) {
            char[][] chars = new char[height][width];
            for (int indexInMatrix = 0; indexInMatrix < width * height; indexInMatrix++) {
                int row = indexInMatrix / width;
                int col = indexInMatrix % width;

                if (pairs.stream().anyMatch(pair -> pair.getKey() == row && pair.getValue() == col)) {
                    chars[row][col] = '*';
                } else {
                    if (srcOffset == text.length()) {
                        chars[row][col] = '\0';
                    } else {
                        chars[row][col] = text.charAt(srcOffset);
                        srcOffset++;
                    }
                }
            }
            matricesList.add(chars);
        }

        return toArray(matricesList, width, height);
    }

    // парсинг ключа2 в список пар<число-число> с конвертацией индексов от 1
    private static List<Pair<Integer, Integer>> parseKey2(String key2) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();

        String[] parts = key2.split(",");

        for (String part : parts) {
            String[] indexesStrings = part.split("-");
            int row = Integer.parseInt(indexesStrings[0]) - 1;
            int column = Integer.parseInt(indexesStrings[1]) - 1;

            list.add(new Pair<>(row, column));
        }
        return list;
    }
}
