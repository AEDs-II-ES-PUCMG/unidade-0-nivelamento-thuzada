import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
}
