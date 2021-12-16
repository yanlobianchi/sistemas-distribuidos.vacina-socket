package br.edu.pucgo.vacinasocket

abstract class SocketServer {

	protected final input = System.in.newReader()
	protected ServerSocket serverSocket
	protected Socket clientSocket
	protected PrintWriter writer
	protected BufferedReader reader

	void iniciar() {
		serverSocket = new ServerSocket(0)
		validaPorta()
		clientSocket = serverSocket.accept()
		writer = new PrintWriter(clientSocket.getOutputStream(), true)
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
		String linhaSocket

		while ((linhaSocket = reader.readLine()) != null) {
			if (linhaSocket == '-1') {
				parar()
				break
			}

			try {
				consumirMensagem(linhaSocket)
			} catch(Exception e) {
				writer.println("Falha ao consumir mensagem no socket: ${this.toString()}. ${e.message}")
			}
		}
	}

	void validaPorta() {
	}

	void parar() {
		println('Parando servidor na porta: ' + serverSocket.localPort)
		clientSocket?.shutdownInput()
		reader?.close()
		writer?.close()
		clientSocket?.close()
		serverSocket?.close()
	}

	abstract protected void consumirMensagem(String mensagem)

	abstract String toString()
}
