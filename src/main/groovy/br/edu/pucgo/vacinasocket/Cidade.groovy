package br.edu.pucgo.vacinasocket

class Cidade extends SocketClient {
	String nome
	Estado estado
	private int porta

	Cidade(String nome, Estado estado) {
		this.nome = nome
		this.estado = estado
		porta = GruposEstado.getGrupoByEstado(estado).ordinal()
	}

	void conectar() {
		super.conectar(porta)
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
			String linha = input.readLine()
			if (linha == 'sair') {
				cidade.desconectar()
				break
			}
			println('Insira seu CPF para vacinar: ')
			println(cidade.enviarMensagem(linha))
		}
	}
}
