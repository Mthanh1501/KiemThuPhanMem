package com.supermarket.BLL;

import com.supermarket.DAL.CategoryDAL;
import com.supermarket.DTO.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryBLL extends Manager<Category> {
    private CategoryDAL categoryDAL;
    private List<Category> categoryList;

    public CategoryBLL() {
        categoryDAL = new CategoryDAL();
        categoryList = searchCategorys("deleted = 0");
    }

    public CategoryDAL getCategoryDAL() {
        return categoryDAL;
    }

    public void setCategoryDAL(CategoryDAL categoryDAL) {
        this.categoryDAL = categoryDAL;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Object[][] getData() {
        return getData(categoryList);
    }

    public boolean addCategory(Category category) {
        categoryList.add(category);
        return categoryDAL.addCategory(category) != 0;
    }

    public boolean updateCategory(Category category) {
        categoryList.set(getIndex(category, "id", categoryList), category);
        return categoryDAL.updateCategory(category) != 0;
    }

    public boolean deleteCategory(Category category) {
        categoryList.remove(getIndex(category, "id", categoryList));
        return categoryDAL.deleteCategory("id = " + category.getId()) != 0;
    }

    public List<Category> searchCategorys(String... conditions) {
        return categoryDAL.searchCategories(conditions);
    }

    public List<Category> findCategorys(String key, String value) {
        List<Category> list = new ArrayList<>();
        for (Category category : categoryList) {
            if (getValueByKey(category, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(category);
            }
        }
        return list;
    }

    public List<Category> findCategoriesBy(Map<String, Object> conditions) {
        List<Category> categorys = categoryList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            categorys = findObjectsBy(entry.getKey(), entry.getValue(), categorys);
        return categorys;
    }

    public boolean exists(Category category) {
        return !findCategoriesBy(Map.of(
            "id", category.getId(),
            "name", category.getName(),
            "quantity", category.getQuantity()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findCategoriesBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Category category, String key) {
        return switch (key) {
            case "id" -> category.getId();
            case "name" -> category.getName();
            case "quantity" -> category.getQuantity();

            default -> null;
        };
    }
}
