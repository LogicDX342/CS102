
import java.io.*;
import java.util.*;

public class Test {
    private static int I;
    private static int pairMatch = 0;
    static ArrayList<String> stm = new ArrayList<String>();
    // static HashMap<String, Integer> map = new HashMap<>();
    static MaxHeap heap1 = new MaxHeap();
    static MinHeap heap2 = new MinHeap();

    public static void main(String[] args) {
        QReader input = new QReader();
        QWriter output = new QWriter();
        int type;
        long score = 0;
        boolean check = true;
        String stmTemp;
        long a = 0;
        long b = 0;
        long m = 0;
        int n = input.nextInt();
        I = input.nextInt();

        for (int i = 0; i < n; i++) {
            m += input.nextInt();
        }

        for (int i = 0; i < n; i++) {
            type = input.nextInt();
            switch (type) {
                case 1:
                    stmTemp = input.next();
                    stm.add(stmTemp);
                    counterStatements(stmTemp.length());
                    // if (map.containsKey(stmTemp)) {
                    // pairMatch += map.get(stmTemp);
                    // map.replace(stmTemp, map.get(stmTemp) + 1);
                    // } else {
                    // map.put(stmTemp, 1);
                    // }
                    break;
                case 2:
                    I++;
                    pairMatch(input.next());
                    alterInfluenceGauge(counterStatements(-1));
                    output.println(counterStatements(-1));
                    break;
                case 3:
                    output.println(pairMatch);
                    break;
            }
            a += counterStatements(-1);
            b += pairMatch;
            if (I < 0) {
                check = false;
            }
        }
        score = a * b * m;

        if (check && score >= 0) {
            output.println("Qi Fei");
        } else {
            output.println("Fail");
        }
        output.close();
    }

    public static int counterStatements(int length) {
        if (length != -1) {
            if (length < heap1.getMax() || heap1.size() == 0) {
                heap1.push(length);
                if (heap1.size() > heap2.size() + 1) {
                    heap2.push(heap1.getMax());
                    heap1.pop();
                }
            } else {
                heap2.push(length);
                if (heap1.size() < heap2.size()) {
                    heap1.push(heap2.getMin());
                    heap2.pop();
                }
            }
            return 1;
        } else {
            return heap1.getMax();
        }
    }

    public static void alterInfluenceGauge(int k) {
        if (I < k) {
            I -= stm.size();
        }
    }

    public static int pairMatch(String tb) {
        // if (map.containsKey(tb)) {
        // pairMatch += map.get(tb);
        // }
        // return pairMatch;
        // }
        int cnt = 0;
        for (String stmTemp : stm) {
            if (stmTemp.equals(tb)) {
                cnt++;
            }
        }
        pairMatch += cnt;
        return pairMatch;
    }
}

class QReader {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private StringTokenizer tokenizer = new StringTokenizer("");

    private String innerNextLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean hasNext() {
        while (!tokenizer.hasMoreTokens()) {
            String nextLine = innerNextLine();
            if (nextLine == null) {
                return false;
            }
            tokenizer = new StringTokenizer(nextLine);
        }
        return true;
    }

    public String nextLine() {
        tokenizer = new StringTokenizer("");
        return innerNextLine();
    }

    public String next() {
        hasNext();
        return tokenizer.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }

    public long nextLong() {
        return Long.parseLong(next());
    }
}

class QWriter implements Closeable {
    private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

    public void print(Object object) {
        try {
            writer.write(object.toString());
        } catch (IOException e) {
            return;
        }
    }

    public void println(Object object) {
        try {
            writer.write(object.toString());
            writer.write("\n");
        } catch (IOException e) {
            return;
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            return;
        }
    }
}

class MinHeap {
    private static final int MAXSIZE = 10010;
    private int[] a = new int[MAXSIZE];

    private void swap(int x, int y) {
        a[x] = a[x] ^ a[y];
        a[y] = a[y] ^ a[x];
        a[x] = a[x] ^ a[y];
    }

    private void up() {
        int p = a[0];
        while (p > 1) {
            if (a[p] < a[p / 2]) {
                swap(p, p / 2);
                p = p / 2;
            } else
                break;
        }
    }

    public void push(int k) {
        a[++a[0]] = k;
        up();
    }

    public void pop() {
        int s = 2, t = 1;
        a[1] = a[a[0]--];
        while (s <= a[0]) {
            if (s <= a[0] - 1 && a[s + 1] < a[s])
                ++s;
            if (a[s] < a[t]) {
                swap(s, t);
                t = s;
                s *= 2;
            } else
                break;
        }
    }

    public int getMin() {
        return a[1];
    }

    public int size() {
        return a[0];
    }
}

class MaxHeap {
    private static final int MAXSIZE = 10010;
    private int[] a = new int[MAXSIZE];

    private void swap(int x, int y) {
        a[x] = a[x] ^ a[y];
        a[y] = a[y] ^ a[x];
        a[x] = a[x] ^ a[y];
    }

    private void up() {
        int p = a[0];
        while (p > 1) {
            if (a[p] > a[p / 2]) {
                swap(p, p / 2);
                p = p / 2;
            } else
                break;
        }
    }

    public void push(int k) {
        a[++a[0]] = k;
        up();
    }

    public void pop() {
        int s = 2, t = 1;
        a[1] = a[a[0]--];
        while (s <= a[0]) {
            if (s <= a[0] - 1 && a[s + 1] > a[s])
                ++s;
            if (a[s] > a[t]) {
                swap(s, t);
                t = s;
                s *= 2;
            } else
                break;
        }
    }

    public int getMax() {
        return a[1];
    }

    public int size() {
        return a[0];
    }
}
