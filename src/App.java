import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
	/** Para inclusão de novos produtos no vetor */
	static final int MAX_NOVOS_PRODUTOS = 10;

	/** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
	static String nomeArquivoDados;

	/** Scanner para leitura do teclado */
	static Scanner teclado;

	/** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
	static Produto[] produtosCadastrados;

	/** Quantidade produtos cadastrados atualmente no vetor */
	static int quantosProdutos;

	/** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
	static void pausa(){
	System.out.println("Digite enter para continuar...");
	teclado.nextLine();
	}

	/** Cabeçalho principal da CLI do sistema */
	static void cabecalho(){
	System.out.println("AEDII COMÉRCIO DE COISINHAS");
	System.out.println("===========================");
	}

	/** Imprime o menu principal, lê a opção do usuário e a retorna (int).
	* Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
	* @return Um inteiro com a opção do usuário.
	*/
	static int menu(){
	cabecalho();
	System.out.println("1 - Listar todos os produtos");
	System.out.println("2 - Procurar e listar um produto");
	System.out.println("3 - Cadastrar novo produto");
	System.out.println("0 - Sair");
	System.out.print("Digite sua opção: ");
	return Integer.parseInt(teclado.nextLine());
	}

	/**
	* Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
	* N (quantiade de produtos) <br/>
	* tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
	* Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
	* @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
	* @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
	*/
	static Produto[] lerProdutos(String nomeArquivoDados) {
		quantosProdutos = 0;

		try (Scanner arquivoDados = new Scanner(new java.io.File(nomeArquivoDados), "ISO-8859-2")) {
			if (!arquivoDados.hasNextLine()) {
				return new Produto[0];
			}

			int quantidadeProdutosArquivo = Integer.parseInt(arquivoDados.nextLine().trim());
			Produto[] vetorProdutos = new Produto[quantidadeProdutosArquivo + MAX_NOVOS_PRODUTOS];

			while (quantosProdutos < quantidadeProdutosArquivo && arquivoDados.hasNextLine()) {
				String linha = arquivoDados.nextLine().trim();
				if (!linha.isEmpty()) {
					vetorProdutos[quantosProdutos++] = Produto.criarDoTexto(linha);
				}
			}

			return vetorProdutos;
		} catch (Exception e) {
			quantosProdutos = 0;
			return new Produto[0];
		}
	}

	/** Lista todos os produtos cadastrados, numerados, um por linha */
	static void listarTodosOsProdutos(){
		try (Scanner arquivoDados = new Scanner(new java.io.File(nomeArquivoDados), "ISO-8859-2")) {
			int quantidadeProdutosArquivo = Integer.parseInt(arquivoDados.nextLine().trim());
			for (int i = 0; i < quantidadeProdutosArquivo && arquivoDados.hasNextLine(); i++) {
				String linha = arquivoDados.nextLine().trim();
				if (!linha.isEmpty()) {
					System.out.println((i + 1) + " - " + Produto.criarDoTexto(linha).gerarDadosTexto());
				}
			}
		} catch (Exception e) {
			System.out.println("Não foi possível listar os produtos.");
		}
	}

	/** Localiza um produto no vetor de cadastrados, a partir do nome (descrição), e imprime seus dados.
	* A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime mensagem padrão */
	static void localizarProdutos(){
		System.out.print("Digite o nome do produto: ");
		String nome = teclado.nextLine().trim();

		if (nome.length() < 3) {
			System.out.println("Produto não encontrado.");
			return;
		}

		Produto chaveBusca = new ProdutoNaoPerecivel(nome, 0.01);

		for (int i = 0; i < quantosProdutos; i++) {
			if (produtosCadastrados[i] != null && produtosCadastrados[i].equals(chaveBusca)) {
				System.out.println(produtosCadastrados[i]);
				return;
			}
		}

		System.out.println("Produto não encontrado.");
	}

	/**
	* Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
	* cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito
	* melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos.
	* Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos
	objetos.
	*/
	static void cadastrarProduto(){
		try {
			System.out.println("Cadastro de produto:");
			System.out.println("1 - Não perecível");
			System.out.println("2 - Perecível");
			System.out.print("Tipo: ");
			int tipo = Integer.parseInt(teclado.nextLine().trim());

			System.out.print("Descrição: ");
			String descricao = teclado.nextLine().trim();

			System.out.print("Preço de custo: ");
			double precoCusto = Double.parseDouble(teclado.nextLine().trim().replace(",", "."));

			System.out.print("Margem de lucro (ex.: 0,2 para 20%): ");
			double margemLucro = Double.parseDouble(teclado.nextLine().trim().replace(",", "."));

			Produto novoProduto;
			if (tipo == 1) {
				novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
			} else if (tipo == 2) {
				System.out.print("Data de validade (dd/MM/yyyy): ");
				String dataTexto = teclado.nextLine().trim();
				LocalDate dataValidade = LocalDate.parse(dataTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade);
			} else {
				System.out.println("Tipo inválido.");
				return;
			}

			if (produtosCadastrados == null) {
				produtosCadastrados = new Produto[MAX_NOVOS_PRODUTOS];
			} else if (quantosProdutos >= produtosCadastrados.length) {
				Produto[] novoVetor = new Produto[produtosCadastrados.length + MAX_NOVOS_PRODUTOS];
				for (int i = 0; i < produtosCadastrados.length; i++) {
					novoVetor[i] = produtosCadastrados[i];
				}
				produtosCadastrados = novoVetor;
			}

			produtosCadastrados[quantosProdutos++] = novoProduto;
			System.out.println("Produto cadastrado com sucesso.");
		} catch (Exception e) {
			System.out.println("Não foi possível cadastrar o produto.");
		}
	}

	/**
	* Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
	* @param nomeArquivo Nome do arquivo a ser gravado.
	*/
	public static void salvarProdutos(String nomeArquivo){
		try (java.io.PrintWriter arquivoSaida = new java.io.PrintWriter(nomeArquivo, "ISO-8859-2")) {
			if (produtosCadastrados == null) {
				arquivoSaida.println(0);
				return;
			}

			arquivoSaida.println(quantosProdutos);
			for (int i = 0; i < quantosProdutos; i++) {
				if (produtosCadastrados[i] != null) {
					arquivoSaida.println(produtosCadastrados[i].gerarDadosTexto());
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao salvar os produtos no arquivo.");
		}
	}

	public static void main(String[] args) throws Exception {
	teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
	nomeArquivoDados = "dadosProdutos.csv";
	produtosCadastrados = lerProdutos(nomeArquivoDados);
	int opcao = -1;
	do{
	opcao = menu();
	switch (opcao) {
	case 1 -> listarTodosOsProdutos();
	case 2 -> localizarProdutos();
	case 3 -> cadastrarProduto();
	}
	pausa();
	}while(opcao !=0);
	salvarProdutos(nomeArquivoDados);
	teclado.close();
	}

}
