package com.supermarket.DAL;

import com.supermarket.DTO.Brand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandDAL extends Manager{
    public BrandDAL() {
        super("brand",
            List.of(
                "id",
                "name",
                "supplier_id",
                "deleted"
            ));
    }

    public List<Brand> convertToBrands(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Brand(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), //name
                    Integer.parseInt(row.get(2)), //supplier_id
                    Boolean.parseBoolean(row.get(3)) //deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in BrandDAL.convertToBrands(): " + e.getMessage());
            }
            return new Brand();
        });
    }

    public int addBrand(Brand brand) {
        try {
            return create(
                brand.getId(),
                brand.getName(),
                brand.getSupplier_id(),
                false
            ); // brand khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BrandDAL.addBrand(): " + e.getMessage());
        }
        return 0;
    }

    public int updateBrand(Brand brand) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(brand.getId());
            updateValues.add(brand.getName());
            updateValues.add(brand.getSupplier_id());
            updateValues.add(brand.isDeleted());
            return update(updateValues, "id = " + brand.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BrandDAL.updateBrand(): " + e.getMessage());
        }
        return 0;
    }


    public int deleteBrand(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BrandDAL.deleteBrand(): " + e.getMessage());
        }
        return 0;
    }

    public List<Brand> searchBrands(String... conditions) {
        try {
            return convertToBrands(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in BrandDAL.searchBrand(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
