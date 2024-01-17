package com.supermarket.BLL;

import com.supermarket.DAL.FunctionDAL;
import com.supermarket.DTO.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionBLL extends Manager<Function>{
    private FunctionDAL functionDAL;
    private List<Function> functionList;

    public FunctionBLL() {
        functionDAL = new FunctionDAL();
        functionList = searchFunctions("deleted = 0");
    }

    public FunctionDAL getFunctionDAL() {
        return functionDAL;
    }

    public void setFunctionDAL(FunctionDAL functionDAL) {
        this.functionDAL = functionDAL;
    }

    public List<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<Function> functionList) {
        this.functionList = functionList;
    }

    public Object[][] getData() {
        return getData(functionList);
    }

    public boolean addFunction(Function Function) {
        functionList.add(Function);
        return functionDAL.addFunction(Function) != 0;
    }

    public boolean updateFunction(Function function) {
        functionList.set(getIndex(function, "id", functionList), function);
        return functionDAL.updateFunction(function) != 0;
    }

    public boolean deleteFunction(Function function) {
        functionList.remove(getIndex(function, "id", functionList));
        return functionDAL.deleteFunction("id = " + function.getId()) != 0;
    }

    public List<Function> searchFunctions(String... conditions) {
        return functionDAL.searchFunctions(conditions);
    }

    public List<Function> findFunctions(String key, String value) {
        List<Function> list = new ArrayList<>();
        for (Function function : functionList) {
            if (getValueByKey(function, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(function);
            }
        }
        return list;
    }

    public List<Function> findFunctionsBy(Map<String, Object> conditions) {
        List<Function> functions = functionList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            functions = findObjectsBy(entry.getKey(), entry.getValue(), functions);
        return functions;
    }

    public boolean exists(Function function) {
        return !findFunctionsBy(Map.of(
            "name", function.getName()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findFunctionsBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Function function, String key) {
        return switch (key) {
            case "id" -> function.getId();
            case "name" -> function.getName();
            default -> null;
        };
    }
}
