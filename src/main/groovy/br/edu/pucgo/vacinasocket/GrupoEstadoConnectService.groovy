package br.edu.pucgo.vacinasocket

@Singleton
class GrupoEstadoConnectService {
	private final String chave = 'sistemas-distribuidos.vacina-socket.grupo-estados.portas-por-verificador'

	private Integer getPorta(int verificador) {
		return ConfigService.instance.getMapField(chave, verificador.toString()) as Integer
	}

	synchronized Integer findPortaByVerificador(int verificador) {
		final Integer porta = getPorta(verificador)
		if (!porta) {
			throw new IllegalStateException("Nenhum grupo de estados rodando para este verificador: ${verificador}")
		}
		return porta
	}

	synchronized String updatePortaByVerificador(Integer verificador, Integer porta) {
		final Integer portaRodando = getPorta(verificador)
		if (portaRodando) {
			throw new IllegalStateException("Este grupo já está rodando na porta: ${portaRodando}")
		}
		ConfigService.instance.putInMap(chave, verificador.toString(), porta.toString())
		return porta
	}

	synchronized void deletePortaByVerificador(Integer verificador) {
		ConfigService.instance.deleteFromMap(chave, verificador.toString())
	}

}
