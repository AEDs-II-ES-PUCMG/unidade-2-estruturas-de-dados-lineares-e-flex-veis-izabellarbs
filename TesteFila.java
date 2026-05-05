import java.text.CharacterIterator;
import java.util.Scanner;

public class TesteFila {
    public static void main(String[] args) {
        Fila<Character> f = new Fila<Character>();
        Scanner teclado = new Scanner(System.in);

        System.out.println("Digite um caracter para ser adicionado a fila: ");
            char caracter = teclado.next().charAt(0);
            f.enfileirar(caracter);
            caracter = teclado.next().charAt(0);
        System.out.println("imprimindo...");
        System.out.println(f.consultarPrimeiro());
        teclado.close();
    }
}

