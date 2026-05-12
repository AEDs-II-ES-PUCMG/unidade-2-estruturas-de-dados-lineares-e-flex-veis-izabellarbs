import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

	/** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;

    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    // TODO: Tarefa 5 - Substituir a pilha abaixo por uma Lista<Pedido> para armazenar os pedidos.
    static Pilha<Pedido> pilhaPedidos = new Pilha<>();

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {

    	T valor;

    	System.out.println(mensagem);
    	try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int). */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar por um produto, por código");
        System.out.println("3 - Procurar por um produto, por nome");
        System.out.println("4 - Iniciar novo pedido");
        System.out.println("5 - Fechar pedido");
        System.out.println("6 - Filtrar pedidos por produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {

    	Scanner arquivo = null;
    	int numProdutos;
    	String linha;
    	Produto produto;
    	Produto[] produtosCadastrados;

    	try {
    		arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));

    		numProdutos = Integer.parseInt(arquivo.nextLine());
    		produtosCadastrados = new Produto[numProdutos];

    		for (int i = 0; i < numProdutos; i++) {
    			linha = arquivo.nextLine();
    			produto = Produto.criarDoTexto(linha);
    			produtosCadastrados[i] = produto;
    		}
    		quantosProdutos = numProdutos;

    	} catch (IOException excecaoArquivo) {
    		produtosCadastrados = null;
    	} finally {
    		arquivo.close();
    	}

    	return produtosCadastrados;
    }

    /** Localiza um produto no vetor de produtos cadastrados, a partir do código informado. */
    static Produto localizarProduto() {

    	Produto produto = null;
    	Boolean localizado = false;

    	cabecalho();
    	System.out.println("Localizando um produto...");
        int idProduto = lerOpcao("Digite o código identificador do produto desejado: ", Integer.class);
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
        	if (produtosCadastrados[i].hashCode() == idProduto) {
        		produto = produtosCadastrados[i];
        		localizado = true;
        	}
        }

        return produto;
    }

    /** Localiza um produto no vetor de produtos cadastrados, a partir da descrição informada. */
    static Produto localizarProdutoDescricao() {

    	Produto produto = null;
    	Boolean localizado = false;
    	String descricao;

    	cabecalho();
    	System.out.println("Localizando um produto...");
    	System.out.println("Digite o nome ou a descrição do produto desejado:");
        descricao = teclado.nextLine();
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
        	if (produtosCadastrados[i].descricao.equals(descricao)) {
        		produto = produtosCadastrados[i];
        		localizado = true;
    		}
        }

        return produto;
    }

    private static void mostrarProduto(Produto produto) {

        cabecalho();
        String mensagem = "Dados inválidos para o produto!";

        if (produto != null){
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {

        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < quantosProdutos; i++) {
        	System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
        }
    }

    /** Inicia um novo pedido permitindo ao usuário escolher e incluir produtos. */
    public static Pedido iniciarPedido() {

    	int formaPagamento = lerOpcao("Digite a forma de pagamento do pedido, sendo 1 para pagamento à vista e 2 para pagamento a prazo", Integer.class);
    	Pedido pedido = new Pedido(LocalDate.now(), formaPagamento);
    	Produto produto;
    	int numProdutos;
    	int quantidade;

    	listarTodosOsProdutos();
    	System.out.println("Incluindo produtos no pedido...");
    	numProdutos = lerOpcao("Quantos produtos serão incluídos no pedido?", Integer.class);
        for (int i = 0; i < numProdutos; i++) {
        	produto = localizarProdutoDescricao();
        	if (produto == null) {
        		System.out.println("Produto não encontrado");
        		i--;
        	} else {
        		quantidade = lerOpcao("Quantos itens desse produto serão incluídos no pedido?", Integer.class);
        		pedido.incluirProduto(produto, quantidade);
        	}
        }

        return pedido;
    }

    /**
     * Finaliza um pedido, armazenando-o na lista de pedidos.
     */
    public static void finalizarPedido(Pedido pedido) {
    	// TODO: Tarefa 5 - Verificar se o pedido é válido e armazená-lo na lista de pedidos.
    	//       Exibir o pedido finalizado na tela.
    }

    /**
     * Filtra e exibe os pedidos que apresentam um produto específico,
     * cuja descrição foi informada pelo usuário.
     */
    public static void filtrarPorProduto() {
    	// TODO: Tarefa 5 - Ler a descrição do produto informada pelo usuário.
    	//       Utilizar obrigatoriamente o método filtrar (Lista<E>) para selecionar os pedidos
    	//       e o método buscarPor (Lista<E>) para verificar se um pedido contém o produto buscado.
    	//       Exibir os pedidos encontrados ou uma mensagem caso nenhum seja localizado.
    }

	public static void main(String[] args) {

		teclado = new Scanner(System.in, Charset.forName("UTF-8"));

        nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);

        Pedido pedido = null;

        int opcao = -1;

        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> mostrarProduto(localizarProduto());
                case 3 -> mostrarProduto(localizarProdutoDescricao());
                case 4 -> pedido = iniciarPedido();
                case 5 -> finalizarPedido(pedido);
                case 6 -> filtrarPorProduto();
            }
            pausa();
        } while (opcao != 0);

        teclado.close();
    }
}
