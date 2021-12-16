package br.edu.pucgo.vacinasocket

import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern

class GrupoEstado extends SocketServer {

	private static Pattern PATTERN = ~/\d{3}\.\d{3}\.\d\d(?<verificador>\d)-\d\d/
	private static List<Integer> VERIFICADORES_VALIDOS = 0..9
	private final int verificadorCPF
	private final List<Estado> estados = []
	private final String vacinadosKey = 'sistemas-distribuidos.vacina-socket.grupo-estados.vacinados'

	transient Exception transientException

	GrupoEstado(int verificadorCPF) {
		this.verificadorCPF = verificadorCPF
		this.estados.addAll(GruposEstado.getGrupoByVerificador(verificadorCPF).estados)
	}

	@Override
	void consumirMensagem(String mensagem) {
		def matcher = mensagem =~ PATTERN
		Integer verificador = (matcher.matches() && matcher.hasGroup() ? matcher.group('verificador') : null)?.toInteger()
		if (verificador == null) {
			writer.println('CPF mal formatado, tente novamente')
			return
		}
		if (verificador == this.verificadorCPF) {
			final List<String> vacinados = ConfigService.instance.getMapField(vacinadosKey, verificador.toString(), List) ?: []
			if (vacinados.contains(mensagem)) {
				writer.println("CPF já consta como vacinado no grupo com os seguintes estados: ${estados}.")
			} else {
				writer.println("CPF corresponde ao grupo com os seguintes estados: ${estados}. Vacinação confirmada.")
				vacinados.add(mensagem)
				ConfigService.instance.putInMap(vacinadosKey, verificador.toString(), vacinados)
			}
		} else {
			final int porta = GrupoEstadoConnectService.instance.findPortaByVerificador(verificador)
			SocketClient client = new SocketClient()
			client.conectar(porta)
			writer.println(client.enviarMensagem(mensagem))
			client.desconectar()
		}
	}

	@Override
	String toString() {
		return estados.collect { it.name() }.toString()
	}

	@Override
	void validaPorta() {
		GrupoEstadoConnectService.instance.updatePortaByVerificador(verificadorCPF, serverSocket.localPort)
		println("Rodando servidor do grupo de verificador='${verificadorCPF}' e com os seguintes estados='${estados}' na porta=${serverSocket.localPort}")
	}

	@Override
	void iniciar() {
		super.iniciar()
	}

	@Override
	void parar() {
		super.parar()
		GrupoEstadoConnectService.instance.deletePortaByVerificador(verificadorCPF)
	}

	static void main(String[] args) {
		Integer verificadorCPF = args.size() > 0 ? args[0].toInteger() : null
		final BufferedReader input = System.in.newReader()
		while (!(verificadorCPF != null && VERIFICADORES_VALIDOS.contains(verificadorCPF))) {
			println('Insira o dígito verificador do CPF correspondente a este grupo de 0-9: ')
			verificadorCPF = input.readLine()?.toInteger()
		}

		GrupoEstado grupo = new GrupoEstado(verificadorCPF)
		try {
			CompletableFuture.runAsync(() -> {
				try {
					grupo.iniciar()
				} catch (e) {
					grupo.transientException = e
					throw e
				}
			})
			CompletableFuture.runAsync(() -> {
				while (input.readLine() != 'sair');
				grupo.transientException = new InterruptedException('Saindo...')
			})
			while (true) {
				if (grupo.transientException) {
					throw grupo.transientException
				}
			}
		} finally {
			grupo.parar()
		}
	}
}
