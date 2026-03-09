public class ItemDePedido {

    // Atributos encapsulados
    private Produto produto;
    public int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
			this.produto = produto;
			this.precoVenda = produto.precoCusto;
			this.quantidade = quantidade;
    }

    public double calcularSubtotal() {
        return quantidade*precoVenda;
    }

    // --- Sobrescrita do método equals ---

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override


    	public boolean equals(Object obj){
		produto = (Produto)obj;
		return produto.descricao.toLowerCase().equals(produto.descricao.toLowerCase());
	}
}
