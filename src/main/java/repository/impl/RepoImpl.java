package repository.impl;

import model.Product;
import org.example.connection.v2.ConnectionBD;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepoImpl implements Repository<Product> {

    private Connection getConnection() throws SQLException {
        return ConnectionBD.getInstance();
    }

    private Product createProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getDouble("price"));
        p.setDate(rs.getDate("regist_date").toLocalDate());
        return p;
    }

    @Override
    public List<Product> list() {
        List<Product> products = new ArrayList<>();
        try(Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery("select * from products;")
        ){
            while (rs.next()){
                Product p = createProduct(rs);
                products.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public Product byId(int id) {
        Product p = null;
        try(PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM products WHERE id = ?")){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = createProduct(rs);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    @Override
    public void save(Product p) {
        String query = (p.getId() != null && p.getId() > 0) ? "UPDATE products SET name=?, price=? WHERE id=?" : "INSERT INTO products (name, price, regist_date) VALUES (?,?,?)" ;
        try(PreparedStatement ps = getConnection()
                .prepareStatement(query)
        ){
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            if (p.getId() != null && p.getId() > 0){
                ps.setInt(3, p.getId());
            }else{
                ps.setDate(3, Date.valueOf(p.getDate()));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try(PreparedStatement ps = getConnection().prepareStatement("DELETE FROM products WHERE id=?")){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
