package br.edu.pucgo.vacinasocket

abstract class SocketServer {

	protected ServerSocket serverSocket
	protected Socket clientSocket
	protected PrintWriter writer
	protected BufferedReader reader

	void iniciar(int porta) {
		serverSocket = new ServerSocket(porta)
		clientSocket = serverSocket.accept()
		writer = new PrintWriter(clientSocket.getOutputStream(), true)
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
		String linha

		while ((linha = reader.readLine()) != null) {
			try {
				consumirMensagem(linha)
			} catch(Exception e) {
				println("Falha ao consumir mensagem no socket: ${this.toString()}\n${e.message}")
			}
		}
	}

	void parar() {
		reader?.close()
		writer?.close()
		clientSocket?.close()
		serverSocket?.close()
	}

	abstract protected void consumirMensagem(String mensagem)

	abstract String toString()
}
