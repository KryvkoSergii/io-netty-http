package io.netty.http;
 
import java.util.Map;
 
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
 
public class ServerHTTP {
/** The main class of HTTP server. Initializes the server by port,
 *  receives incoming requests from clients, creates threads of request handlers
 *  and register them.
 */
 
	private int port;
	//	parameters of mySql server establishing
	private static Map config;
 
	    
	    public ServerHTTP(int port) {
	        this.port = port;
	    }
	    
	    public void run() throws Exception {
	    	
	    	//	registration of incoming connections
	        EventLoopGroup bossGroup = new NioEventLoopGroup();
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        //	creating channels group to calculate current number of connection in processing
	        final DefaultChannelGroup allChannels  = new DefaultChannelGroup("netty-receiver", null);
	        
	       try {
	    	   //	set up server
	            ServerBootstrap b = new ServerBootstrap(); // (2)
	            b.group(bossGroup, workerGroup)
	             .channel(NioServerSocketChannel.class) // (3)
	             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)     	 
	                 @Override
	                 public void initChannel(SocketChannel ch) throws Exception {
	                	 allChannels.add(ch.pipeline().channel());
		                 int link = allChannels.size(); 
		                 //	adds decoder to decode incoming request
	                	 ch.pipeline().addLast("decoder", new HttpRequestDecoder());
	                	 //	aggregates HttpMessage and its following HttpContents into a single HttpMessage with no following HttpContents
	                	 ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
	                	 //	encodes respond of server
	                	 ch.pipeline().addLast("encoder", new HttpResponseEncoder());
	                	 //	add handler for operating request and preparing respond 
	                	 // 	gives parameters of mySql server establishing to ServerHTTPHandler3 constructor
	                	 //  	also gives number of current connections to ServerHTTPHandler3 constructor
	                	 ch.pipeline().addLast("handler", new ServerHTTPHandler3(config.get("user").toString(),config.get("password").toString(),config.get("dataBase").toString(),config.get("table").toString(), link));
	                	                	 
	              }
	             })
	             //	specifying channel options
	             .option(ChannelOption.SO_BACKLOG, 128)          
	             .childOption(ChannelOption.SO_KEEPALIVE, true); 
	            	    
	            // Bind and start to accept incoming connections.
	            ChannelFuture f = b.bind(port).sync();
 
	            // Wait until the server socket is closed.
	            // In this example, this does not happen, but you can do that to gracefully
	            // shut down your server.
	            f.channel().closeFuture().sync();
	           
	        } finally {
	            workerGroup.shutdownGracefully();
	            bossGroup.shutdownGracefully();
	        }
	    }
	    
	    public static void main(String[] args) throws Exception {
	        int port;
	        if (args.length > 0) {
	            port = Integer.parseInt(args[0]);
	        } else {
	            port = 8080;
	        }
	        //	creating XMLWriterReader, what reads parameters of mySql server establishing from config.xml file 
	        config = new XMLWriterReader("config.xml").getParameters();
	        new ServerHTTP(port).run();
	        
 
	    }
	}
