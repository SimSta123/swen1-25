package TheorieFr.FragenTeil5;

public class Factorial {

    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative numbers not allowed");
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        System.out.println(result);
        return result;
    }

    public static void hello(){
        System.out.println("hello");
    }
}
