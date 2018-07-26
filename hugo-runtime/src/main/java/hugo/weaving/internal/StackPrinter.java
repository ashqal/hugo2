package hugo.weaving.internal;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hzqiujiadi on 2018/7/26.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class StackPrinter {

    private static final String TAG = "StackPrinter";

    private static StackPrinter sInstance = new StackPrinter();

    private AtomicInteger integer = new AtomicInteger();

    private List<String> line = Collections.synchronizedList(new LinkedList<String>());

    public static StackPrinter shared() {
        return sInstance;
    }

    private Map<String, String> methodMap = new ConcurrentHashMap<>();

    public void printIn(long tag, String method) {
        java.util.Map<Thread, StackTraceElement[]> ts = Thread.getAllStackTraces();
        StackTraceElement[] ste = ts.get(Thread.currentThread());
        int skip = 0;
        StringBuilder sb = new StringBuilder();
        sb.append('s')
                .append(" | ").append(Thread.currentThread().getId())
                .append(" | ").append(tag)
                .append(" | ").append(method);
        for (StackTraceElement s : ste) {
            if (++skip > 4) {
                sb.append(" | ").append(toCode(s.toString()));
            }
        }
        line.add(sb.toString());
    }

    public void printOut(long tag, String method) {
        StringBuilder sb = new StringBuilder();
        sb.append('e')
                .append(" | ").append(Thread.currentThread().getId())
                .append(" | ").append(tag)
                .append(" | ").append(method);
        line.add(sb.toString());
    }

    private String toCode(String key) {
        String value = methodMap.get(key);
        if (TextUtils.isEmpty(value)) {
            value = String.format("0x%08x", integer.addAndGet(1));
            methodMap.put(key, value);
        }
        return value;
    }

    public void dump(File f) throws IOException {
        if (f.exists()) {
            f.delete();
        }

        PrintStream print = new PrintStream(f);

        line.add("begin method trace");
        for (Map.Entry<String, String> entry : methodMap.entrySet()) {
            line.add(entry.getValue() + "=" + entry.getKey());
        }

        for (String s : line) {
            print.println(s);
            Log.d(TAG, s);
        }

        methodMap.clear();
        line.clear();
        integer.set(0);

        print.flush();
        print.close();
    }
}
