package br.edu.pucgo.vacinasocket

class SocketClient {
	private Socket clientSocket
	private PrintWriter writer
	private BufferedReader reader

	void conectar(int port) {
		clientSocket = new Socket('localhost', port)
		writer = new PrintWriter(clientSocket.getOutputStream(), true)
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
	}

	String enviarMensagem(String msg) {
		writer.println(msg)
		String resp = reader.readLine()
		return resp
	}

	void desconectar() {
		reader.close()
		writer.close()
		clientSocket.close()
	}
}
