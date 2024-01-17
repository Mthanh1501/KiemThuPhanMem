package com.supermarket.BLL;

import com.supermarket.DAL.ModuleDAL;
import com.supermarket.DTO.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleBLL extends Manager<Module>{
    private ModuleDAL moduleDAL;
    private List<Module> moduleList;

    public ModuleBLL() {
        moduleDAL = new ModuleDAL();
        moduleList = searchModules("deleted = 0");
    }

    public ModuleDAL getModuleDAL() {
        return moduleDAL;
    }

    public void setModuleDAL(ModuleDAL moduleDAL) {
        this.moduleDAL = moduleDAL;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public Object[][] getData() {
        return getData(moduleList);
    }

    public boolean addModule(Module module) {
        moduleList.add(module);
        return moduleDAL.addModule(module) != 0;
    }

    public boolean updateModule(Module module) {
        moduleList.set(getIndex(module, "id", moduleList), module);
        return moduleDAL.updateModule(module) != 0;
    }

    public boolean deleteModule(Module module) {
        moduleList.remove(getIndex(module, "id", moduleList));
        return moduleDAL.deleteModule("id = " + module.getId()) != 0;
    }

    public List<Module> searchModules(String... conditions) {
        return moduleDAL.searchModules(conditions);
    }

    public List<Module> findModules(String key, String value) {
        List<Module> list = new ArrayList<>();
        for (Module module : moduleList) {
            if (getValueByKey(module, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(module);
            }
        }
        return list;
    }

    public List<Module> findModulesBy(Map<String, Object> conditions) {
        List<Module> modules = moduleList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            modules = findObjectsBy(entry.getKey(), entry.getValue(), modules);
        return modules;
    }

    public boolean exists(Module module) {
        return !findModulesBy(Map.of(
            "name", module.getName()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findModulesBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Module module, String key) {
        return switch (key) {
            case "id" -> module.getId();
            case "name" -> module.getName();
            default -> null;
        };
    }
}
