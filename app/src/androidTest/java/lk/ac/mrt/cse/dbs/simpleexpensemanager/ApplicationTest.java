/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.*;


import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class ApplicationTest{
    private static ExpenseManager expMgr;

    @Before
    public void setup() throws ExpenseManagerException {
        Context context=ApplicationProvider.getApplicationContext();
        expMgr=new PersistentExpenseManager(context);
    }

    @Test
    public void addAccount_test(){
        expMgr.addAccount("1756","Bank1","Andrew Dean",7000.0);
        List<String> acc_no_list=expMgr.getAccountNumbersList();
        boolean b= acc_no_list.contains("1756");
        assertTrue(b);
    }

    @Test
    public void getData_test(){
        List<Account> accounts=expMgr.getAccountsDAO().getAccountsList();
        assertTrue(accounts.size()==2 && accounts.get(0).getAccountNo().equals("12345A") && accounts.get(1).getAccountNo().equals("78945Z"));
    }
}