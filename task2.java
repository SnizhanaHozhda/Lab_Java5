import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class task2 {

    static double readDouble(Scanner in) {
        return Double.parseDouble(in.next().replace(',', '.'));
    }

    // Мінімізуємо S(R) = sum (U_i - R * I_i)^2.
    // dS/dR = 0  =>  R = sum(I_i * U_i) / sum(I_i^2)
    static double leastSquaresR(ArrayList<Double> I, ArrayList<Double> U) {
        double sumIU = 0;
        double sumII = 0;

        Iterator<Double> itI = I.iterator();
        Iterator<Double> itU = U.iterator();
        while (itI.hasNext() && itU.hasNext()) {
            double i = itI.next();
            double u = itU.next();
            sumIU += i * u;
            sumII += i * i;
        }

        if (sumII == 0) {
            throw new ArithmeticException("Усі значення струму дорівнюють нулю, R обчислити неможливо.");
        }
        return sumIU / sumII;
    }

    public static void main(String[] args) {

        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            System.out.println("Помилка налаштування кодування виводу");
        }

        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8.name());

        try {
            System.out.print("Введіть кількість вимірів n: ");
            int n = in.nextInt();
            if (n <= 0) {
                System.out.println("n має бути додатним.");
                return;
            }

            ArrayList<Double> I = new ArrayList<Double>();
            ArrayList<Double> U = new ArrayList<Double>();

            System.out.println("Введіть значення сили струму I (А):");
            for (int k = 1; k <= n; k++) {
                System.out.print("I[" + k + "] = ");
                I.add(readDouble(in));
            }

            System.out.println("Введіть значення напруги U (В):");
            for (int k = 1; k <= n; k++) {
                System.out.print("U[" + k + "] = ");
                U.add(readDouble(in));
            }

            System.out.println("\nI = " + I);
            System.out.println("U = " + U);

            double R = leastSquaresR(I, U);
            System.out.printf("%nНаближене значення опору R = %.4f Ом%n", R);

        } catch (NumberFormatException | java.util.InputMismatchException e) {
            System.out.println("Помилка: введіть коректні числа.");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Введення завершено достроково.");
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        } finally {
            in.close();
        }
    }
}