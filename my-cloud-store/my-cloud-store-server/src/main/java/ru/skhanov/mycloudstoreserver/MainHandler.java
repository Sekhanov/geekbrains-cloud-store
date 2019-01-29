package ru.skhanov.mycloudstoreserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.skhanov.mycloudstorecommon.AuthentificationMessage;
import ru.skhanov.mycloudstorecommon.FileMessage;
import ru.skhanov.mycloudstorecommon.FileOperationsMessage;
import ru.skhanov.mycloudstorecommon.FileParametersListMessage;

public class MainHandler extends ChannelInboundHandlerAdapter {
	
	private static final String CLOUD_STORAGE = "server_storage/";
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
				AuthentificationMessage authMessage = (AuthentificationMessage) msg;
				
				switch(authMessage.getAuthCommandType()) {
				case AUTHORIZATION:
					if (sqlUsersDaoService.authentification(authMessage.getLogin(), authMessage.getPassword())) {
						authMessage.setStatus(true);
						ctx.writeAndFlush(authMessage);
					} else {
						ctx.writeAndFlush(authMessage);
					}
					break;
				case CHANGE_PASS:
					break;
				case DELETE_USER:
					break;
				case REGISTRATION:
					break;
				default:
					break;
				
				}
			}
			if (msg instanceof FileOperationsMessage) {
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

			if (msg instanceof FileParametersListMessage) {
				sendListOfFileParameters(ctx);
			}
			if (msg instanceof FileMessage) {
				FileMessage fileMessage = (FileMessage) msg;
				Files.write(Paths.get(CLOUD_STORAGE + fileMessage.getFilename()), fileMessage.getData(),
						StandardOpenOption.CREATE);
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}


	private void deleteFileFromCloudStorage(ChannelHandlerContext ctx, FileOperationsMessage fileOperationsMessage)
			throws IOException {
		Path path = Paths.get(CLOUD_STORAGE + fileOperationsMessage.getFileName());
		Files.delete(path);
		sendListOfFileParameters(ctx);
	}


	private void sendListOfFileParameters(ChannelHandlerContext ctx) {
		FileParametersListMessage fileParametersList = new FileParametersListMessage(CLOUD_STORAGE);
		ctx.writeAndFlush(fileParametersList);
	}


	private void copyToClientStorege(ChannelHandlerContext ctx, FileOperationsMessage fileOperationsMessage)
			throws IOException {
		Path path =  Paths.get(CLOUD_STORAGE + fileOperationsMessage.getFileName());
		FileMessage fileMessage = new FileMessage(path);
		ctx.writeAndFlush(fileMessage);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
