package com.supermarket.BLL;

import com.supermarket.DAL.RoleDAL;
import com.supermarket.DTO.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleBLL extends Manager<Role>{
    private RoleDAL roleDAL;
    private List<Role> roleList;

    public RoleBLL() {
        roleDAL = new RoleDAL();
        roleList = searchRoles("deleted = 0");
    }

    public RoleDAL getRoleDAL() {
        return roleDAL;
    }

    public void setRoleDAL(RoleDAL roleDAL) {
        this.roleDAL = roleDAL;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public Object[][] getData() {
        return getData(roleList);
    }

    public boolean addRole(Role role) {
        roleList.add(role);
        return roleDAL.addRole(role) != 0;
    }

    public boolean updateRole(Role role) {
        roleList.set(getIndex(role, "id", roleList), role);
        return roleDAL.updateRole(role) != 0;
    }

    public boolean deleteRole(Role role) {
        roleList.remove(getIndex(role, "id", roleList));
        return roleDAL.deleteRole("id = " + role.getId()) != 0;
    }

    public List<Role> searchRoles(String... conditions) {
        return roleDAL.searchRoles(conditions);
    }

    public List<Role> findRoles(String key, String value) {
        List<Role> list = new ArrayList<>();
        for (Role role : roleList) {
            if (getValueByKey(role, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(role);
            }
        }
        return list;
    }

    public List<Role> findRolesBy(Map<String, Object> conditions) {
        List<Role> roles = roleList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            roles = findObjectsBy(entry.getKey(), entry.getValue(), roles);
        return roles;
    }

    public boolean exists(Role role) {
        return !findRolesBy(Map.of(
            "name", role.getName()
        )).isEmpty();
    }

    public boolean exists(Map<String, Object> conditions) {
        return !findRolesBy(conditions).isEmpty();
    }

    @Override
    public Object getValueByKey(Role role, String key) {
        return switch (key) {
            case "id" -> role.getId();
            case "name" -> role.getName();
            default -> null;
        };
    }
}
