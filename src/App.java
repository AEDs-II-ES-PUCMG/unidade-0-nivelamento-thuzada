import java.time.LocalDate;

public class App {

	public static void main(String[] args) {
		Produto[] produtos = {
			new ProdutoNaoPerecivel("Detergente", 5.0),
			new ProdutoPerecivel("Queijo", 10.0, LocalDate.now().plusDays(5)),
			new ProdutoPerecivel("Arroz", 20.0, 0.3, LocalDate.now().plusDays(15))
		};

		for (Produto produto : produtos) {
			System.out.println(produto);
		}

	}
}
