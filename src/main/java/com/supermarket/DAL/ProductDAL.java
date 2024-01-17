package com.supermarket.DAL;

import com.supermarket.DTO.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAL extends Manager{

    public ProductDAL() {
        super("product",
            List.of("id",
                "name",
                "brand_id",
                "category_id",
                "unit",
                "cost",
                "quantity",
                "image",
                "barcode",
                "deleted"));
    }
    public List<Product> convertToProducts(List<List<String>> data){
        return convert(data, row -> {
            try {
                return new Product(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), // name
                    Integer.parseInt(row.get(2)), // brand_id
                    Integer.parseInt(row.get(3)), // category_id
                    row.get(4), // unit
                    Double.parseDouble(row.get(5)), //cost
                    Double.parseDouble(row.get(6)),    // quantity
                    row.get(7),//image
                    row.get(8), //barcode
                    Boolean.parseBoolean(row.get(9)) //deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in ProductDAL.convertToProducts(): " + e.getMessage());
            }
            return new Product();
        });
    }

    public int addProduct(Product product) {
        try {
            return create(product.getId(),
                product.getName(),
                product.getBrand_id(),
                product.getCategory_id(),
                product.getUnit(),
                product.getCost(),
                product.getQuantity(),
                product.getImage(),
                product.getBarcode(),
                false
            ); // product khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ProductDAL.addProduct(): " + e.getMessage());
        }
        return 0;
    }
    public int updateProduct(Product product) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(product.getId());
            updateValues.add(product.getName());
            updateValues.add(product.getBrand_id());
            updateValues.add(product.getCategory_id());
            updateValues.add(product.getUnit());
            updateValues.add(product.getCost());
            updateValues.add(product.getQuantity());
            updateValues.add(product.getImage());
            updateValues.add(product.getBarcode());
            updateValues.add(product.isDeleted());
            return update(updateValues, "id = " + product.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in Product.updateProduct(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteProduct(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ProductDAL.deleteProduct(): " + e.getMessage());
        }
        return 0;
    }

    public List<Product> searchProducts(String... conditions) {
        try {
            return convertToProducts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in ProductDAL.searchProducts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
