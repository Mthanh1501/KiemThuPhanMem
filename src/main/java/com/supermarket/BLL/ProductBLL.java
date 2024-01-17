package com.supermarket.BLL;

import com.supermarket.DAL.ProductDAL;
import com.supermarket.DTO.Product;
import com.supermarket.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductBLL extends Manager<Product> {

    private ProductDAL productDAL;
    private List<Product> productList;

    public ProductBLL() {
        productDAL = new ProductDAL();
        productList = searchProducts("deleted = 0");
    }

    public ProductDAL getProductDAL() {
        return productDAL;
    }

    public void setProductDAL(ProductDAL productDAL) {
        this.productDAL = productDAL;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Object[][] getData() {
        return getData(productList);
    }

    public Pair<Boolean, String> addProduct(Product product) {
        Pair<Boolean, String> result;

        result = validateName(product.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateCost(String.valueOf(product.getCost()));
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateBarcode(product.getBarcode());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        result = exists(product);
        if (result.getKey()) {
            return new Pair<>(false,result.getValue());
        }

        productList.add(product);
        productDAL.addProduct(product);
        return new Pair<>(true,"");
    }

    public Pair<Boolean, String> updateProduct(Product product) {
        Pair<Boolean, String> result;

        result = validateName(product.getName());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateCost(String.valueOf(product.getCost()));
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validateBarcode(product.getBarcode());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }
        productList.set(getIndex(product, "id", productList), product);
        productDAL.updateProduct(product);
        return new Pair<>(true,"");

    }

    public boolean deleteProduct(Product product) {
        productList.remove(getIndex(product, "id", productList));
        return productDAL.deleteProduct("id = " + product.getId()) != 0;
    }

    public List<Product> searchProducts(String... conditions) {
        return productDAL.searchProducts(conditions);
    }

    public List<Product> findProducts(String key, String value) {
        List<Product> list = new ArrayList<>();
        for (Product product : productList) {
            if (getValueByKey(product, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(product);
            }
        }
        return list;
    }

    public List<Product> findProductsBy(Map<String, Object> conditions) {
        List<Product> products = productList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            products = findObjectsBy(entry.getKey(), entry.getValue(), products);
        return products;
    }

    public Pair<Boolean, String> exists(Product newProduct){
        List<Product> products = productDAL.searchProducts("phone = '" + newProduct.getId() + "'", "deleted = 0");
        if(!products.isEmpty()){
            return new Pair<>(true, "Sản phẩm đã tồn tại.");
        }
        products = productDAL.searchProducts("email = '" + newProduct.getName()+ "'", "deleted = 0");
        if(!products.isEmpty()){
            return new Pair<>(true, "Tên sản phẩm đã tồn tại.");
        }
        return new Pair<>(false, "");
    }


    private static Pair<Boolean, String> validateName(String name) {
        if (name.isBlank())
            return new Pair<>(false, "Tên sản phẩm không được để trống.");
        if (VNString.containsSpecial(name))
            return new Pair<>(false, "Tên sản phẩm không được chứa ký tự đặc biệt.");
        return new Pair<>(true, name);
    }

    private static Pair<Boolean, String> validateCost(String cost) {
        if (cost.isBlank())
            return new Pair<>(false, "Giá bán của sản phẩm không được để trống.");
        if (!VNString.checkUnsignedNumber(cost))
            return new Pair<>(false,"Giá bán của sản phẩm phải lớn hơn 0.");
        return new Pair<>(true,cost);
    }

    private static Pair<Boolean, String> validateBarcode(String barcode) {
        if (barcode.isBlank())
            return new Pair<>(false, "Mã vạch sản phẩm không được để trống.");
        if (VNString.containsSpecial(barcode))
            return new Pair<>(false, "Mã vạch sản phẩm không được chứa ký tự đặc biệt.");
        if(VNString.containsUnicode(barcode))
            return new Pair<>(false, "Mã vạch không được chứa ký tự không hỗ trợ.");
        return new Pair<>(true, barcode);
    }


    @Override
    public Object getValueByKey(Product product, String key) {
        return switch (key) {
            case "id" -> product.getId();
            case "name" -> product.getName();
            case "brand_id" -> product.getBrand_id();
            case "category_id" -> product.getCategory_id();
            case "unit" -> product.getUnit();
            case "cost" -> product.getCost();
            case "quantity" -> product.getQuantity();
            case "image" -> product.getImage();
            case "barcode" -> product.getBarcode();
            default -> null;
        };
    }
}
