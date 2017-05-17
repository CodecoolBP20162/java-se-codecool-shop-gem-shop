package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DatabaseConnectionData;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eszti on 2017.05.16..
 */


public class ProductCategoryDaoImplJdbc extends JdbcDao implements ProductCategoryDao {

    @Override
    public void add(ProductCategory category) {
        try {
            String query = "INSERT INTO Categories " +
                    "(category_name, category_department, category_description)" +
                    " VALUES(?,?);";
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            executeQuery(stmt.toString());
        }
        catch (SQLException e) {
            System.out.println("Category could not be added to the database.");
        }

    }

    @Override
    public ProductCategory find(int id)  {
        String query = "SELECT * FROM categories WHERE id = ?;";

        try {
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()){
                ProductCategory category = new ProductCategory(
                        resultSet.getString("category_name"),
                        resultSet.getString("category_department"),
                        resultSet.getString("category_description"));
                category.setId(resultSet.getInt("id"));
                return category;
            }
            return null;
        }
        catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void remove(int id) {

        try {
            String query = "DELETE FROM categories WHERE id = ?";

            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            executeQuery(stmt.toString());}
        catch (SQLException e) {
            System.out.println("Could not remove category from database.");
        }

    }

    @Override
    public List<ProductCategory> getAll() {

        try {
            List<ProductCategory> results = new ArrayList<>();
            String query = "SELECT * FROM categories;";

            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int dbId = resultSet.getInt("id");
                ProductCategory category = new ProductCategory(
                        resultSet.getString("category_name"),
                        resultSet.getString("category_department"),
                        resultSet.getString("category_description"));
                category.setId(dbId);
                results.add(category);
            }
            return results;
        }
        catch (SQLException e) {
            return null;
        }
    }

    @Override
    Connection getConnection() throws SQLException {
        DatabaseConnectionData.setupUserAndPasswordFromFile("connection.properties");
        return DriverManager.getConnection(
                DatabaseConnectionData.getDb(),
                DatabaseConnectionData.getDbUser(),
                DatabaseConnectionData.getDbPassword());
    }
}

