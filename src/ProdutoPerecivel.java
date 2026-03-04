import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ProdutoPerecivel extends Produto {

	private LocalDate dataValidade;

	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataValidade) {
		super(desc, precoCusto, margemLucro);
		validarDataValidade(dataValidade);
		this.dataValidade = dataValidade;
	}

	public ProdutoPerecivel(String desc, double precoCusto, LocalDate dataValidade) {
		super(desc, precoCusto);
		validarDataValidade(dataValidade);
		this.dataValidade = dataValidade;
	}

	private void validarDataValidade(LocalDate dataValidade) {
		if (dataValidade == null || dataValidade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade inválida.");
		}
	}

	@Override
	public double valorDeVenda() {
		LocalDate hoje = LocalDate.now();

		if (dataValidade.isBefore(hoje)) {
			throw new IllegalArgumentException("Produto fora da validade.");
		}

		double valor = super.valorDeVenda();
		long diasParaVencer = ChronoUnit.DAYS.between(hoje, dataValidade);

		if (diasParaVencer <= 7) {
			return valor * 0.75;
		}

		return valor;
	}

	/**
	* Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
	decimais.
	* Data de validade vai no formato dd/mm/aaaa
	* @return Uma string no formato "2; descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
	*/
	@Override
	public String gerarDadosTexto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dataFormatada = formatter.format(dataValidade);
        String precoformatado = String.format(Locale.US, "%.2f", precoCusto).replace(".", ",");
        String margemformatada = String.format(Locale.US, "%.2f", margemLucro).replace(".", ",");
		return String.format(Locale.US, "2; %s;%s;%s;%s", descricao, precoformatado, margemformatada,
		        dataFormatada);
	}
}
