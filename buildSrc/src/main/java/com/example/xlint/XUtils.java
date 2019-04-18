package com.example.xlint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author David.Yi
 * @Describe io util
 * @create 2019/3/14
 */
public class XUtils {

    /**
     * copy input to output
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);

        try {
            input.close();
            output.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return count > 2147483647L ? -1 : (int) count;
    }

    private static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;

        int n1;
        for (boolean n = false; -1 != (n1 = input.read(buffer)); count += (long) n1) {
            output.write(buffer, 0, n1);
        }

        return count;
    }
}
