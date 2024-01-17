package com.supermarket.DAL;

import com.supermarket.DTO.Category;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAL extends Manager{
    public CategoryDAL() {
        super("category",
            List.of(
                "id",
                "name",
                "quantity",
                "deleted"
            ));
    }

    public List<Category> convertToCategories(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Category(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), // name
                    Integer.parseInt(row.get(2)), // quantity
                    Boolean.parseBoolean(row.get(3))    // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in CategoryDAL.convertToCategories(): " + e.getMessage());
            }
            return new Category();
        });
    }

    public int addCategory(Category category) {
        try {
            return create(category.getId(),
                category.getName(),
                category.getQuantity(),
                false
            ); // category khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CategoryDAL.addCategory(): " + e.getMessage());
        }
        return 0;
    }

    public int updateCategory(Category category) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(category.getId());
            updateValues.add(category.getName());
            updateValues.add(category.getQuantity());
            updateValues.add(category.isDeleted());
            return update(updateValues, "id = " + category.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CategoryDAL.updateCategory(): " + e.getMessage());
        }
        return 0;
    }


    public int deleteCategory(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CategoryDAL.deleteCategory(): " + e.getMessage());
        }
        return 0;
    }

    public List<Category> searchCategories(String... conditions) {
        try {
            return convertToCategories(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in CategoryDAL.searchCategories(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
