package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context=context;
        setup();
    }

    @Override
    public void setup(){
        TransactionDAO persistentTransactionDAO=new PersistentTransactionDAO(this.context);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO=new PersistentAccountDAO(this.context);
        setAccountsDAO(persistentAccountDAO);

        //Dummy data
        Account dummyAcc1=new Account("12345A","Yoda Bank","Anakin Skywalker",10000.0);
        Account dummyAcc2=new Account("78945Z","Clone BC","Obi-Wan Kenobi",80000.0);
        getAccountsDAO().addAccount(dummyAcc1);
        getAccountsDAO().addAccount(dummyAcc2);
    }
}
