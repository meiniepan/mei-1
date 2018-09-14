package com.wuyou.user;

import android.util.Log;

import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.data.local.db.EosAccount;
import com.wuyou.user.util.EncryptUtil;
import com.wuyou.user.util.RxUtil;

import org.bson.Document;
import org.junit.Test;

import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        int[][] arr ={{0,0,1,1},{1,2,1,1}};
        System.out.println(arr[1][1]);

        assertEquals( "ec(secp256k1) key pair not match! - 1"
                , new EosPrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3").getPublicKey().toString()
                , "EOS6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV" );

        EosPrivateKey eosPrivateKey = new EosPrivateKey();
//        System.out.println(eosPrivateKey.toWif() +"........"+eosPrivateKey.getPublicKey());
//5Jk4wByKKoy4FtzEkNcY4JRdFJKBqaLq95Pb8MQF6gMXUaNWxov

        //EOS5CkzyPjWHtu1U9N7yVgFVQaSt4vfaLozgmHM1H5sLL3FZf5JgP

        EosPrivateKey privateKey = new EosPrivateKey("5Jk4wByKKoy4FtzEkNcY4JRdFJKBqaLq95Pb8MQF6gMXUaNWxov");
        System.out.println(privateKey.getPublicKey()+".................");
//        try {
//            int x  = -12;
//            byte  y= (byte)(x&0xff);
//            System.out.println(y);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        List<EosAccount> allEosAccount = CarefreeDaoSession.getInstance().getAllEosAccount();
//        Log.e("Carefree", "addition_isCorrect: ");
//        EoscDataManager.getIns().getWalletManager().createOrOpenOwnerWallet()
//        CarefreeDaoSession.getInstance().searchName();
//        EosWallet wallet = new EosWallet();
//        wallet.setPassword("5555");
//        wallet.lock();
//        System.out.println(wallet.isLocked());
//        wallet.unlock("111111");
//        System.out.println(wallet.isLocked());
//
//        wallet.unlock("5555");
//        System.out.println(wallet.isLocked());


//        String s ="1111222233334444";
//        System.out.println(s.getBytes().length);
//        byte[] encrypt = CryptUtil.encrypt(Constant.SAMPLE_PRIV_KEY_FOR_TEST, s);
//        CryptUtil.decrypt(encrypt,s);


        String encryptString = EncryptUtil.getEncryptString(Constant.SAMPLE_PRIV_KEY_FOR_TEST, "555555");
        System.out.println(encryptString);
        //FXSsJMI7b759opm2thtYOBmBpIQqxMXTa503cf665a08fa9302673f92ea7711d138ad773e7e22f4
        // 061f918e717a80fba1896a486e033c46a8c9e997b6316b2258cf5177481d9ec2700e01674bca1c9af4
//        String sss="L4VHg4EJmQ6Vfeu6Rgx4ABvbCZsQkko01bb366eff1b545027e1face8540ed01293ec" +
//                "0d36f2275101442d314a2d00d046553d5ab323cff5a4ffeaf3f179332aef91822f44cccfb9fc4b3272f0f265c907";
        String decryptString = EncryptUtil.getDecryptString(encryptString, "555555");
        System.out.println(decryptString);
    }

    public long count(int... n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n.length; i++) {
            sb.append(n[i]);
        }

        char[] arr = sb.toString().toCharArray();
        for (int i = 0; i < arr.length-1 ; i++) {
            for (int j = 0; j < arr.length-1-i ; j++) {
                if (arr[j] < arr[j+1]) {
                    char temp;
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        sb = new StringBuilder();
        for (char c : arr) {
            sb.append(c);
        }
        System.out.println(sb.toString());
        return Long.parseLong(sb.toString());
    }

    public void ccc() {
        char[] arr = {'1', '3', '8', '2', '9', '0'};
        System.out.println("排序前数组为：");
        for (char num : arr) {
            System.out.print(num + " ");
        }
        for (int i = 0; i < arr.length - 1; i++) {//外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {//内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    char temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println();
        System.out.println("排序后的数组为：");
        for (char num : arr) {
            System.out.print(num + " ");
        }
    }

    private void print15() {
        Thread1 thread1 = new Thread1();
        Thread1 thread2 = new Thread1();
        Thread1 thread3 = new Thread1();

        while (n < 76) {
            int i1 = thread1.run(n);
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int i2 = thread2.run(i1);
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            n = thread3.run(i2);
            try {
                thread3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int n = 1;

    class ThreadLetter extends Thread {
        @Override
        public void run() {
            for (char c = 'A'; c <= 'Z'; c++) {
                printer.print(c);
            }
        }
    }

    class ThreadNumber extends Thread {
        @Override
        public void run() {
            for (int j = 1; j < 53; j++) {
                printer.print(j);
            }
        }
    }

    Printer printer = new Printer();
    int index = 1;

    class Printer {
        private synchronized void print(int i) {
            while (index % 3 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            index++;
            notifyAll();
            System.out.println("---" + i);
        }


        private synchronized void print(char c) {
            while (index % 3 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            index++;
            notifyAll();
            System.out.println("____" + c);
        }
    }

    class Thread1 extends Thread {
        public int run(int i) {
            for (int j = i; j < i + 5; j++) {
                System.out.println(Thread.currentThread() + ",,,,," + j);
            }
            return i + 5;
        }


    }
}