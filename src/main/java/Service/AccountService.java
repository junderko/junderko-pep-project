package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        if (accountDAO.getByUsername(account.getUsername()) != null) {
            return null;
        }
        return accountDAO.insert(account);
    }

    public Account login(Account account) {
        return accountDAO.getByCredentials(account.getUsername(), account.getPassword());
    }
}
