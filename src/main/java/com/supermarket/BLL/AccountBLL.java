package com.supermarket.BLL;

import com.supermarket.DAL.AccountDAL;
import com.supermarket.DTO.Account;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.DialogGUI.SmallDialog;
import com.supermarket.utils.DateTime;
import com.supermarket.utils.Email;
import com.supermarket.utils.Password;
import com.supermarket.utils.VNString;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountBLL extends Manager<Account> {
    private AccountDAL accountDAL;
    private List<Account> accountList;

    public AccountBLL() {
        accountDAL = new AccountDAL();
        accountList = searchAccounts("deleted = 0");
    }

    public AccountDAL getAccountDAL() {
        return accountDAL;
    }

    public void setAccountDAL(AccountDAL accountDAL) {
        this.accountDAL = accountDAL;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public Object[][] getData() {
        return getData(accountList);
    }

    public Pair<Boolean, String> addAccount(Account account) {
        Pair<Boolean, String> result;

        result = validateUserName(account.getUsername());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validatePassWord(account.getPassword());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = exists(account);
        if (result.getKey()) {
            return new Pair<>(false,result.getValue());
        }

        String password = Password.generateRandomPassword(8);
        String hashedPassword = Password.hashPassword(password);
        account.setPassword("first" + hashedPassword);
        account.setLast_signed_in(DateTime.MIN);

        if (accountDAL.addAccount(account) == 0)
            return new Pair<>(false, "Thêm tài khoản không thành công.");

        new Thread(() -> {
            Staff staff = new StaffBLL().findStaffsBy(Map.of("id", account.getStaffID())).get(0);
            String emailSubject = "Mật khẩu mặc định Bách Hóa Xanh";
            String emailBody = "Không được cung cấp mật khẩu này cho bất cứ ai: " + password;
            Email.sendOTP(staff.getEmail(), emailSubject, emailBody);
        }).start();
        accountList.add(account);
        return new Pair<>(true,"");
    }

    public Pair<Boolean, String> updateAccount(Account account) {
        Pair<Boolean, String> result;

        result = validateUserName(account.getUsername());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        result = validatePassWord(account.getPassword());
        if(!result.getKey()){
            return new Pair<>(false,result.getValue());
        }

        accountList.set(getIndex(account, "id", accountList), account);
        accountDAL.updateAccount(account);
        return new Pair<>(true,"");

    }

    public boolean updateAccountPassword(Account account) {
        Pair<Boolean, String> result;
        result = validatePassWord(account.getPassword());
        if(!result.getKey()){
            SmallDialog.showResult(result.getValue());
            return false;
        }

        accountList.set(getIndex(account, "id", accountList), account);
        return accountDAL.updateAccountPassword(account) != 0;
    }

    public boolean updateAccountLast_signed_in(Account account, DateTime dateTime) {
        accountList.set(getIndex(account, "id", accountList), account);
        return accountDAL.updateLast_signed_in(account, dateTime) != 0;
    }

    public boolean deleteAccount(Account account) {
        accountList.remove(getIndex(account, "id", accountList));
        return accountDAL.deleteAccount("id = " + account.getId()) != 0;
    }

    public List<Account> searchAccounts(String... conditions) {
        return accountDAL.searchAccounts(conditions);
    }

    public List<Account> findAccounts(String key, String value) {
        List<Account> list = new ArrayList<>();
        for (Account account : accountList) {
            if (getValueByKey(account, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(account);
            }
        }
        return list;
    }

    public List<Account> findAccountsBy(Map<String, Object> conditions) {
        List<Account> accounts = accountList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            accounts = findObjectsBy(entry.getKey(), entry.getValue(), accounts);
        return accounts;
    }
    public int getRoleIdByLoginCredentials(String username, String password) {
        // Xác định tài khoản dựa trên thông tin đăng nhập
        List<Account> accounts = accountDAL.searchAccounts("username = '" + username + "'", "password = '" + password + "'", "deleted = 0");

        // Nếu tìm thấy tài khoản, trả về role_id của tài khoản đó
        if (!accounts.isEmpty()) {
            Account account = accounts.get(0);
            return account.getRoleID();
        }

        // Trường hợp không tìm thấy tài khoản
        return -1; // Hoặc giá trị khác nếu bạn muốn xử lý khác
    }

//    public boolean exists(Account account) {
//        return !findAccountsBy(Map.of(
//            "username", account.getUsername(),
//            "role_id", account.getRoleID(),
//            "staff_id", account.getStaffID()
//        )).isEmpty();
//    }
//
//    public boolean exists(Map<String, Object> conditions) {
//        if (conditions.containsKey("username") && conditions.get("username").equals("admin")) {
//            return true;
//        }
//        return !findAccountsBy(conditions).isEmpty();
//    }
//public boolean exists(String userName) {
//    for (Account account : accountList) {
//        if (account.getUsername().equals(userName)) {
//            SmallDialog.showResult("Tên tài khoản đã tồn tại.");
//            return true;
//        }
//        if (account.getUsername().equals("admin")) {
//            SmallDialog.showResult("Tên tài khoản không được đặt trùng tên admin.");
//            return true;
//        }
//    }
//    return false;
//}
    public Pair<Boolean, String> exists(Account newAccount){
        List<Account> accounts = accountDAL.searchAccounts("username = '" + newAccount.getUsername() + "'", "deleted = 0");
        if(!accounts.isEmpty()){
            return new Pair<>(true, "Tên tài khoản đã tồn tại.");
        }
        return new Pair<>(false, "");
    }


    public Pair<Boolean, String> validatePassWord(String passWord){
        if(passWord.isBlank())
            return new Pair<>(false,"Mật khẩu tài khoản không được bỏ trống.");
        if(passWord.contains(" "))
            return new Pair<>(false,"Mật khẩu tài khoản không được có khoảng trắng.");
        if(!VNString.containsUpperCase(passWord))
            return new Pair<>(false,"Mật khẩu phải chứa ít nhất 1 chữ cái in hoa.");
        if(!VNString.containsNumber(passWord))
            return new Pair<>(false,"Mật khẩu phải chứa ít nhất 1 chữ số.");
        if(!VNString.containsSpecial(passWord))
            return new Pair<>(false,"Mật khẩu phải chứa ít nhất 1 ký tự đặc biệt.");
        if(!VNString.containsLowerCase(passWord))
            return new Pair<>(false,"Mật khẩu phải chứa ít nhất 1 chữ cái thường.");
        return new Pair<>(true,passWord);
    }


    public Pair<Boolean, String> validateUserName(String username){
        if(username.isBlank())
            return new Pair<>(false,"Tên tài khoản không được để trống.");
        if(VNString.containsUnicode(username))
            return new Pair<>(false,"Tên tài khoản không được chứa ký tự không hỗ trợ.");
        if(VNString.containsSpecial(username))
            return new Pair<>(false,"Tên tài khoản không được chứa ký tự đặc biệt.");
        return new Pair<>(true,username);
    }

    @Override
    public Object getValueByKey(Account account, String key) {
        return switch (key) {
            case "id" -> account.getId();
            case "username" -> account.getUsername();
            case "password" -> account.getPassword();
            case "role_id" -> account.getRoleID();
            case "staff_id" -> account.getStaffID();
            case "last_signed_in" -> account.getLast_signed_in();
            default -> null;
        };
    }
}
