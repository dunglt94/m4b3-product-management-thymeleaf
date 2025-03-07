package com.example.productmanagement.service;

import com.example.productmanagement.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements IProductService {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/products_management";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "123456";

    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String FIND_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String FIND_PRODUCT_BY_NAME = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(CONCAT('%',?,'%'))";
    private static final String INSERT_PRODUCTS = "INSERT INTO products(name, price, description, manufacturer, image) " +
                                                "VALUES(?,?,?,?,?)";
    private static final String UPDATE_PRODUCT = "update products set name = ?, price = ?, " +
                                                "description = ?, manufacturer = ?, image = ? where id = ?";
    private static final String DELETE_PRODUCTS = "DELETE FROM products WHERE id = ?";

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println(preparedStatement);
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setDescription(resultSet.getString("description"));
                product.setManufacturer(resultSet.getString("manufacturer"));
                product.setImage(resultSet.getString("image"));
                products.add(product);
            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product findById(int id) {
        Product product = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String description = resultSet.getString("description");
                    String manufacturer = resultSet.getString("manufacturer");
                    String image = resultSet.getString("image");
                    product = new Product(id, name, price, description, manufacturer, image);
                }
            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> findByName(String name) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCT_BY_NAME)) {
            preparedStatement.setString(1, name);
            System.out.println(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setDescription(resultSet.getString("description"));
                    product.setManufacturer(resultSet.getString("manufacturer"));
                    product.setImage(resultSet.getString("image"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void create(Product product) {
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCTS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getManufacturer());
            preparedStatement.setString(5, product.getImage());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getManufacturer());
            preparedStatement.setString(5, product.getImage());
            preparedStatement.setInt(6, product.getId());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCTS)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}
