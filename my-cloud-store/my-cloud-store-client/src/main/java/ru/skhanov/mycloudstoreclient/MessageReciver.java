package ru.skhanov.mycloudstoreclient;

import java.io.IOException;
import java.util.concurrent.Exchanger;

import ru.skhanov.mycloudstorecommon.AbstractMessage;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage;
import ru.skhanov.mycloudstorecommon.FileMessage;
import ru.skhanov.mycloudstorecommon.FileParametersListMessage;



public class MessageReciver implements Runnable {
	
	private Exchanger<AuthentificationMessage> authExchanger;
	private Exchanger<AbstractMessage> absExchanger;
	
	public MessageReciver(Exchanger<AuthentificationMessage> authExchanger, Exchanger<AbstractMessage> absExchanger) {
		this.authExchanger = authExchanger;
		this.absExchanger = absExchanger;
		Thread thread = new Thread(this, "ClientMessageExchanger");
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				AbstractMessage abstractMessage = Network.readObject();
				if(abstractMessage instanceof AuthentificationMessage) {
					AuthentificationMessage authentificationMessage = (AuthentificationMessage) abstractMessage;
					authExchanger.exchange(authentificationMessage);
				}
				if(abstractMessage instanceof FileParametersListMessage ||
						abstractMessage instanceof FileMessage) {
					absExchanger.exchange(abstractMessage);
				}
					
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
