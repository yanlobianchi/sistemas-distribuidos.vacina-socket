package br.edu.pucgo.vacinasocket

class Pais extends SocketServer {
	private final String sigla
	private final int totalDeVacinas

	Pais(String sigla, int totalDeVacinas) {
		this.sigla = sigla
		this.totalDeVacinas = totalDeVacinas
	}

	@Override
	protected void consumirMensagem(String mensagem) {
	}

	@Override
	String toString() {
		return sigla
	}
}
