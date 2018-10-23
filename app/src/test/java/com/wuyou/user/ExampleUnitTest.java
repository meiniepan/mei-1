package com.wuyou.user;

import com.wuyou.user.crypto.ec.EosPrivateKey;
import com.wuyou.user.crypto.util.HexUtils;
import com.wuyou.user.data.chain.PackedTransaction;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        int[][] arr = {{0, 0, 1, 1}, {1, 2, 1, 1}};
        System.out.println(arr[1][1]);

        assertEquals("ec(secp256k1) key pair not match! - 1"
                , new EosPrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3").getPublicKey().toString()
                , "EOS6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV");

        EosPrivateKey eosPrivateKey = new EosPrivateKey();
//        System.out.println(eosPrivateKey.toWif() +"........"+eosPrivateKey.getPublicKey());
//5Jk4wByKKoy4FtzEkNcY4JRdFJKBqaLq95Pb8MQF6gMXUaNWxov

        //EOS5CkzyPjWHtu1U9N7yVgFVQaSt4vfaLozgmHM1H5sLL3FZf5JgP

        EosPrivateKey privateKey = new EosPrivateKey("5Jk4wByKKoy4FtzEkNcY4JRdFJKBqaLq95Pb8MQF6gMXUaNWxov");
        System.out.println(privateKey.getPublicKey() + ".................");
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


//        String encryptString = EncryptUtil.getEncryptString(Constant.SAMPLE_PRIV_KEY_FOR_TEST, "555555");
//        System.out.println(encryptString);
        //FXSsJMI7b759opm2thtYOBmBpIQqxMXTa503cf665a08fa9302673f92ea7711d138ad773e7e22f4
        // 061f918e717a80fba1896a486e033c46a8c9e997b6316b2258cf5177481d9ec2700e01674bca1c9af4
//        String sss="L4VHg4EJmQ6Vfeu6Rgx4ABvbCZsQkko01bb366eff1b545027e1face8540ed01293ec" +
//                "0d36f2275101442d314a2d00d046553d5ab323cff5a4ffeaf3f179332aef91822f44cccfb9fc4b3272f0f265c907";
//        String decryptString = EncryptUtil.getDecryptString(encryptString, "555555");
//        System.out.println(decryptString);


        String ss = "aaabbbccc";
        byte[] a1 = ss.getBytes();
        String hex = HexUtils.toHex(a1);
        byte[] bytes1 = HexUtils.toBytes(hex);
        byte[] bytes2 = HexUtils.toBytesReversed(hex);
        String s1 = new String(bytes1);
        String s2 = new String(bytes2);

        byte[] a2 = compress1(a1);
        byte[] a3 = decompress1(a2);
        System.out.println(new String(a3));

        byte[] bytes = HexUtils.toBytes("386bc95bfdeb669d239c00000000011032dd2ad3ad32dde0b3db2e23a332dd0160e712256ea79d3700000000a8ed32322a00000000000000000101000000000000000060e712256ea79d37102700000000000004454f530000000000");
        byte[] b2 = decompress(bytes);

        String s = new String(b2);
        System.out.println(s);
    }

    public static byte[] decompress1(byte[] zipByte) throws IOException {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        Inflater inflater = new Inflater();
        inflater.setInput(zipByte);
        byte[] buff = new byte[1024];
        int byteNum = 0;
        while (!inflater.finished()) {
            try {
                byteNum = inflater.inflate(buff);
                aos.write(buff, 0, byteNum);
            } catch (DataFormatException e) {
                e.printStackTrace();
                return zipByte;
            }
        }
        return aos.toByteArray();
    }

    /**
     * 用zip算法压缩字节
     *
     * @param bytes 输入的待压缩字节
     * @throws IOException
     * @return 压缩就绪的字节
     */
    public static byte[] compress1(byte[] bytes) throws IOException {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        Deflater inflater = new Deflater();
        inflater.setInput(bytes);
        inflater.finish();
        byte[] buff = new byte[1024];
        int byteNum = 0;
        while (!inflater.finished()) {
            byteNum = inflater.deflate(buff);
            aos.write(buff, 0, byteNum);
        }
        return aos.toByteArray();
    }

    private byte[] compress(byte[] uncompressedBytes, PackedTransaction.CompressType compressType) {
        if (compressType == null || !PackedTransaction.CompressType.zlib.equals(compressType)) {
            return uncompressedBytes;
        }

        // zip!
        Deflater deflater = new Deflater();
        deflater.setInput(uncompressedBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(uncompressedBytes.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return uncompressedBytes;
        }

        return outputStream.toByteArray();
    }


    private byte[] decompress(byte[] compressedBytes) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedBytes.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (DataFormatException | IOException e) {
            e.printStackTrace();
            return compressedBytes;
        }


        return outputStream.toByteArray();
    }

    public long count(int... n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n.length; i++) {
            sb.append(n[i]);
        }

        char[] arr = sb.toString().toCharArray();
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    char temp;
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
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