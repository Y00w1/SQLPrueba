package org.example.connection.v2;

import model.Product;
import repository.Repository;
import repository.impl.RepoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main2 {
    public static void main(String[] args) {
        try(Connection conn = ConnectionBD.getInstance() ){
            Repository<Product> repo = new RepoImpl();

            Product p = new Product();
            p.setName("Cilantro");
            p.setPrice(500.0);
            p.setDate(LocalDate.now());

            Product p2 = new Product();
            p2.setId(4);
            p2.setName("cilantro");
            p2.setPrice(600.0);

            listProducts(repo);
            Product prod = getProdById(repo, 2);
            System.out.println("ID: "+prod.getId()+" Name: "+prod.getName() + " Price: "+prod.getPrice()+ " Date registered: "+prod.getDate());
            addProduct(repo, p);
            listProducts(repo);
            updateProduct(repo, p2);
            listProducts(repo);
            deleteProduct(repo, 4);
            listProducts(repo);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void listProducts(Repository<Product> repo) {
        System.out.println("----List of products-----");
        repo.list().forEach(System.out::println);
    }

    private static Product getProdById(Repository<Product> repo, int id){
        System.out.println("-----Product by ID------");
        return repo.byId(id);
    }
    private static void addProduct(Repository<Product> repo, Product product){
        System.out.println("---------Add product -----------");
        repo.save(product);
    }
    private static void updateProduct(Repository<Product> repo, Product product){
        System.out.println("---------Update product -----------");
        repo.save(product);
    }
    private static void deleteProduct(Repository<Product> repo, int id){
        System.out.println("---------Delete product----------");
        repo.delete(id);
    }
}
