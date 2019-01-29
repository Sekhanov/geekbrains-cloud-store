package ru.skhanov.mycloudstoreclient;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Exchanger;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.skhanov.mycloudstorecommon.AbstractMessage;

public class Network {
	private static Socket socket;
	private static ObjectEncoderOutputStream objectEncoderOutputStream;
	private static ObjectDecoderInputStream objectDecoderInputStream;
	private static Exchanger<AbstractMessage> absMesExchanger;

	private static final int MAX_OBJ_SIZE = 100 * 1024 * 1024;

	public static void start() {
		try {
			socket = new Socket("localhost", 8189);
			objectEncoderOutputStream = new ObjectEncoderOutputStream(socket.getOutputStream());
			objectDecoderInputStream = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJ_SIZE);
			absMesExchanger = new Exchanger<>();
			new MessageReciver(absMesExchanger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Exchanger<AbstractMessage> getAbsMesExchanger() {
		return absMesExchanger;
	}

	public static void setAbsMesExchanger(Exchanger<AbstractMessage> absMesExchanger) {
		Network.absMesExchanger = absMesExchanger;
	}




	public static void stop() {
		try {
			objectEncoderOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			objectDecoderInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean sendMsg(AbstractMessage msg) {
		try {
			objectEncoderOutputStream.writeObject(msg);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
		Object object = objectDecoderInputStream.readObject();
		return (AbstractMessage) object;
	}

}
