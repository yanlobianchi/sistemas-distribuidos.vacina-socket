package br.edu.pucgo.vacinasocket

class Cidade extends SocketClient {
	String nome
	Estado estado

	Cidade(String nome, Estado estado) {
		this.nome = nome
		this.estado = estado
	}

	void conectar() {
		final int verificador = GruposEstado.getGrupoByEstado(estado).ordinal()
		final int porta = GrupoEstadoConnectService.instance.findPortaByVerificador(verificador)
		super.conectar(porta)
		println("Cidade '${nome}' conectada no grupo de estado='${estado}', verificador='${verificador}' e porta='${porta}'")
	}

	static void main(String[] args) {
		final BufferedReader input = System.in.newReader()
		Estado estado
		String cidadeNome
		while (!estado || !cidadeNome) {
			println('Insira o estado: ')
			estado = Estado.valueOf(input.readLine())
			println('Insira o nome da cidade: ')
			cidadeNome = input.readLine()
		}
		Cidade cidade = new Cidade(cidadeNome, estado)
		cidade.conectar()

		while (true) {
			if (cidade.desconectado()) {
				throw new IllegalStateException('Cidade desconectado do servidor do grupo de estados, o mesmo pode ter caido')
			}

			println('Insira seu CPF para vacinar: ')

			String linha = input.readLine()
			if (linha == 'sair') {
				cidade.desconectar()
				break
			}

			if (linha) {
				println(cidade.enviarMensagem(linha))
			}
		}
	}
}
