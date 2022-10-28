import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 * @author George Marcus
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		SerejaAndBubbleSort solver = new SerejaAndBubbleSort();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
}

class SerejaAndBubbleSort {
    private static final int MAXN = 101;
    private static double[][] dp;
    private static double[][] suffixSum;
    private static double[][] suffixPSum;

    public void solve(int testNumber, InputReader in, PrintWriter out) {
        if (testNumber == 1) {
            initDp();
        }
        int N = in.nextInt();
        long K = in.nextLong();
        int[] A = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = in.nextInt();
        }

        int invCount = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (A[i] > A[j]) {
                    invCount++;
                }
            }
        }

        double ans = -1.0;
        if (invCount <= K) {
            ans = 0.0;
        } else {
//            ans = slowDp(N, invCount, (int) K);
            ans = fastDp(N, invCount, (int) K);
        }

        out.format("%.10f\n", ans);
    }

    private double fastDp(int n, int invCount, int K) {
        double[] f = new double[K];
        int lim = n * (n - 1) / 2;
        for (int k = 0; k < K; k++) {
            double next = k > 0 ? f[k - 1] : lim + 1;
            int a = k + 1;
            int b = Math.min(lim, k + (int) next);
            double crt = calcPSum(n, a, b);
            if (b + 1 <= lim) {
                crt += suffixSum[n][b + 1] * next;
            }
            f[k] = crt;
        }
        double ans = invCount - K;
        if (K > 0) {
            ans = Math.min(ans, f[K - 1]);
        }
        return ans;
    }

    private double calcPSum(int n, int a, int b) {
        double ret = suffixPSum[n][a];
        if (b + 1 < suffixSum[n].length) {
            ret -= suffixPSum[n][b + 1] + (b - a + 1) * suffixSum[n][b + 1];
        }
        return ret;
    }

    private void initDp() {
        dp = new double[MAXN][];
        suffixSum = new double[MAXN][];
        suffixPSum = new double[MAXN][];
        for (int i = 0; i < MAXN; i++) {
            int lim = i * (i - 1) / 2;
            dp[i] = new double[lim + 1];
            suffixSum[i] = new double[lim + 1];
            suffixPSum[i] = new double[lim + 1];
        }
        dp[1][0] = 1.0;
        for (int i = 1; i < MAXN - 1; i++) {
            int lim = i * (i - 1) / 2;
            double prob = 1.0 / (i + 1);
            for (int j = 0; j <= lim; j++) {
                for (int k = 0; k <= i; k++) {
                    dp[i + 1][j + k] += dp[i][j] * prob;
                }
            }
        }
        for (int i = 1; i < MAXN; i++) {
            int lim = i * (i - 1) / 2;
            for (int j = lim; j >= 0; j--) {
                suffixSum[i][j] = suffixPSum[i][j] = dp[i][j];
                if (j < lim) {
                    suffixSum[i][j] += suffixSum[i][j + 1];
                    suffixPSum[i][j] += suffixSum[i][j + 1] + suffixPSum[i][j + 1];
                }
            }
        }
    }
}

class InputReader {
    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;
    private int numChars;

    public InputReader(InputStream stream) {
        this.stream = stream;
    }

    public String next() {
        return nextString();
    }

    public int read() {
        if (numChars == -1)
            throw new InputMismatchException();
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (numChars <= 0)
                return -1;
        }
        return buf[curChar++];
    }

    public int nextInt() {
        return Integer.parseInt(nextString());
    }

    public long nextLong() {
        return Long.parseLong(nextString());
    }

    public String nextString() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        StringBuffer res = new StringBuffer();
        do {
            res.appendCodePoint(c);
            c = read();
        } while (!isSpaceChar(c));

        return res.toString();
    }

    private boolean isSpaceChar(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

}

