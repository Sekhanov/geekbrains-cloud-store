package ru.skhanov.mycloudstoreclient;

import java.io.IOException;
import java.util.concurrent.Exchanger;

import ru.skhanov.mycloudstorecommon.AbstractMessage;


/**
 * 
 * Класс для обмена сетевыми сообщениями между разными FX контроллерами
 */
public class MessageReciver implements Runnable {
	
	private Exchanger<AbstractMessage> exchanger;
	
	public MessageReciver(Exchanger<AbstractMessage> exchanger) {
		this.exchanger = exchanger;
		Thread thread = new Thread(this, "ClientMessageExchanger");
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				AbstractMessage abstractMessage = Network.readObject();
				exchanger.exchange(abstractMessage);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
