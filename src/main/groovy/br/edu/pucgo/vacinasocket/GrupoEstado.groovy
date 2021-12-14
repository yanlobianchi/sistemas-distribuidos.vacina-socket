package br.edu.pucgo.vacinasocket

class GrupoEstado extends SocketServer {

	private static List<Integer> VERIFICADORES_VALIDOS = 0..9

	final String id = UUID.randomUUID().toString()
	private final int verificadorCPF
	private final List<Estado> estados = []

	GrupoEstado(int verificadorCPF, Estado... estados) {
		this.verificadorCPF = verificadorCPF
		this.estados.addAll(estados)
	}

	@Override
	void consumirMensagem(String mensagem) {
		if (cpfPertenceAEsteGrupo(mensagem)) {
			writer.println("CPF corresponde ao grupo atual com os seguintes estados: ${estados}.")
		} else {
			writer.println("unrecognised greeting")
		}
	}

	@Override
	String toString() {
		return estados.collect { it.name() }.toString()
	}

	void iniciar() {
		super.iniciar(verificadorCPF)
	}

	void adicionarCidade() {

	}

	boolean cpfPertenceAEsteGrupo(String cpf) {
		Integer verificador = cpf?.find(/\d(?=\-\d\d)/)?.toInteger()
		return verificador == verificadorCPF
	}

	static void main(String[] args) {
		int verificadorCPF = args[0]?.toInteger()
		final BufferedReader input = System.in.newReader()
		while (!(verificadorCPF && VERIFICADORES_VALIDOS.contains(verificadorCPF))) {
			println('Insira o dígito verificador do CPF correspondente a este grupo de 0-9: ')
			verificadorCPF = input.readLine()?.toInteger()
		}

		GrupoEstado grupo = new GrupoEstado(verificadorCPF)
		try {
			grupo.iniciar()
		} finally {
			grupo.parar()
		}
	}
}
