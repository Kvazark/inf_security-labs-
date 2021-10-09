package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class inf_sec3 {
    public static Scanner sc = new Scanner(System.in);
    public static List<Character> alphabet = Arrays.asList('а','б', 'в', 'г','д','е','ё','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я',' ','.',',','!');
    public static List<List<Character>> alphabetTable = Arrays.asList(
            Arrays.asList('ф','н','(','щ','и','г','е','r','а','д','ы','~','@','s','l','я','ж','^','с','ш','м','б','q','п','т','х','ю','ъ','р','}','|','_','#','u','h','?'),
            Arrays.asList('*','н','У','щ','d','+','е','r','=','д','ц','й','ч','[','b','ь',')','о','&','{','м','б','q','п','т','х','ю','Ъ','р','}','|','_','<','u','h','?'),
            Arrays.asList('л','н','(','щ','и',']','е','r','%','д','ы','~','@','g','/','я','э','з','"','Ш','м','б','q','п','т','х','ю','Ъ','р','}','|','_','w','u','Z','?'),
            Arrays.asList('Ф','н','У','щ','d','к','е','r','а','д','ц','й','ч','s','l','ь','ж','^','с','{','м','б','q','п','т','х','ю','Ъ','р','}','|','_','v','u','h','?'));

    public static void main(String[] args) {
        System.out.println("Таблица для зашиврованния сообщения: ");
        for (int i = 0; i < alphabetTable.size(); i++) {
            System.out.println(alphabetTable.get(i));
        }
        System.out.print("Введите текст для зашифровки: ");
        String text = sc.nextLine();

        text = encode(text);
        System.out.println(text);
        text = decode(text);
        System.out.println(text);
    }
    public static List<Character> shift(List<Character> list, int amount) {
        List<Character> encryption = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            encryption.add(list.get((i + amount) % list.size()));
        }
        return encryption;
    }

    public static String encode(String text) {
        List<Character> encryption = new ArrayList<>();
        List<List<Character>> keyColumn = new ArrayList<>();

        for (List<Character> alphabet: alphabetTable) {
            keyColumn.add(new ArrayList<>(alphabet));
        }

        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            List<Character> col = keyColumn.get(i % keyColumn.size());

            for (int j = 0; j < alphabet.size(); j++) {
                if (alphabet.get(j) == text.charAt(i)) {
                    index = j;
                    break;
                }
            }
            char firstSymbol = keyColumn.get(0).get(index);

            encryption.add(col.get((index) % col.size()));

            for (int j = 0; j < keyColumn.size(); j++) {
                if (j % keyColumn.size() == 0) {
                    firstSymbol = keyColumn.get(j).get(index);
                } else if (j % (keyColumn.size() - 1) == 0) {
                    keyColumn.get(j).set(index, firstSymbol);
                    continue;
                }
                keyColumn.get(j).set(index, keyColumn.get((j + 1) % keyColumn.size()).get(index));
            }
        }

        System.out.print("Зашифрованное сообщение: " );
        return encryption.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static String decode(String text) {
        List<Character> transcript = new ArrayList<>();
        List<List<Character>> keyColumn = new ArrayList<>();

        for (List<Character> alphabet: alphabetTable ) {
            keyColumn.add(new ArrayList<>(alphabet));
        }

        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            List<Character> col = keyColumn.get(i % keyColumn.size());

            for (int j = 0; j < col.size(); j++) {
                if (col.get(j) == text.charAt(i)) {
                    index = j;
                    break;
                }
            }
            char first = keyColumn.get(0).get(index);
            for (int j = 0; j < keyColumn.size(); j++) {
                if ( j % keyColumn.size() == 0) {
                    first = keyColumn.get(j).get(index);
                } else if (j % (keyColumn.size() - 1) == 0) {
                    keyColumn.get(j).set(index, first);
                    continue;
                }
                keyColumn.get(j).set(index, keyColumn.get((j + 1) % keyColumn.size()).get(index));
            }

            transcript.add(alphabet.get((index) % alphabet.size()));
        }

        System.out.print("Расшифрованное сообщение: ");
        return transcript.stream().map(String::valueOf).collect(Collectors.joining());
    }
}

