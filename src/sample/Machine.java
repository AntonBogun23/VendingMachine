package sample;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class Machine {

    private static Scanner scanner = new Scanner(System.in);
    private static ControllerForProducts products;
    private static String dateAndTimeToday = new SimpleDateFormat("yyyy-MM-dd_HH.mm").format(new Date());

    /*
    Реализация меню
     */
    public static void MachineMenu() throws IOException {

        char choice, ignore;

        createProduct();

        for (;;) {
            do {
                System.out.println("Меню:");
                System.out.println("1. Вывод меню");
                System.out.println("2. Купить товар");
                System.out.println("3. Записать в файл историю покупок");
                System.out.println("4. Записать в файл статистику покупок");
                System.out.println("5. Записать в файл статистику оставшихся товаров");
                System.out.print("Выберите оператор (q - выход): ");

                choice = (char) System.in.read();

                do {
                    ignore = (char) System.in.read();
                } while (ignore != '\n');
            } while (choice < '1' | choice > '5' & choice != 'q');

            if (choice == 'q') { scanner.close(); break; }

            switch (choice) {
                case '1':
                    System.out.println("\nВывод меню:\n");
                    products.display();
                    break;
                case '2':
                    System.out.println("\nПокупка товара\n");
                    buyProduct();
                    break;
                case '3':
                    System.out.println("\nЗаписать в файл историю покупок");
                    if (products.purchaseHistory != "")
                        recordingPurchaseHistory(products.purchaseHistory);
                    else
                        System.out.println("Совершите покупку, чтобы отслеживать историю\n");
                    break;
                case '4':
                    System.out.println("\nЗаписать в файл статистику покупок");
                    if (products.statPrice != "")
                        recordingPurchaseStatistics(products.statPrice);
                    else
                        System.out.println("Совершите покупку, чтобы отслеживать статистику\n");
                    break;
                case '5':
                    System.out.println("\nЗаписать в файл статистику оставшихся товаров");
                    recordingRemainingProducts(products.statValid);
                    break;
                default:
                    System.out.println("\nНеверный ввод\n");
                    break;
            }
        }
    }

    /*
    Запись в файл статистики покупок
     */
    private static void recordingPurchaseStatistics(String statPrice) {
        String fileName = "src\\reports\\purchaseStatistics\\stat_price_" + dateAndTimeToday + ".log";

        try {
            File file = new File(fileName);

            if (!file.exists())
                file.createNewFile();

            PrintWriter pw = new PrintWriter(file);
            pw.println(statPrice);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Запись выполнена\n");
    }

    /*
    Запись в файл статистики оставшихся товаров
     */
    private static void recordingRemainingProducts(String statValid) {
        String fileName = "src\\reports\\remainingProducts\\stat_valid_" + dateAndTimeToday + ".log";

        try {
            File file = new File(fileName);

            if (!file.exists())
                file.createNewFile();

            PrintWriter pw = new PrintWriter(file);
            pw.println(statValid);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Запись выполнена\n");
    }

    /*
    Запись в файл истории покупок
     */
    private static void recordingPurchaseHistory(String purchaseHistory) {
        String fileName = "src\\reports\\purchaseHistory\\history_" + dateAndTimeToday + ".log";

        try {
            File file = new File(fileName);

            if (!file.exists())
                file.createNewFile();

            PrintWriter pw = new PrintWriter(file);
            pw.println(purchaseHistory);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Запись выполнена\n");
    }

    /*
    Заполнение ячеек товарами
     */
    private static void createProduct() {
        products = new ControllerForProducts();

        products.insert("Twix", 40);
        products.insert("Snickers", 45);
        products.insert("M&M's", 45);
        products.insert("Nuts", 40);
        products.insert("Coca-cola", 40);
        products.insert("Fanta", 30);
        products.insert("Sprite", 35);
        products.insert("Lay's (сыр)", 60);
        products.insert("Lay's (бекон)", 55);
    }

    /*
    Реализация покупки товара
     */
    private static void buyProduct() {
        int amount, cellNumber;

        String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("HH:mm").format(new Date());

        System.out.println("Для совершения покупки введите следующие данные");
        System.out.print("Номер ячейки (A1-C3): ");
        cellNumber = scanCellNumber();
        System.out.println("Сумму:");
        amount = scanAmount();
        System.out.println();

        products.purchase(cellNumber, amount, dateToday, time);
    }

    /*
    Проверка на верный ввод ячейки с A1 по C3
     */
    private static int scanCellNumber() {
        String  cellNumber;
        String checkLetter = "A1|A2|A3|B1|B2|B3|C1|C2|C3";
        String[] alphabet = {"A", "B", "C"};
        while (!scanner.hasNext(checkLetter)) {
            System.out.print("Неверный ввод, укажите ячейку (A1-C3): ");
            scanner.nextLine();
        }
        cellNumber = scanner.next();
        String cellNum = cellNumber.replaceAll("[ABC]", "");
        String cellLetter = cellNumber.replaceAll("[123]", "");
        int newNum = Integer.parseInt(cellNum);
        for (int i = 0; i < alphabet.length; i++)
            if (cellLetter.equals(alphabet[i]))
                newNum += i * 3 - 1;
        return newNum;
    }

    /*
    Проверка на верный ввод положительного числа в интервале 1-100
     */
    private static int scanAmount() {
        int amount;
        do {
            System.out.print("Введите положительное число в интервале 1-100: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Указано не число, повторите ввод: ");
                scanner.next();
            }
            amount = scanner.nextInt();
        } while (amount <= 0 | amount > 100);
        return amount;
    }
}
