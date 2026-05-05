import java.util.Scanner;

public class Teste {
    public static void main(String[] args) {
        Pilha<Integer> p = new Pilha<Integer>();
        Scanner teclado = new Scanner(System.in);

        System.out.println("Digite um valor para ser adicionado a pilha: ");
        int num = teclado.nextInt();
        while (num != -1) {
            p.empilhar(num);
            num = teclado.nextInt();
        }
        System.out.println("imprimindo...");
        System.out.println(p.listaDados());
        teclado.close();
    }
}
