package sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ControllerForProducts {

        public static final int LINES = 9, COLUMNS = 10, SINGLE_LINE = 0;

        public String purchaseHistory = "",  statPrice = "", statValid = "";
        private Product[][] products;
        private int[] statisticPrice = new int[LINES];
        private int insertCount = 0;

        public ControllerForProducts() {
                products = new Product[LINES][COLUMNS];
        }

        /*
        Заполнение лотков товарами
        Происходит случайная генерация количества товаров в одном из лотков
        Также случайно генерируется дата окончания срока годности товара, которая составляет + 1-7 дней от текущей даты
        */
        public void insert(String name, int cost) {
                int productsNumMin = 5, productsNumMax = 10, dateNumMin = 1, dateNumMax = 7;
                int[] dateNum = new int[COLUMNS];

                int randomNum = productsNumMin + (int) (Math.random() * productsNumMax);
                createArrDate(dateNum, dateNumMin, dateNumMax);

                for (int j = 0; j < COLUMNS; j++) {
                        if (randomNum != 0) {
                                String date = createDate(dateNum[j]);
                                products[insertCount][j] = new Product(name, cost, date); randomNum--;
                        } else products[insertCount][j] = null;
                }
                insertCount++;
                remainingProducts();
        }

        /*
        Генерация даты окончания срока годности
         */
        private void createArrDate(int[] dateNum, int c, int d) {
                for (int i = 0; i < dateNum.length; i++)
                        dateNum[i] = c + (int) (Math.random() * d);
                Arrays.sort(dateNum);
        }

        /*
        Генерация даты окончания срока годности, + 1-7 дней от текущей даты
         */
        private static String createDate(int num) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Calendar c = Calendar.getInstance();
                try {
                        c.setTime(dateFormat.parse(today));
                } catch (ParseException e) {
                        e.printStackTrace();
                }
                c.add(Calendar.DATE, num);
                return dateFormat.format(c.getTime());
        }

        /*
        Вывод ячеек с товарами
         */
        public void display() {
                int checkNum = 1;
                char letter = 'A';
                for (int i = 0; i < LINES; i++) {
                        int quantity = 0;
                        for (int j = 0; j < COLUMNS; j++) { if (products[i][j] != null) quantity++; }
                        if (products[i][SINGLE_LINE] != null) {
                                System.out.print(letter + "" + checkNum + " - ");
                                products[i][SINGLE_LINE].displayProduct(); System.out.println(" - " + quantity);
                                if (checkNum == 3) { checkNum = 0; letter++; System.out.println();}
                        }
                        checkNum++;
                }
        }

        /*
        Реализация покупки товара
        Присутствуют проверки на наличие товара, срок годности, цену
        Присутствуют методы для ведения отчетности
         */
        public void purchase(int cellNumber, int amount, String date, String time) {
                if (products[cellNumber][SINGLE_LINE] != null) {
                        String[] checkDate = products[cellNumber][SINGLE_LINE].getDate().split("-"),
                                today = date.split("-");
                        int productDate = Integer.parseInt(checkDate[2]), cost = products[cellNumber][0].getCost(),
                                Today = Integer.parseInt(today[2]);

                        if (productDate < Today)
                                System.out.println("У товара просрочена дата, выберите другой товар\n");
                        else if (cost > amount) {
                                System.out.println("Суммы недостаточно для покупки, не хватает " + (cost - amount) + " рублей, повторите ввод\n");
                        } else {
                                historyPurchase(date, time, cellNumber);
                                purchaseStatistic(cellNumber, cost);
                                remainingProducts();
                                products[cellNumber][SINGLE_LINE] = null;
                                for (int j = 1; j < COLUMNS; j++) {
                                        if (products[cellNumber][j - 1] == null) {
                                                products[cellNumber][j - 1] = products[cellNumber][j];
                                                products[cellNumber][j] = null;
                                        }
                                }
                                System.out.println("Товар куплен успешно\n");
                        }
                } else {
                        System.out.println("Товар кончился, выберите другой\n");
                }

        }

        /*
        Статистику покупок, сгруппированная по товарам, начиная с наибольшей суммарной прибыли
         */
        private void purchaseStatistic(int cellNumber, int cost) {
                statPrice = "";
                String[][] statistic = new String[LINES][2];
                statisticPrice[cellNumber] += cost;

                for (int i = statisticPrice.length - 1; i >= 0; i--) {
                        statistic[i][0] = String.valueOf(statisticPrice[i]);
                        statistic[i][1] = String.valueOf(i);
                }

                Arrays.sort(statistic, new Comparator<String[]>() {
                        @Override
                        public int compare(String[] o1, String[] o2) {
                                return o1[SINGLE_LINE].compareTo(o2[SINGLE_LINE]);
                        }
                });

                for (int i = 0; i < LINES; i++) {
                        if (Integer.parseInt(statistic[i][0]) != 0) {
                                int cell = Integer.parseInt(statistic[i][1]);
                                statPrice = products[cell][SINGLE_LINE].getName() + " - " + statistic[i][SINGLE_LINE]
                                        + "\n" + statPrice;
                        }
                }
        }

        /*
        Статистику оставшихся в автомате товаров, начиная с ближайшей даты истечения срока годности
         */
        private void remainingProducts() {
                statValid = "";
                for (int i = LINES - 1; i >= 0; i--) {
                        for (int j = COLUMNS - 1; j >= 0; j--) {
                                if (products[i][j] != null) {
                                        statValid = products[i][j].getName() + " - " + products[i][j].getDate()
                                                + "\n" + statValid;
                                }
                        }
                        if (i != 0) statValid = "\n" + statValid;
                }
        }

        /*
        История покупок, начиная с последней совершенной покупки
         */
        private void historyPurchase(String date, String time, int cellNumber) {
                purchaseHistory = date + " " + time + " - "
                        + products[cellNumber][SINGLE_LINE].getName() + " - "
                        + products[cellNumber][SINGLE_LINE].getCost() + "\n" + purchaseHistory;
        }
}