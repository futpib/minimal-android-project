package pl.czak.minimal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

interface MessageProvider {
    String provideMessage();

    default String provideFormattedMessage() {
        return "[" + provideMessage() + "]";
    }
}

class DefaultMessageProvider implements MessageProvider {
    @Override
    public String provideMessage() {
        return "Hello world!";
    }
}

// Record uses invoke-custom for toString/equals/hashCode
record MessageRecord(String text, int priority) {}

public class MainActivity extends Activity
{
    // Static field
    private static final String TAG = "MainActivity";

    // Instance field
    private TextView label;

    // Direct method (constructor)
    public MainActivity() {
        super();
    }

    // Direct method (static)
    private static String getTag() {
        return TAG;
    }

    // Direct method (private instance)
    private void setupLabel() {
        label = new TextView(this);
        label.setText(getMessage());
    }

    // Try with specific error class
    private String parseNumber(String input) {
        try {
            Integer.parseInt(input);
            return "Valid number";
        } catch (NumberFormatException e) {
            return "Invalid number";
        }
    }

    // Try with catch-all
    private String safeGetMessage() {
        try {
            return getMessage();
        } catch (Exception e) {
            return "Error";
        }
    }

    // Virtual method (override)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLabel();
        label.setTag(computeTimestamp());
        getPackedSwitchResult(1);
        getSparseSwitchResult(100);
        getSmallArrayData();
        getLargeArrayData();
        sumToN(10);
        useLambda();
        useMethodReference();
        useRecord();
        setContentView(label);
    }

    // Virtual method (public, can be overridden)
    public String getMessage() {
        return "Hello world!";
    }

    // Method using 64-bit value (occupies 2 Dalvik registers)
    private long computeTimestamp() {
        long current = System.currentTimeMillis();
        long offset = 1000L;
        return current + offset;
    }

    // Packed switch (sequential case values 0,1,2,3)
    private String getPackedSwitchResult(int value) {
        switch (value) {
            case 0: return "zero";
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            default: return "other";
        }
    }

    // Sparse switch (non-sequential case values)
    private String getSparseSwitchResult(int value) {
        switch (value) {
            case 1: return "one";
            case 100: return "hundred";
            case 1000: return "thousand";
            case 10000: return "ten thousand";
            default: return "other";
        }
    }

    // Small array uses filled-new-array
    private int[] getSmallArrayData() {
        return new int[] { 10, 20, 30, 40, 50 };
    }

    // Larger array uses fill-array-data payload
    private int[] getLargeArrayData() {
        return new int[] { 10, 20, 30, 40, 50, 60, 70, 80 };
    }

    // Loop generates goto instruction
    private int sumToN(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    // Lambda expression generates invoke-custom/call site
    private String useLambda() {
        Supplier<String> supplier = () -> "Hello from lambda";
        return supplier.get();
    }

    // Method reference generates invoke-custom/call site
    private int useMethodReference() {
        List<String> items = Arrays.asList("a", "bb", "ccc");
        return items.stream().mapToInt(String::length).sum();
    }

    // Record's toString/equals/hashCode use invoke-custom via ObjectMethods bootstrap
    private String useRecord() {
        MessageRecord record = new MessageRecord("test", 1);
        return record.toString();
    }
}
