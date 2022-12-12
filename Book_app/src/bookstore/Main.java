package bookstore;

import bookstore.model.Book;
import bookstore.model.UserInfo;

import java.util.List;
import java.util.Scanner;

public class Main {
    private Integer basketId;
    public static void main(String[] args) {
        AllDao allDao = new AllDao();
        Scanner input = new Scanner(System.in);
        new Menu(input,allDao).menu();
    }



}
