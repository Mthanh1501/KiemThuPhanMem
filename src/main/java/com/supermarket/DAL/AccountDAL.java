package com.supermarket.DAL;

import com.supermarket.DTO.Account;
import com.supermarket.utils.DateTime;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAL extends Manager{
    public AccountDAL() {
        super("account",
            List.of("id",
                "username",
                "password",
                "role_id",
                "staff_id",
                "last_signed_in",
                "deleted"));
    }

    public List<Account> convertToAccounts(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Account(
                    Integer.parseInt(row.get(0)), // id
                    row.get(1), // username
                    row.get(2), // password
                    Integer.parseInt(row.get(3)), // role_id
                    Integer.parseInt(row.get(4)), // staff_id
                    DateTime.parseDateTime(row.get(5)), //last_signed_in
                    Boolean.parseBoolean(row.get(6))    // deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in AccountDAL.convertToAccounts(): " + e.getMessage());
            }
            return new Account();
        });
    }

    public int addAccount(Account account) {
        try {
            return create(account.getId(),
                account.getUsername(),
                account.getPassword(),
                account.getRoleID(),
                account.getStaffID(),
                account.getLast_signed_in().toSQL(),
                false
            ); // account khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.addAccount(): " + e.getMessage());
        }
        return 0;
    }

    public int updateAccount(Account account) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(account.getId());
            updateValues.add(account.getUsername());
            updateValues.add(account.getPassword());
            updateValues.add(account.getRoleID());
            updateValues.add(account.getStaffID());
            updateValues.add(account.getLast_signed_in().toSQL());
            updateValues.add(account.isDeleted());
            return update(updateValues, "id = " + account.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.updateAccount(): " + e.getMessage());
        }
        return 0;
    }

   // updateLast_signed_in va updateAccountPassword không cần làm chỉ cần làm những hàm còn lại
    public int updateAccountPassword(Account account) {
        try {
            String query = "UPDATE `" + getTableName() + "` SET password = '" + account.getPassword() + "' WHERE id = " + account.getId() + ";";
            return executeUpdate(query);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.updateAccountPassword(): " + e.getMessage());
        }
        return 0;
    }
    public int updateLast_signed_in(Account account, DateTime dateTime) {
        try {
            String query = "UPDATE `" + getTableName() + "` SET last_signed_in = '" + dateTime.toSQL() + "' WHERE id = " + account.getId() + ";";
            return executeUpdate(query);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.updateAccountLast_signed_in(): " + e.getMessage());
        }
        return 0;
    }

    public int deleteAccount(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.deleteAccount(): " + e.getMessage());
        }
        return 0;
    }

    public List<Account> searchAccounts(String... conditions) {
        try {
            return convertToAccounts(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in AccountDAL.searchAccounts(): " + e.getMessage());
        }
        return new ArrayList<>();
    }

}
