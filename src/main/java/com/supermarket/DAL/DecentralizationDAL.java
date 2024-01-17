package com.supermarket.DAL;
import com.supermarket.DTO.Decentralization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class DecentralizationDAL extends Manager{
    public DecentralizationDAL() {
        super( "decentralization",
            List.of("role_id",
                "module_id",
                "function_id"
            ));
    }
    public List<Decentralization> convertToDecentralizations(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Decentralization(
                    Integer.parseInt(row.get(0)), // role_id
                    Integer.parseInt(row.get(1)), // module_id
                    Integer.parseInt(row.get(2)) //function
                );
            } catch (Exception e) {
                System.out.println("Error occurred in DecentralizationDAL.convertToDecentralizations(): " + e.getMessage());
            }
            return new Decentralization();
        });
    }
    public int addDecentralization(Decentralization decentralization) {
        try {
            return create(
                decentralization.getRole_id(),
                decentralization.getModule_id(),
                decentralization.getFunction_id()
            );
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DecentralizationDAL.addDecentralization():" + e.getMessage());
        }
        return 0;
    }

    public int updateDecentralization(Decentralization decentralization) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(decentralization.getRole_id());
            updateValues.add(decentralization.getModule_id());
            updateValues.add(decentralization.getFunction_id());
            return update(updateValues,
                "role_id = " + decentralization.getRole_id(),
                "Module_id = " +decentralization.getModule_id(),
                "function_id =  " + decentralization.getFunction_id());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DecentralizationDAL.updateDecentralization() " + e.getMessage());
        }
        return 0;
    }

    public int deleteDecentralization(String... conditions) {
        try {
            return delete(conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DecentralizationDAL.deleteDecentralization() " + e.getMessage());
        }
        return 0;
    }

    public List<Decentralization> searchDecentralizations(String... conditions) {
        try {
            return convertToDecentralizations(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in DecentralizationDAL.searchDecentralizations(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
