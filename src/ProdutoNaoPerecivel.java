import java.util.Locale;

public class ProdutoNaoPerecivel extends Produto {

	public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
		super(desc, precoCusto, margemLucro);
	}

	public ProdutoNaoPerecivel(String desc, double precoCusto) {
		super(desc, precoCusto);
	}

    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * @return Uma string no formato "1; descrição;preçoDeCusto;margemDeLucro"
    */
    @Override
    public String gerarDadosTexto() {
        String precoformatado = String.format(Locale.US, "%.2f", precoCusto).replace(".", ",");
        String margemformatada = String.format(Locale.US, "%.2f", margemLucro).replace(".", ",");
        return String.format(Locale.US, "1; %s;%s;%s", descricao, 
                            precoformatado, margemformatada);
    }
}
