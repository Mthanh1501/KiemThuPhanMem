package com.supermarket.BLL;

import com.supermarket.DAL.BrandDAL;
import com.supermarket.DTO.Brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrandBLL extends Manager<Brand> {
    private BrandDAL brandDAL;
    private List<Brand> brandList;

    public BrandBLL() {
        brandDAL = new BrandDAL();
        brandList = searchBrands("deleted = 0");
    }

    public BrandDAL getBrandDAL() {
        return brandDAL;
    }

    public void setBrandDAL(BrandDAL brandDAL) {
        this.brandDAL = brandDAL;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Object[][] getData() {
        return getData(brandList);
    }

    public boolean addBrand(Brand brand) {
        brandList.add(brand);
        return brandDAL.addBrand(brand) != 0;
    }

    public boolean updateBrand(Brand brand) {
        brandList.set(getIndex(brand, "id", brandList), brand);
        return brandDAL.updateBrand(brand) != 0;
    }

    public boolean deleteBrand(Brand brand) {
        brandList.remove(getIndex(brand, "id", brandList));
        return brandDAL.deleteBrand("id = " + brand.getId()) != 0;
    }

    public List<Brand> searchBrands(String... conditions) {
        return brandDAL.searchBrands(conditions);
    }

    public List<Brand> findBrands(String key, String value) {
        List<Brand> list = new ArrayList<>();
        for (Brand brand : brandList) {
            if (getValueByKey(brand, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(brand);
            }
        }
        return list;
    }

    public List<Brand> findBrandsBy(Map<String, Object> conditions) {
        List<Brand> brands = brandList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            brands = findObjectsBy(entry.getKey(), entry.getValue(), brands);
        return brands;
    }

    public boolean exists(Brand brand) {
        return !findBrandsBy(Map.of(
            "id", brand.getId(),
            "name", brand.getName(),
            "supplier_id", brand.getSupplier_id()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findBrandsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Brand brand, String key) {
        return switch (key) {
            case "id" -> brand.getId();
            case "name" -> brand.getName();
            case "supplier_id" -> brand.getSupplier_id();
            default -> null;
        };
    }
}
