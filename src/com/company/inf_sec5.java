package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class inf_sec5 {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.print("Введите текст для зашифровки: ");
        String text = sc.nextLine();
        System.out.print("Введите резмер матрицы.\nВысота: ");
        Integer height = sc.nextInt();
        System.out.print("Ширина: ");
        Integer width = sc.nextInt();
        sc.nextLine();

        int size = width * height;
        int matricesCount = text.length() / size + (text.length() % size > 0 ? 1 : 0);
        char[][][]matrices = new char[matricesCount][height][width];
        String decodeString=text;

        for(int i=0; i < matricesCount; i++){
            int length = size;
            if (i==matricesCount-1){
                length=text.length() % size;
            }
            char[][] matrix = toMatrix(text, width, height, i * size, length);
            matrices[i]=matrix;
        }
        for(int i=0; i < matricesCount; i++){
            printMatrix(matrices[i]);
        }

        System.out.print("Введите последовательность столбцов без пробелов: ");
        String key = sc.nextLine();

        String encodedString = encode(text, key, width, height);

        System.out.println(encodedString);

        String decodedString = decode(encodedString, key, width, height);

        System.out.println(decodeString);


    }

    public static char[][] toMatrix(String text, int width, int height, int startIndex, int length) {

        char[][] chars = new char[height][width];
        int cell = 0;

        for (int i = startIndex; i < startIndex + length; i++) {
            int row = cell / width % height;
            int col = cell % width;
            chars[row][col] = text.charAt(i);
            cell++;
        }
        return chars;
    }
    public static void printMatrix(char[][] matrix){

        for( int i=0; i < matrix[0].length * 4 + 1; i++){
            System.out.print('-');
        }
        System.out.println();

        for( int i=0; i < matrix.length; i++){
            for(int j =0; j < matrix[i].length; j++){
                System.out.print("| "+(matrix[i][j]=='\0' ? " ": matrix[i][j]+ " "));
            }
            System.out.println("|");

            for( int j=0; j < matrix[0].length * 4 + 1; j++){
                System.out.print('-');
            }
            System.out.println();
        }
        System.out.println();
    }
    private static String encode(String text, String key, int width, int height) {

        // пишем кодирующие матрицы
        char[][][] matrices = writeEncodeMatrices(text, width, height);

        return collectEncodedMatrices(matrices, key, height);
    }

    // сборка шифрованных матриц в строку
    private static String collectEncodedMatrices(char[][][] matrices, String key, int height) {
        StringBuilder dtx = new StringBuilder();

        // собираем символы из матрицы, используя ключ, как индексатор столбцов
        for (char[][] matrix : matrices) {
            for (int keyColumn = 0; keyColumn < key.length(); keyColumn++) {
                int columnIndex = Integer.parseInt(Character.toString(key.charAt(keyColumn))) - 1;
                for (int row = 0; row < height; row++) {
                    char value = matrix[row][columnIndex];
                    if (value != '\0'  && value !='*')
                        dtx.append(value);
                }
            }
        }
        return dtx.toString();
    }

    // дешифровка строки
    private static String decode(String text, String key, int width, int height) {
        char[][][] matrices = writeDecodeMatrices(text, key, width, height);

        return collectDecodedMatrices(matrices, key, width, height);
    }

    // сборка дешифрованных матриц в строку
    private static String collectDecodedMatrices(char[][][] matrices, String key, int width, int height) {
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
    private static char[][][] writeDecodeMatrices(String text, String key, int width, int height) {

        List<char[][]> matricesList = new ArrayList<>();

        int srcOffset = 0;
        int ofNull = width*height-text.length()%(width*height);


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
            for (int keyColumn = 0; keyColumn < key.length(); keyColumn++) {
                // парсим номер колонки из ключа
                int col = Integer.parseInt(Character.toString(key.charAt(keyColumn))) - 1;

                // пишем строку
                for (int row = 0; row < height; row++) {
                    int finalRow = row;
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
    private static char[][][] writeEncodeMatrices(String text, int width, int height) {

        List<char[][]> matricesList = new ArrayList<>();

        int srcOffset = 0;

        // бежим по всем символам и пишем их в матрицу
        // если строка кончилась - пишем \0

        while (srcOffset < text.length()) {
            char[][] chars = new char[height][width];

            for (int indexInMatrix = 0; indexInMatrix < width * height; indexInMatrix++) {
                int row = indexInMatrix / width;
                int col = indexInMatrix % width;

                if (srcOffset == text.length()) {
                    chars[row][col] = '\0';
                } else {
                    chars[row][col] = text.charAt(srcOffset);
                    srcOffset++;
                }

            }

            matricesList.add(chars);
        }

        return toArray(matricesList, width, height);
    }

}
