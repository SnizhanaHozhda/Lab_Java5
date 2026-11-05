import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

abstract class PrintedEdition implements Comparable<PrintedEdition> {
    private String name;
    private int year;
    private int pages;

    public static int num_r = 0;

    public PrintedEdition(String n, int y, int p) {
        this.name = n;
        this.year = y;
        this.pages = p;
        num_r++;
    }

    public PrintedEdition(String in_name) {
        this(in_name, 2020, 100);
    }

    public PrintedEdition() {
        this("Видання", 2000, 50);
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getPages() {
        return pages;
    }

    public abstract void Show();

    @Override
    public String toString() {
        return name + " " + year + " " + pages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PrintedEdition) {
            PrintedEdition other = (PrintedEdition) obj;
            return year == other.year && pages == other.pages && name.equals(other.name);
        }
        return false;
    }

    @Override
    public int compareTo(PrintedEdition o) {
        int r = name.compareToIgnoreCase(o.name);
        if (r != 0) return r;
        return Integer.compare(year, o.year);
    }
}

class Magazine extends PrintedEdition {
    private int number;

    public Magazine(String n, int y, int p, int num) {
        super(n, y, p);
        this.number = num;
    }

    public Magazine(String in_name) {
        super(in_name);
        this.number = 1;
    }

    public Magazine() {
        super();
        this.number = 1;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void Show() {
        System.out.println(this.toString());
    }

    @Override
    public int compareTo(PrintedEdition o) {
        if (o.getClass() == Magazine.class) {
            int r = Integer.compare(this.number, ((Magazine) o).number);
            if (r != 0) return r;
        }
        return super.compareTo(o);
    }

    @Override
    public String toString() {
        return "Magazine{} №" + number + " " + super.toString();
    }
}

class Book extends PrintedEdition {
    private String author;

    public Book(String n, int y, int p, String a) {
        super(n, y, p);
        this.author = a;
    }

    public Book(String in_name) {
        super(in_name);
        this.author = "Невідомий";
    }

    public Book() {
        super();
        this.author = "Невідомий";
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public void Show() {
        System.out.println(this.toString());
    }

    @Override
    public int compareTo(PrintedEdition o) {
        if (o.getClass() == Book.class) {
            int r = this.author.compareToIgnoreCase(((Book) o).author);
            if (r != 0) return r;
        }
        return super.compareTo(o);
    }

    @Override
    public String toString() {
        return "Book{} " + author + " " + super.toString();
    }
}

class Textbook extends Book {
    private String subject;

    public Textbook(String n, int y, int p, String a, String subj) {
        super(n, y, p, a);
        this.subject = subj;
    }

    public Textbook(String in_name) {
        super(in_name);
        this.subject = "Загальний";
    }

    public Textbook() {
        super();
        this.subject = "Загальний";
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public void Show() {
        System.out.println(this.toString());
    }

    @Override
    public int compareTo(PrintedEdition o) {
        if (o.getClass() == Textbook.class) {
            int r = this.subject.compareToIgnoreCase(((Textbook) o).subject);
            if (r != 0) return r;
        }
        return super.compareTo(o);
    }

    @Override
    public String toString() {
        return "Textbook{} [" + subject + "] " + super.toString();
    }
}

class PrintedEditionComparator implements Comparator<PrintedEdition> {
    @Override
    public int compare(PrintedEdition a, PrintedEdition b) {
        int r = Integer.compare(a.getYear(), b.getYear());
        if (r != 0) return r;
        r = Integer.compare(a.getPages(), b.getPages());
        if (r != 0) return r;
        return a.getName().compareToIgnoreCase(b.getName());
    }
}

public class task1 {

    static final String MAGAZINES_FILE = "magazines.txt";
    static final String BOOKS_FILE = "books.txt";
    static final String TEXTBOOKS_FILE = "textbooks.txt";
    static final String OUTPUT_FILE = "sorted.txt";

    static <T> ArrayList<T> readFile(String fileName, Function<String[], T> parser) {
        ArrayList<T> list = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            int lineNo = 0;
            for (String line : lines) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.charAt(0) == '\uFEFF') line = line.substring(1);
                try {
                    String[] parts = line.split(";");
                    for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
                    list.add(parser.apply(parts));
                } catch (RuntimeException e) {
                    System.out.println("Помилка у файлі " + fileName + ", рядок " + lineNo + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Не вдалося прочитати файл " + fileName + ": " + e.getMessage());
        }
        return list;
    }

    static <T extends PrintedEdition> void printList(String title, List<T> list) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) {
            System.out.println("(порожньо)");
            return;
        }
        for (PrintedEdition p : list) {
            p.Show();
        }
    }

    private static int readInt(Scanner in, String pidkazka) {
        while (true) {
            System.out.print(pidkazka);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть ціле число!");
            }
        }
    }

    static Magazine inputMagazine(Scanner in) {
        System.out.println("\nНовий журнал:");
        System.out.print("Введіть назву: ");
        String name = in.nextLine();
        int year = readInt(in, "Введіть рік: ");
        int pages = readInt(in, "Введіть кількість сторінок: ");
        int num = readInt(in, "Введіть номер журналу: ");
        return new Magazine(name, year, pages, num);
    }

    static Book inputBook(Scanner in) {
        System.out.println("\nНова книга:");
        System.out.print("Введіть назву: ");
        String name = in.nextLine();
        int year = readInt(in, "Введіть рік: ");
        int pages = readInt(in, "Введіть кількість сторінок: ");
        System.out.print("Введіть автора: ");
        String author = in.nextLine();
        return new Book(name, year, pages, author);
    }

    static Textbook inputTextbook(Scanner in) {
        System.out.println("\nНовий підручник:");
        System.out.print("Введіть назву: ");
        String name = in.nextLine();
        int year = readInt(in, "Введіть рік: ");
        int pages = readInt(in, "Введіть кількість сторінок: ");
        System.out.print("Введіть автора: ");
        String author = in.nextLine();
        System.out.print("Введіть предмет: ");
        String subj = in.nextLine();
        return new Textbook(name, year, pages, author, subj);
    }

    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            System.out.println("Помилка налаштування кодування виводу");
        }

        ArrayList<Magazine> magazines = readFile(MAGAZINES_FILE, p ->
                new Magazine(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3])));
        ArrayList<Book> books = readFile(BOOKS_FILE, p ->
                new Book(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]), p[3]));
        ArrayList<Textbook> textbooks = readFile(TEXTBOOKS_FILE, p ->
                new Textbook(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]), p[3], p[4]));

        System.out.println("===== Прочитані з файлів дані =====");
        printList("Журнали", magazines);
        printList("Книги", books);
        printList("Підручники", textbooks);

        Collections.sort(magazines);
        Collections.sort(books);
        Collections.sort(textbooks);

        System.out.println("\n===== Після сортування (Comparable) =====");
        printList("Журнали (за номером)", magazines);
        printList("Книги (за автором)", books);
        printList("Підручники (за предметом)", textbooks);

        Scanner in = new Scanner(System.in);
        try {
            magazines.add(inputMagazine(in));
            books.add(inputBook(in));
            textbooks.add(inputTextbook(in));
        } catch (Exception e) {
            System.out.println("Введення перервано.");
        }

        Collections.sort(magazines);
        Collections.sort(books);
        Collections.sort(textbooks);

        System.out.println("\n===== Після додавання нових записів і повторного сортування =====");
        printList("Журнали", magazines);
        printList("Книги", books);
        printList("Підручники", textbooks);

        ArrayList<PrintedEdition> all = new ArrayList<>();
        all.addAll(magazines);
        all.addAll(books);
        all.addAll(textbooks);

        Collections.sort(all, new PrintedEditionComparator());

        printList("Загальний список (за роком, сторінками, назвою)", all);

        ArrayList<String> out = new ArrayList<>();
        for (PrintedEdition p : all) {
            out.add(p.toString());
        }
        try {
            Files.write(Paths.get(OUTPUT_FILE), out, StandardCharsets.UTF_8);
            System.out.println("\nВідсортований список записано у файл " + OUTPUT_FILE);
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + OUTPUT_FILE + ": " + e.getMessage());
        }

        System.out.println("Кількість створених об'єктів = " + PrintedEdition.num_r);
    }
}