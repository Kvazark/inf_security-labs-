package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class inf_sec4 {
    public static Scanner sc = new Scanner(System.in);
    public static List<Character> alphabet = Arrays.asList('а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', ' ', '.', ',');
    public static List<List<Character>> alphabetTable = Arrays.asList(
            Arrays.asList('а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '/', '.', ','),
            Arrays.asList('с', 'к', 'у', 'п', 'н', 'ш', 'ь', 'x', 'ч', 'щ', 'р', 'ы', 'я', 'ъ', 'т', '/', 'ц', 'э', ',', 'ю', 'в', 'ё', 'ж', 'й', '.', 'е', 'г', 'м', 'б', 'о', 'л', 'ф', 'и', 'з', 'д', 'а'),
            Arrays.asList('н', 'р', 'т', 'ф', 'л', 'с', 'ч', 'у', 'я', 'о', '/', 'ц', 'ь', 'п', 'щ', 'ю', 'ы', 'x', 'ш', ',', 'б', 'з', 'ъ', 'э', 'д', 'ё', 'и', 'м', '.', 'е', 'й', 'г', 'а', 'ж', 'к', 'в'));

    public static void main(String[] args) {

        System.out.print("Введите текст для зашифровки: ");
        String text = sc.nextLine();
        System.out.print("Введите ключ-слово: ");
        String key = sc.nextLine();

        text = encode(text, key);
        System.out.println(text);
        text = decode(text, key);
        System.out.println(text);
    }

    public static List<Character> shift(List<Character> list, int amount) {
        List<Character> encryption = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            encryption.add(list.get((i + amount) % list.size()));
        }
        return encryption;
    }

    public static String encode(String text, String key) {
        List<Character> encryption = new ArrayList<>();
        String[] textArray = text.split(" ");

        for (int i = 0; i < textArray.length; i++) {
            List<List<Character>> keyRows = new ArrayList<>();
            List<Character> alphabet = alphabetTable.get(i % alphabetTable.size());
            for (int j = 0; j < key.length(); j++) {
                for (int e = 0; e < alphabet.size(); e++) {
                    if (key.charAt(j) == alphabet.get(e)) {
                        keyRows.add(shift(alphabet, e));
                    }
                }
            }
            for (int j = 0; j < textArray[i].length(); j++) {
                int index = 0;
                List<Character> row = keyRows.get(j % keyRows.size());

                for (int e = 0; e < alphabet.size(); e++) {
                    if (textArray[i].charAt(j) == alphabet.get(e)) {
                        index = e;
                        break;
                    }
                }
                encryption.add(row.get((index) % row.size()));
            }
            encryption.add(' ');
        }
        System.out.print("Зашифрованное сообщение: ");
        return encryption.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static String decode(String text, String key) {
        List<Character> transcript = new ArrayList<>();
        String[] textArray = text.split(" ");

        for (int i = 0; i < textArray.length; i++) {
            List<List<Character>> keyRows = new ArrayList<>();
            List<Character> alphabet = alphabetTable.get(i%alphabetTable.size());
            for (int j = 0; j < key.length(); j++) {
                for (int e = 0; e < alphabet.size(); e++) {
                    if (key.charAt(j) == alphabet.get(e)) {
                        keyRows.add(shift(alphabet, e));
                    }
                }
            }


            for (int j = 0; j < textArray[i].length(); j++) {
                int index = 0;
                List<Character> row = keyRows.get(j % keyRows.size());

                for (int e = 0; e < row.size(); e++) {
                    if (textArray[i].charAt(j) == row.get(e)) {
                        index = e;
                        break;
                    }
                }
                transcript.add(alphabet.get((index) % alphabet.size()));
            }
            transcript.add(' ');
        }
        System.out.print("Расшифрованное сообщение: ");
        return transcript.stream().map(String::valueOf).collect(Collectors.joining());
    }
}

