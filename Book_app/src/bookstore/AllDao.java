package bookstore;

import bookstore.model.*;
import bookstore.utils.DBConnUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllDao {
    private QueryRunner queryRunner = new QueryRunner();
    private final Connection connection = DBConnUtils.getConnection();

    public List<Publisher> queryAllPublishers() {
        List<Publisher> result = new ArrayList<>();
        try {
            result = queryRunner.query(connection, "SELECT pub_id, pub_name, bank_account, email, phone FROM publisher", new BeanListHandler<Publisher>(Publisher.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Warehouse> queryAllWarehouses() {
        List<Warehouse> result = new ArrayList<>();
        String sql = "SELECT warehouse_id warehouseId, warehouse_address warehouseAddress FROM warehouse";

        try {
            result = queryRunner.query(connection, sql, new BeanListHandler<>(Warehouse.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public UserInfo queryUserByUsernameAndPassword(String username, String password) {
        UserInfo userInfo = null;
        String sql = "SELECT user_id userid, username, \"password\", email, is_owner isOwner FROM userinfo where username=? and password=?";
        try {
            userInfo = queryRunner.query(connection, sql, new BeanHandler<>(UserInfo.class), username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userInfo;
    }

    public boolean insertBook(Book book) {
        boolean result = true;
        String sql = "INSERT INTO book\n" +
                "(isbn, title, number_of_pages, price, warehouse_id, pub_id)\n" +
                "VALUES(?, ?, ?, ?, ?, ?)";
        try {
            queryRunner.execute(connection, sql, book.getIsbn(), book.getTitle(), book.getNumberOfPages(), book.getPrice(), book.getWarehouseId(), book.getPubId());
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean deleteBook(String ISBN) {
        boolean result = true;
        String sql = "delete from Book where isbn=?";
        try {
            queryRunner.execute(connection, sql, ISBN);
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Book> findBooks(int searchType, String content) {
        List<Book> result = new ArrayList<>();
        String sql = "select isbn, title, number_of_pages numberOfPages, price, warehouse_id warehouseId, pub_id pubId from Book where 1=1 ";
        if (1 == searchType) {
            sql += "and title=?";
        } else if (2 == searchType) {
            sql += "and title=?";
        } else if (3 == searchType) {
            sql += "and isbn=?";
        } else if (4 == searchType) {
            sql += "and genre_id in (select genre_id from genre where genre_name=?)";
        }
        try {
            result = queryRunner.query(connection, sql, new BeanListHandler<>(Book.class),content);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public boolean insertUserInfo(UserInfo userInfo) {
        boolean result = true;
        String sql = "INSERT INTO userinfo VALUES(nextval('bookstore_id'), ?, ?, ?, 'N')";
        try {
            queryRunner.execute(connection, sql,userInfo.getUserName(),userInfo.getPassword(),userInfo.getEmail());
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }
        return result;
    }

    public Basket findBasket(Basket basket) {
        Basket result = null;
        String sql = "SELECT basket_id basketId, user_id userId FROM basket where user_id=? and status=1";
        try {
            result = queryRunner.query(connection, sql, new BeanHandler<>(Basket.class),basket.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int getStoreId() {
        int result = -1;
        String sql = "select nextval('bookstore_id')";
        try {
            result = queryRunner.query(connection, sql, new BeanHandler<>(Integer.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean insertBasket(Basket basket) {
        boolean result = true;
        String sql = "INSERT INTO basket VALUES(nextval('bookstore_id') , ? , 1)";
        try {
            queryRunner.execute(connection, sql,basket.getUserId());
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }

        return result;
    }

    public boolean updateBasketStatus(Basket basket) {
        boolean result = true;
        String sql = "update basket set status=0 where basket_id=?";
        try {
            queryRunner.execute(connection, sql,basket.getBasketId());
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }

        return result;
    }

    public boolean insertBasketBooks(BasketBooks basketBooks) {
        boolean result = true;
        String sql = "INSERT INTO Basket_books VALUES(? , ?)";
        try {
            queryRunner.execute(connection, sql,basketBooks.getBasketId(),basketBooks.getISBN());
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }

        return result;
    }

    public List<Book> findBooksByBasketid(int basketId) {
        List<Book> result = new ArrayList<>();
        String sql = "select isbn, title, number_of_pages numberOfPages, price, warehouse_id warehouseId, pub_id pubId " +
                "from Book where isbn in (select isbn from Basket_books where basket_id=?)";

        try {
            result = queryRunner.query(connection, sql, new BeanListHandler<>(Book.class),basketId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean insertOrder(OrderInfo orderInfo) {
        boolean result = true;
        String sql = "INSERT INTO OrderInfo VALUES(nextval('bookstore_id') ,? ,? ,? ,'1' ,? ,now() ,? )";
        try {
            queryRunner.execute(connection, sql,orderInfo.getUserId(),orderInfo.getBasketId(),orderInfo.getShipId(),orderInfo.getAddressId(),orderInfo.getPrice());
        } catch (SQLException e) {
            result = false;
            throw new RuntimeException(e);
        }

        return result;
    }


    public List<ShippingService> findShippingServices() {
        List<ShippingService> result = new ArrayList<>();
        String sql = "select ship_id shipId, company_name companyName from ShippingService";

        try {
            result = queryRunner.query(connection, sql, new BeanListHandler<>(ShippingService.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Address> findAddressesByUserId(Integer userId) {
        List<Address> result = new ArrayList<>();
        String sql = "select address_id addressId, full_address fullAddress,user_id userid from Address";

        try {
            result = queryRunner.query(connection, sql, new BeanListHandler<>(Address.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Map<String,Object> findOrderInfoByOrderId(Integer orderId) {
        Map<String,Object> result = null;
        String sql = "select " +
                "o.order_id orderId, " +
                "s.company_name companyName, " +
                "a.full_address fullAddress " +
                "from orderinfo o  " +
                "join shippingservice s " +
                "on o.ship_id =s.ship_id  " +
                "join address a " +
                "on o.address_id = a.address_id  " +
                "where o.order_id = ?";
        try {
            result = queryRunner.query(connection, sql, new MapHandler(),orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public List<Map<String,Object>> showReport() {
        List<Map<String,Object>> result = null;
        String sql = "select * from store_report";
        try {
            result = queryRunner.query(connection, sql, new MapListHandler());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
