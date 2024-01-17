package com.supermarket.DAL;

import com.supermarket.DTO.Function;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FunctionDAL extends Manager {
    public FunctionDAL() {
        super("function", List.of("id", "name", "deleted"));
    }

    public List<Function> convertToFunctions(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Function(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), // name
                    Boolean.parseBoolean(row.get(2))    // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in FunctionDAL.convertToFunctions(): " + e.getMessage());
            }
            return new Function();
        });
    }

    public int addFunction(Function function) {
        try {
            return create(function.getId(),
                function.getName(),
                false
            ); // khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in FunctionDAL.addFunction(): " + e.getMessage());
        }
        return 0;
    }

    public int updateFunction(Function function) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(function.getId());
            updateValues.add(function.getName());
            updateValues.add(function.isDeleted());
            return update(updateValues, "id = " + function.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in FunctionDAL.updateFunction(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteFunction(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in FunctionDAL.deleteFunction(): " + e.getMessage());
        }
        return 0;
    }

    public List<Function> searchFunctions(String... conditions) {
        try {
            return convertToFunctions(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in FunctionDAL.searchFunctions(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}


