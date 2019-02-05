package ru.skhanov.mycloudstoreserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage;
import ru.skhanov.mycloudstorecommon.FileMessage;
import ru.skhanov.mycloudstorecommon.FileOperationsMessage;
import ru.skhanov.mycloudstorecommon.FileParametersListMessage;


/**
 * Класс-обработчик всех входящих десериализованных классов-сообщений от клиента
 */
public class MainHandler extends ChannelInboundHandlerAdapter {
	
	private String userCloudStorage;
	
	private SqlUsersDaoService sqlUsersDaoService;
	
	public MainHandler() {
		sqlUsersDaoService = new SqlUsersDaoService("jdbc:sqlite:my_cloud_store_server.db", "org.sqlite.JDBC");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {		
		try {
			if (msg == null) {
				return;
			}
			if(msg instanceof AuthentificationMessage) {
				authMessageHandler(ctx, msg);
			}
			if (msg instanceof FileOperationsMessage) {
				fileOperationMessageHandler(ctx, msg);
			}

			if (msg instanceof FileParametersListMessage) {
				sendListOfFileParameters(ctx);
			}
			if (msg instanceof FileMessage) {
				FileMessage fileMessage = (FileMessage) msg;
				Files.write(Paths.get(userCloudStorage + fileMessage.getFilename()), fileMessage.getData(),
						StandardOpenOption.CREATE);
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	private void fileOperationMessageHandler(ChannelHandlerContext ctx, Object msg) throws IOException {
		FileOperationsMessage fileOperationsMessage = (FileOperationsMessage) msg;
		switch (fileOperationsMessage.getFileOperation()) {
		case COPY:
			copyToClientStorege(ctx, fileOperationsMessage);
			break;
		case MOVE:
			copyToClientStorege(ctx, fileOperationsMessage);
			deleteFileFromCloudStorage(ctx, fileOperationsMessage);
			break;
		case DELETE:
			deleteFileFromCloudStorage(ctx, fileOperationsMessage);
			break;
		default:
			break;
		}
	}

	private void authMessageHandler(ChannelHandlerContext ctx, Object msg) {
		AuthentificationMessage authMessage = (AuthentificationMessage) msg;
		switch(authMessage.getAuthCommandType()) {
		case AUTHORIZATION:
			if (sqlUsersDaoService.authentification(authMessage.getLogin(), authMessage.getPassword())) {
				authMessage.setStatus(true);
				userCloudStorage = authMessage.getLogin() + "Storage/";
				ctx.writeAndFlush(authMessage);
			} else {
				ctx.writeAndFlush(authMessage);
			}
			break;
		case CHANGE_PASS:
			if(sqlUsersDaoService.changePass(authMessage.getLogin(), authMessage.getPassword(),
					authMessage.getNewPassword())) {
				authMessage.setStatus(true);
				ctx.writeAndFlush(authMessage);
			} else {
				ctx.writeAndFlush(authMessage);
			}
			break;
		case DELETE_USER:
			if(sqlUsersDaoService.authentification(authMessage.getLogin(), authMessage.getPassword())) {
				authMessage.setStatus(true);
				sqlUsersDaoService.deleteUserByName(authMessage.getLogin());
				userCloudStorage = authMessage.getLogin() + "Storage/";
				deleteUsersCloudStorege(userCloudStorage);
				ctx.writeAndFlush(authMessage);
			} else {
				ctx.writeAndFlush(authMessage);
			}
			break;
		case REGISTRATION:
			if(sqlUsersDaoService.selectUserByName(authMessage.getLogin()) == null) {
				authMessage.setStatus(true);
				sqlUsersDaoService.insertUser(authMessage.getLogin(), authMessage.getPassword());
				ctx.writeAndFlush(authMessage);				
			} else {
				ctx.writeAndFlush(authMessage);
			}
			break;
		default:
			break;
		}
	}

	private void deleteUsersCloudStorege(String userCloudStorage) {
		Path userStoragePath = Paths.get(userCloudStorage);
		try {
			Files.walk(userStoragePath)
			.sorted(Comparator.reverseOrder())
			.peek(System.out::println)
			.map(Path::toFile)			
			.forEach(File::delete);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteFileFromCloudStorage(ChannelHandlerContext ctx, FileOperationsMessage fileOperationsMessage)
			throws IOException {
		Path path = Paths.get(userCloudStorage + fileOperationsMessage.getFileName());
		Files.delete(path);
		sendListOfFileParameters(ctx);
	}


	private void sendListOfFileParameters(ChannelHandlerContext ctx) {
		File directory = new File(userCloudStorage);
		
		if(!directory.exists()) {
			directory.mkdir();			
		}
		FileParametersListMessage fileParametersList = new FileParametersListMessage(userCloudStorage);
		ctx.writeAndFlush(fileParametersList);
	}


	private void copyToClientStorege(ChannelHandlerContext ctx, FileOperationsMessage fileOperationsMessage)
			throws IOException {
		Path path =  Paths.get(userCloudStorage + fileOperationsMessage.getFileName());
		FileMessage fileMessage = new FileMessage(path);
		ctx.writeAndFlush(fileMessage);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
