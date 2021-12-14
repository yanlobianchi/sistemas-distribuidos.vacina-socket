package br.edu.pucgo.vacinasocket

enum GruposEstado {
	RS(Estado.RS),
	DF_GO_MT_MS_TO(Estado.DF, Estado.GO, Estado.MT, Estado.MS, Estado.TO),
	AM_PA_RR_AP_AC_RO(Estado.AM, Estado.PA, Estado.RR, Estado.AP, Estado.AC, Estado.RO),
	CE_MA_PI(Estado.CE, Estado.MA, Estado.PI),
	PB_PE_AL_RN(Estado.PB, Estado.PE, Estado.AL, Estado.RN),
	BA_SE(Estado.BA, Estado.SE),
	MG(Estado.MG),
	RJ_ES(Estado.RJ, Estado.ES),
	SP(Estado.SP),
	PR_SC(Estado.PR, Estado.SC)

	private final List<Estado> estados = []

	private GruposEstado(Estado... estados) {
		this.estados.addAll(estados)
	}

	static GruposEstado getGrupoByEstado(Estado estado) {
		return values().find { it.estados.contains(estado) }
	}

}
