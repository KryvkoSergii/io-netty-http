package io.netty.http;
 
import java.util.Date;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
 
public class ServerHTTPHandler3 extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	//	contain IP address of Client
	private static String ClientIpAddr;
	//	contain received bytes
	private static int receivedByte;
	//	contain sent bytes
	private static int sentByte;
	//	contain date of request
	private static Date timeStamp;
	//	contain URI
	private static String redirectUri;
	//	library 
	private static tagLib tg = new tagLib();
	//	time of request
	private static long initTime;
	//	time of response	
	private static long respTime;
	//	response speed
	private static int speed;
	//	number of current connection
	private static int link;
	
	//	parameters of mySql connection 
	private static String name;
	private static String password;
	private static String database;
	private static String table;
	
	ServerHTTPHandler3(String name, String password, String database, String table, int link)
	{
		this.name = name;
		this.password = password;
		this.database = database;
		this.table = table;
		this.link = link;
	}
	
	 public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		 	final String uri = request.getUri();
		 	initTime = System.currentTimeMillis();
		 	StringBuilder s = new StringBuilder(); 
			s.append(String.valueOf(request.getMethod())+String.valueOf(request.getProtocolVersion())+String.valueOf(request.getUri()));
			receivedByte = s.length();
		
		 	if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
         	return;
         	}
         
         	if (request.getMethod() != HttpMethod.GET) {
        	sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
		 	return;
		 	}
         	
		 	if (request.getMethod().equals(HttpMethod.GET))
		 	{
		 		sendListing(ctx,uri);
		 	}
 
			}
 
			 
 private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
         FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
         response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        
        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
   }
 
 
 private static void sendListing(ChannelHandlerContext ctx, String uri) throws Exception 
 {
	 FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	 response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
	 BaseSQL base = new BaseSQL( name,password, "jdbc:mysql://localhost/"+database,table);
	 
	 //	 get client IP from remoteAddress() and convert into an acceptable form (without request id)
	 String k = ctx.channel().remoteAddress().toString();
	 ClientIpAddr = k.substring(1,k.indexOf(":"));
	 
	 // StringBuilder buf contains response
	 StringBuilder buf = new StringBuilder();
	 Character s = new Character('"');
	 int choose = parsingUri(uri);
	 //create http code depending of incoming request
	 if (choose==2){
		 //		 gets number of general request
		 String p = base.getGeneralNumbOFRow();
		 //		 gets number of unique request
		 String d = base.getUniqueRequest();
		 //		 gets table of redirection request
		 String[][] redirectTable = base.getRedirectTable();
		 //		 gets table of request with IP addresses and number of requests
		 String[][] IPTable = base.getIPTable();
		 //		 gets table of last 16 requests
		 String[][] statisticTable = base.getStatisticTable();
		 
		 buf.append(tg.tagDocumHead());
		 buf.append(tg.tagHtml(true));
		 buf.append(tg.tagHead(true));
		 buf.append(tg.tagTitle(true)+"Status"+tg.tagTitle(false));
		 buf.append(tg.tagHead(false));
		 buf.append(tg.tagBody(true));
		 buf.append(tg.tagTable(true));
		 buf.append(tg.tagAddRowToTable("Requests in general:", p, null));
		 buf.append(tg.tagAddRowToTable("Unique requests:", d, null));
		 
		 buf.append(tg.tagTable(false));
		 buf.append(tg.tagWriteText("Redirect:"));
		 
		 //creates redirect table 		 
		 buf.append(tg.tagTable(true));
		 buf.append(tg.tagAddRowToTable("Links", "Quantity", null));
		 for (String[] a: redirectTable) 
		 {
			 buf.append(tg.tagAddRowToTable(a[0],a[1], null));	
		 }
		 buf.append(tg.tagTable(false));
		 
		 buf.append(tg.tagWriteText("Requests count:"));
		 buf.append(tg.tagTable(true));
		 buf.append(tg.tagAddRowToTable("IP", "Quantity", "Date of last request"));
		 for (String[] a: IPTable) 
		 {
			 buf.append(tg.tagAddRowToTable(a[0],a[1],a[2]));	
		 }
		 buf.append(tg.tagTable(false));
		 
		 buf.append(tg.tagWriteText("Currect connection"));
		 buf.append(tg.tagTable(true));
		 buf.append(tg.tagAddRowToTable("Current connection:",String.valueOf(link), null));
		 buf.append(tg.tagTable(false));
		 buf.append(tg.tagWriteText(" "));
		 
		 buf.append(tg.tagWriteText("Statistic:")); 
		 buf.append(tg.tagTable(true));
		 buf.append(tg.tagAddRowToFullTable("ip_add", "uri", "time_stamp", "sent, byte", "received, byte", "speed, byte/sec"));
		 for (String[] a: statisticTable) 
		 {
			 buf.append(tg.tagAddRowToFullTable(a[0], a[1], a[2], a[3], a[4], a[5]));	
		 }
		 buf.append(tg.tagTable(false));
		
		 buf.append(tg.tagBody(false));
		 buf.append(tg.tagHtml(true));
	 }
	 
	 if (choose==1)
	 {
		 //		 creates http respond of hello world
		 try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
		 buf.append(tg.tagDocumHead());
		 buf.append(tg.tagHtml(true));
		 buf.append(tg.tagHead(true));
		 buf.append(tg.tagTitle(true)+"<<Hello World>>"+tg.tagTitle(false));
		 buf.append(tg.tagHead(false));
		 buf.append(tg.tagBody(true)+tg.tagWriteText(" &lt &lt Hello World &gt &gt")+tg.tagBody(false));
		 buf.append(tg.tagHtml(true));
 
	 }
	 
	 if (choose==3)
	 {
		 //		 creates http respond of redirection
		 buf.append(tg.tagDocumHead());
		 buf.append(tg.tagHtml(true));
		 buf.append(tg.tagMetaData(redirectUri));
		 buf.append(tg.tagHtml(false));
	 }
	 if (choose==4)
	 {
		 sendError(ctx,HttpResponseStatus.BAD_REQUEST);
	 }
	 
     ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
     //     writes buffer with http to respond
     response.content().writeBytes(buffer);
     
     // gets bytes number of response
     sentByte = buffer.writerIndex();
     buffer.release();
     
     // Close the connection as soon as the error message is sent.
     ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
     //     response time calculation
     try{
     speed =  (int) (sentByte*1000/(System.currentTimeMillis()-initTime));
     } catch (ArithmeticException e)
     {
    	 e.printStackTrace();
     }
 
     
     // write to base 
     if (!(sentByte==0)){
     base.Add(ClientIpAddr, redirectUri, sentByte, receivedByte, speed , true);
     }
     base.Close();
     }
 
private static Integer parsingUri(String uri) throws Exception {
	// 	Address example	 http://somedomain/redirect?url=<url>
	 // method parse incoming URI and define, which page should be sent
	 QueryStringDecoder decoderUri = new QueryStringDecoder(uri);
	 //	 if user select <status> application prepare status page 
	 if (decoderUri.path().equals("/status"))
	 {
		 redirectUri="/status";
		 return 2;
	 }
	 //	 if user select <hello> application send Hello page
	 else if (decoderUri.path().equals("/hello"))
	 {
		 redirectUri="/hello";
		 return 1;
	 }
	 //	 if user select <redirect> application send redirect URI
	 else if (decoderUri.path().equals("/redirect"))
	 {
		 redirectUri = decoderUri.parameters().get("url").get(0).trim();
		 return 3;
	 }
	 else {
		 return 4;
	 }
	 
 }
 
	
}
