package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class inf_sec2 {
    public static Scanner sc = new Scanner(System.in);
    public static List<Character> alphabet = Arrays.asList('а','б', 'в', 'г','д','е','ё','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я',' ','.',',','А','Б', 'В', 'Г','Д','Е','Ё','Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ц','Ч','Ш','Щ','Ъ','Ы','Ь','Э','Ю','Я');

    public static void main(String[] args) {
        System.out.print("Введите текст для зашифровки: ");
        String text = sc.nextLine();
        System.out.print("Введите ключ-слова: ");
        String key=sc.nextLine();

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
        List<List<Character>> keyRow = new ArrayList<>();

        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                if (key.charAt(i) == alphabet.get(j)) {
                    keyRow.add(shift(alphabet, j));
                }
            }
        }

        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            List<Character> col = keyRow.get(i % keyRow.size());

            for (int j = 0; j < alphabet.size(); j++) {
                if (alphabet.get(j) == text.charAt(i)) {
                    index = j;
                    break;
                }
            }

            encryption.add(col.get((index) % col.size()));
        }

        System.out.print("Зашифрованное сообщение: ");
        return encryption.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static String decode(String text, String key) {
        List<Character> transcript = new ArrayList<>();
        List<List<Character>> keyRows = new ArrayList<>();

        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < alphabet.size(); j++) {
                if (key.charAt(i) == alphabet.get(j)) {
                    keyRows.add(shift(alphabet, j));
                }
            }
        }

        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            List<Character> col = keyRows.get(i % keyRows.size());

            for (int j = 0; j < col.size(); j++) {
                if (col.get(j) == text.charAt(i)) {
                    index = j;
                    break;
                }
            }

            transcript.add(alphabet.get((index) % alphabet.size()));
        }

        System.out.print("Расшифрованное сообщение: ");
        return transcript.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
