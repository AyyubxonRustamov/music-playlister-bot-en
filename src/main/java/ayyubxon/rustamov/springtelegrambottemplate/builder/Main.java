package ayyubxon.rustamov.springtelegrambottemplate.builder;

public class Main {
    public static void main(String[] args) {
        String s = "Hello how are you Contestant";
        int k = 4;
        String[] arrayS = s.split(" ");
        String sum = "";
        for (int i = 0; i < k; i++) {
            sum += arrayS[i];
            sum += " ";
        }
        System.out.println(sum);
    }
}
