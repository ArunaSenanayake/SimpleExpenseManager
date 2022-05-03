package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PersistentAccountDAO extends SQLiteOpenHelper implements AccountDAO {

    public PersistentAccountDAO(@Nullable Context context) {
        super(context, "190576D.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqldb) {
        String acctStatement="CREATE TABLE Accounts (account_no TEXT PRIMARY KEY,bank_name TEXT,account_holder_name TEXT,balance REAL)";
        sqldb.execSQL(acctStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqldb, int i, int i1) {

    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> list_AcctNo=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        String q_GetAcctNo_ls="SELECT account_no FROM Accounts";
        Cursor cursor = db.rawQuery(q_GetAcctNo_ls, null);
        if(cursor.moveToFirst()){
            do{
                String cur_ac_no=cursor.getString(0);
                list_AcctNo.add(cur_ac_no);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list_AcctNo;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> list_Acct=new ArrayList<Account>();
        SQLiteDatabase db=this.getReadableDatabase();
        String q_GetAcct_ls="SELECT * FROM Accounts";
        Cursor cursor = db.rawQuery(q_GetAcct_ls, null);
        if(cursor.moveToFirst()){
            do{
                Account account=new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
                list_Acct.add(account);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list_Acct;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account  rtAccount=null;
        SQLiteDatabase db=this.getReadableDatabase();
        String q_GetAcct="SELECT * FROM Accounts WHERE account_no="+accountNo;
        Cursor cursor = db.rawQuery(q_GetAcct,null);
        if(cursor.moveToFirst()){
            rtAccount.setAccountNo(cursor.getString(0));
            rtAccount.setBankName(cursor.getString(1));
            rtAccount.setAccountHolderName(cursor.getString(2));
            rtAccount.setBalance(cursor.getDouble(3));
        }
        cursor.close();
        db.close();
        return rtAccount;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("account_no",account.getAccountNo());
        contentValues.put("bank_name",account.getBankName());
        contentValues.put("account_holder_name",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());

        long add_state = db.insert("Accounts", null, contentValues);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String q_rmAcct="DELETE FROM Accounts WHERE account_no="+accountNo;
        db.execSQL(q_rmAcct);
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String q_GetAcct="SELECT * FROM Accounts WHERE account_no="+accountNo;
        Cursor cursor = db.rawQuery(q_GetAcct, null);
        double old_amount=0.0;
        double new_amount=0.0;
        if(cursor.moveToFirst()){
            old_amount= cursor.getDouble(3);
        }
        if(expenseType==ExpenseType.EXPENSE){
            new_amount=old_amount-amount;
        } else{
            new_amount=old_amount+amount;
        }
        cursor.close();
        String q_update="UPDATE Accounts SET amount="+new_amount+" WHERE account_no="+accountNo;
        db.execSQL(q_update);
        db.close();
    }
}