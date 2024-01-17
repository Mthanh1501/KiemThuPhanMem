package com.supermarket.BLL;
import com.supermarket.DAL.DecentralizationDAL;
import com.supermarket.DTO.Decentralization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class DecentralizationBLL extends Manager<Decentralization> {
    private DecentralizationDAL decentralizationDAL;
    private List<Decentralization> decentralizationList;

    public DecentralizationBLL() {
        decentralizationDAL = new DecentralizationDAL();
        decentralizationList = searchDecentralizations();
    }
    public DecentralizationDAL getDecentralizationDAL() {
        return decentralizationDAL;
    }

    public void setDecentralizationDAL(DecentralizationDAL decentralizationDAL) {
        this.decentralizationDAL = decentralizationDAL;
    }

    public List<Decentralization> getDecentralizationList() {
        return decentralizationList;
    }

    public void setDecentralizationList(List<Decentralization> decentralizationList) {
        this.decentralizationList = decentralizationList;
    }
    public Object[][] getData() {
        return getData(decentralizationList);
    }

    public boolean addDecentralization(Decentralization decentralization) {
        decentralizationList.add(decentralization);
        return decentralizationDAL.addDecentralization(decentralization) != 0;
    }

    public boolean updateDecentralization(Decentralization decentralization) {
        decentralizationList.set(getIndex(decentralization, List.of("role_id","module_id","function_id"), decentralizationList), decentralization);
        return decentralizationDAL.updateDecentralization(decentralization) != 0;
    }

    public boolean deleteDecentralization(Decentralization decentralization) {
        decentralizationList.remove(getIndex(decentralization, List.of("role_id", "module_id", "function_id"), decentralizationList));
        return decentralizationDAL.deleteDecentralization(
            "role_id = " + decentralization.getRole_id(),
            "module_id = " + decentralization.getModule_id(),
            "function_id = " + decentralization.getFunction_id()) != 0;
    }

    public List<Decentralization> searchDecentralizations(String... conditions) {
        return decentralizationDAL.searchDecentralizations(conditions);
    }

    public List<Decentralization> findDecemtralizations(String key, String value) {
        List<Decentralization> list = new ArrayList<>();
        for (Decentralization decentralization : decentralizationList) {
            if (getValueByKey(decentralization, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(decentralization);
            }
        }
        return list;
    }

    public List<Decentralization> findDecentralizationsBy(Map<String, Object> conditions) {
        List<Decentralization> decentralizations = decentralizationList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            decentralizations = findObjectsBy(entry.getKey(), entry.getValue(), decentralizations);
        return decentralizations;
    }

    public boolean exists(Decentralization decentralization) {
        return !findDecentralizationsBy(Map.of(
            "role_id", decentralization.getRole_id(),
            "module_id", decentralization.getModule_id(),
            "function_id",decentralization.getFunction_id()
        )).isEmpty();
    }
    public boolean exists(Map<String, Object> conditions) {
        return !findDecentralizationsBy(conditions).isEmpty();
    }
    @Override
    public Object getValueByKey(Decentralization decentralization, String key) {
        return switch (key) {
            case "role_id" -> decentralization.getRole_id();
            case "module_id" -> decentralization.getModule_id();
            case "function_id" -> decentralization.getModule_id();
            default -> null;
        };
    }
}
