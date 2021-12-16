package br.edu.pucgo.vacinasocket

class VacinaSocketApp {
	static void main(String[] args) {
		GruposEstado.values().toList().stream().parallel().forEach(grupo -> {
			GrupoEstado.main(grupo.ordinal().toString())
		})
	}
}
