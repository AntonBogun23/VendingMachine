package sample;

public class Product {

    private String name;
    private int cost;
    private String date;

    public Product(String name, int cost, String date) {
        this.name = name;
        this.cost = cost;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void displayProduct() {
        System.out.print(name + " - " + cost);
    }
}
