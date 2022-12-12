package bookstore;

import bookstore.model.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private Scanner input;
    private AllDao allDao;

    private int userId;

    private int basketId = -1;

    public Menu(Scanner input, AllDao allDao) {
        this.input=input;
        this.allDao=allDao;
    }

    public void menu() {
        System.out.println("--------Welcome to online bookstore---------");
        while (true){
            System.out.println("1.Register");
            System.out.println("2.Login");
            System.out.println("3.exit system");
            System.out.println("Enter[1-3]:");
            String choiceStr = input.next();
            try {
                int choice = Integer.parseInt(choiceStr);
                if(choice==1){
                    registerUser();
                }else if(choice==2){
                    loginUser();
                }else if(choice==3){
                    System.exit(1);
                }else{
                    System.out.println("The choice is not valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("The choice is not valid.");
            }

        }

    }

    private void loginUser() {
        System.out.println("Enter username:");
        String username = input.next();
        System.out.println("Enter password:");
        String password = input.next();
        String choiceStr;
        UserInfo userInfo = allDao.queryUserByUsernameAndPassword(username, password);
        if (userInfo == null) {
            System.out.println("The password is incorrect or the user does not exist!");
            System.exit(-1);
        }
        this.userId=userInfo.getUserId();
        if (userInfo.getIsOwner().equals("Y") ) {
            while (true) {
                System.out.println("1.add book");
                System.out.println("2.delete book");
                System.out.println("3.show report");
                System.out.println("4.exit system");
                System.out.println("Enter[1-4]:");
                choiceStr = input.next();
                try {
                    int choice = Integer.parseInt(choiceStr);
                    if(choice==1){
                        addBook();
                    }else if(choice==2){
                        delBook();
                    }else if(choice==3){
                        showReport();
                    }else if(choice==4){
                        System.exit(1);
                    }else{
                        System.out.println("The choice is not valid.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("The choice is not valid.");
                }
            }
        } else {
            while (true){
                System.out.println("1.search books");
                System.out.println("2.search orders");
                System.out.println("3.add to basket");
                System.out.println("4.checkout to order");
                System.out.println("5.exit system");
                System.out.println("Enter[1-5]:");
                choiceStr = input.next();
                try {
                    int choice = Integer.parseInt(choiceStr);
                    if(choice==1){
                        searchBook();
                    }else if(choice==2){
                        searchOrder();
                    }else if(choice==3){
                        addToBasket();
                    }else if(choice==4){
                        checkoutToOrder();
                    }else if(choice==5){
                        System.exit(1);
                    }else{
                        System.out.println("The choice is not valid.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("The choice is not valid.");
                }
            }
        }
    }

    private void checkoutToOrder() {

        List<Book> books = allDao.findBooksByBasketid(this.basketId);
        if(books.size() == 0){
            System.out.println("You have not add any book to basket.");
        }else{
            System.out.println("In the basket,you have choosed the books:");
            float price = 0;
            for(Book book:books) {
                System.out.println(book);
                price += book.getPrice();
            }
            System.out.println("The total price is:" + price);

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setBasketId(this.basketId);
            orderInfo.setUserId(this.userId);
            orderInfo.setPrice(price);

            System.out.println("Please choose shipping company:");
            List<ShippingService> shippingServices = allDao.findShippingServices();
            for(ShippingService shippingService:shippingServices){
                System.out.println(shippingService);
            }
            System.out.println("Enter:");
            String shipId = input.next();
            orderInfo.setShipId(Integer.parseInt(shipId));

            System.out.println("Please choose address:");
            List<Address> addresses = allDao.findAddressesByUserId(this.userId);
            for(Address address:addresses) {
                System.out.println(address);
            }
            System.out.println("Enter:");
            String addressId = input.next();
            orderInfo.setAddressId(Integer.parseInt(addressId));

            boolean result = allDao.insertOrder(orderInfo);
            if (result) {
                Basket basket = new Basket();
                basket.setBasketId(this.basketId);
                allDao.updateBasketStatus(basket);
                System.out.println("Checkout to order success!");
            } else {
                System.out.println("Checkout to order error!");
            }
        }
    }

    private void showReport() {
        System.out.println("The report:");
        List<Map<String,Object>> result = allDao.showReport();
        for(Map<String,Object> map:result){
            System.out.println("report_id="+map.get("report_id"));
            System.out.println("year="+map.get("year"));
            System.out.println("month="+map.get("month"));
            System.out.println("quantity="+map.get("quantity"));
            System.out.println("total_revenue="+map.get("total_revenue"));
            System.out.println("total_cost="+map.get("total_cost"));
            System.out.println("total_profit="+map.get("total_profit"));
            System.out.println("==========================================");
        }
    }

    private void searchOrder() {
        System.out.println("Enter order id:");
        String orderIdStr = this.input.next();
        try {
            int orderId = Integer.parseInt(orderIdStr);
            Map<String,Object> orderInfo = allDao.findOrderInfoByOrderId(orderId);
            if(orderInfo == null) {
                System.out.println("The order id is not in database.");
            }else{
                System.out.println("orderid:"+orderInfo.get("orderid"));
                System.out.println("companyname:"+orderInfo.get("companyname"));
                System.out.println("fulladdress:"+orderInfo.get("fulladdress"));
            }
        } catch (NumberFormatException e) {
            System.out.println("The order id is not valid.");
        }
    }

    private void addToBasket() {
        if(-1==basketId){
            Basket basket = new Basket();
            basket.setUserId(this.userId);
            allDao.insertBasket(basket);
            Basket newBasket = allDao.findBasket(basket);
            this.basketId = newBasket.getBasketId();
        }
        System.out.println("Enter ISBN:");
        String isbn = input.next();
        BasketBooks basketBooks = new BasketBooks();
        basketBooks.setBasketId(this.basketId);
        basketBooks.setISBN(isbn);
        boolean result = allDao.insertBasketBooks(basketBooks);
        if (result) {
            System.out.println("Add to basket success!");
        } else {
            System.out.println("Add to basket error!");
        }
    }

    private void registerUser() {
        System.out.println("Enter Username:");
        String username = input.next();
        System.out.println("Enter password:");
        String password = input.next();
        System.out.println("Enter email:");
        String email = input.next();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        userInfo.setPassword(password);
        userInfo.setEmail(email);
        boolean result = allDao.insertUserInfo(userInfo);
        if (result) {
            System.out.println("Register user success!");
        } else {
            System.out.println("Register user error!");
        }
    }

    private void addBook() {
        System.out.println("Enter ISBN:");
        String isbn = input.next();
        System.out.println("Enter title:");
        String title = input.next();

        Integer numberOfPages = null;
        while (true) {
            System.out.println("Enter number of pages:");
            String numberOfPagesStr = input.next();
            try {
                numberOfPages = Integer.parseInt(numberOfPagesStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("The input is not valid.");
            }
        }
        Float price = null;
        while (true) {
            System.out.println("Enter price:");
            String priceStr = input.next();
            try {
                price = new Float(priceStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("The input is not valid.");
            }
        }

        Book book = new Book(isbn, title, numberOfPages, price, 1, 1);
        boolean result = allDao.insertBook(book);
        if (result) {
            System.out.println("Add book success!");
        } else {
            System.out.println("Add book error!");
        }

    }

    private void delBook() {
        System.out.println("Enter the book ISBN:");
        String bookISBN = input.next();
        boolean result = allDao.deleteBook(bookISBN);
        if (result) {
            System.out.println("Delete book success!");
        } else {
            System.out.println("Delete book error!");
        }
    }

    private void searchBook() {

        while (true) {
            System.out.println("Search By:\n1.book name\n2.author name\n3.ISBN\n4.genre\nEnter[1-4]:");
            String choiceStr = input.next();
            try {
                int choice = Integer.parseInt(choiceStr);
                if(choice>=1 && choice <=4){
                    System.out.println("Enter the search content:");
                    String content = input.next();
                    List<Book> allBooks = allDao.findBooks(choice,content);
                    System.out.println("Search result:");
                    if(allBooks.size()==0){
                        System.out.println("Not found.");
                    }else{
                        for(Book book:allBooks){
                            System.out.println(book);
                        }
                    }

                    break;
                }else{
                    System.out.println("The choice is not valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("The choice is not valid.");
            }
        }
    }
}
