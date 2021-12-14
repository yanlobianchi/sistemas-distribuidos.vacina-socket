package br.edu.pucgo.vacinasocket

class GrupoEstado extends SocketServer {

	private static List<Integer> VERIFICADORES_VALIDOS = 0..9

	private final int verificadorCPF
	private final List<Estado> estados = []

	GrupoEstado(int verificadorCPF, Estado... estados) {
		this.verificadorCPF = verificadorCPF
		this.estados.addAll(estados)
	}

	@Override
	void consumirMensagem(String mensagem) {
		Integer verificador = mensagem?.find(/\d(?=\-\d\d)/)?.toInteger()
		if (verificador == this.verificadorCPF) {
			writer.println("CPF corresponde ao grupo atual com os seguintes estados: ${estados}.")
		} else {
			SocketClient client = new SocketClient()
			client.conectar(verificador)
			println(client.enviarMensagem(mensagem))
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
