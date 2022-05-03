package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {

    public PersistentTransactionDAO(@Nullable Context context) {
        super(context, "190576D.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqldb) {
        String trsctStatement="CREATE TABLE Transactions (transaction_id INT PRIMARY KEY AUTOINCREMENT,date TEXT,account_no TEXT,transaction_type TEXT,amount REAL)";
        sqldb.execSQL(trsctStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String string_Date=format.format(date);
        String string_expense;
        if(expenseType==ExpenseType.EXPENSE){
            string_expense="Expense";
        } else{
            string_expense="Income";
        }

        contentValues.put("date",string_Date);
        contentValues.put("account_no",accountNo);
        contentValues.put("transaction_type",string_expense);
        contentValues.put("amount",amount);

        long add_state = db.insert("Transactions", null, contentValues);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> list_transactions=new ArrayList<Transaction>();
        SQLiteDatabase db=this.getReadableDatabase();
        String q_GTL="SELECT * FROM Transactions";
        Cursor cursor = db.rawQuery(q_GTL, null);
        if(cursor.moveToFirst()){
            do{
                Transaction transaction=null;
                transaction.setAccountNo(cursor.getString(2));
                String string_Date= cursor.getString(1);
                Date date=null;
                try {
                    date=new SimpleDateFormat("yyyy-MM-dd").parse(string_Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction.setDate(date);
                transaction.setAmount(cursor.getDouble(4));
                if(cursor.getString(3).equals("Expense")){
                    transaction.setExpenseType(ExpenseType.EXPENSE);
                } else{
                    transaction.setExpenseType(ExpenseType.INCOME);
                }
                list_transactions.add(transaction);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list_transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions=new ArrayList<Transaction>();
        SQLiteDatabase db=this.getReadableDatabase();
        String q_GTL="SELECT * FROM Transactions";
        Cursor cursor = db.rawQuery(q_GTL, null);
        if(cursor.moveToFirst()){
            do{
                Transaction transaction=null;
                transaction.setAccountNo(cursor.getString(2));
                String string_Date= cursor.getString(1);
                Date date=null;
                try {
                    date=new SimpleDateFormat("yyyy-MM-dd").parse(string_Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction.setDate(date);
                transaction.setAmount(cursor.getDouble(4));
                if(cursor.getString(3).equals("Expense")){
                    transaction.setExpenseType(ExpenseType.EXPENSE);
                } else{
                    transaction.setExpenseType(ExpenseType.INCOME);
                }
                transactions.add(transaction);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        int size=transactions.size();
        if(size<limit){
            return transactions;
        } else{
            return transactions.subList(size-limit,size);
        }
    }
}