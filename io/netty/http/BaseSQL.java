package io.netty.http;
import java.sql.*;
 
public class BaseSQL {
		/** Class contains SQL request*/
		
		private String ErrorSQL;
		private Connection conn;
		private String tableName;
		private Statement stat;
		
		//		CREATE TABLE DATA(id_request INT(10) PRIMARY KEY AUTO_INCREMENT, ip_add TINYTEXT, uri TINYTEXT, time_stamp DATE, send_bytes INT(10), received_bytes INT(10), speed INT(10), state BOOLEAN);
 
 
		BaseSQL(String userName,String password,String url, String tableName)
		{
		try{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url,userName,password);
		this.tableName=tableName;
		stat = conn.createStatement();
		} 
		catch(Exception e) {ErrorSQL = e.toString();}
		}
		
		public void createTable()
		/** creates table*/
		{
			try {
			stat.executeUpdate("CREATE TABLE "+tableName+"(id_request INT(10) PRIMARY KEY AUTO_INCREMENT, ip_add TINYTEXT, uri TINYTEXT, time_stamp DATE, send_bytes INT(10), received_bytes INT(10), speed INT(10), state BOOLEAN)");
			stat.clearBatch();
			} 
			catch(Exception e) {ErrorSQL = e.toString();}
		}
 
		
		//		adds record to table
		public void Add(String ip_add,String uri, int send_bytes, int received_bytes,int speed, boolean status) 
		{
			try {
			stat.executeUpdate("Insert INTO "+tableName+" VALUES("+null+","+"\""+ip_add+"\""+","+"\""+uri+"\""+","+null+","+send_bytes+","+received_bytes+","+speed+","+status+")");
			stat.clearBatch();
			} 
			catch(Exception e) {ErrorSQL = e.toString();}
		}
		
		//		gets general number of request
		public String getGeneralNumbOFRow()
		{
			String result;
			try{			
				ResultSet out = stat.executeQuery("SELECT COUNT(*) FROM "+tableName);
				out.first();
				result=out.getString("count(*)");
				stat.clearBatch();
				return result;
				} 
			catch(Exception e) {
				ErrorSQL = e.toString();
				result=null;
				return null;
				}
		}
 
		public String getUniqueRequest()
		/** gets number of unique request*/
		{
			String result;
			try{
				ResultSet out = stat.executeQuery("select count(distinct uri) from "+ tableName);
				out.first();
				result=out.getString("count(distinct uri)");
				stat.clearBatch();
				return result;
				} 
			catch(Exception e) {ErrorSQL = e.toString(); System.out.println(ErrorSQL);return null;}
		}
		
		public String[][] getStatisticTable()
		/** return table of redirection as string array*/
		{
			String[][] K;
			try
			{
				ResultSet out = stat.executeQuery("SELECT  ip_add, uri, time_stamp, send_bytes, received_bytes, speed from "+tableName+" order by time_stamp Desc limit 16");
				K = getTable(out);
				stat.clearBatch();
				return K;
			} catch(SQLException e) 
				{
				ErrorSQL = e.toString(); 
				return null;
				}
		}
		
		public String[][] getRedirectTable()
		/** return table of redirection as string array*/
		{
			String[][] K;
			try
			{
				ResultSet out = stat.executeQuery("SELECT uri, count(*) cs FROM "+tableName+" GROUP BY uri");
				K = getTable(out);
				stat.clearBatch();
				return K;
			} catch(SQLException e) 
				{
				ErrorSQL = e.toString(); 
				return null;
				}
		}
		
		public String[][] getIPTable()
		/** return table of IP as string array*/
		{
			String[][] K;
			try
			{
				ResultSet out = stat.executeQuery("SELECT ip_add, count(*) cs, MAX(time_stamp) from "+ tableName +" group by ip_add");
				K = getTable(out);
				stat.clearBatch();
				return K;
			} catch(SQLException e) 
				{
				ErrorSQL = e.toString(); 
				return null;
				}
		}
	
 
		private String[][] getTable(ResultSet out)
		/** return table of ResultSet as string array */
		{
			try{
			String[] columnsName = getColumnNameFromMetaData(out);
			int rowsNumber = getNumberOfRows(out);
//			K contains ResultSet as array
			String[][] K = new String[rowsNumber][columnsName.length];
			
			out.first();
			for (int i=0;i<rowsNumber;i++)
			{
				for (int j=0;j<columnsName.length;j++)
				{
					K[i][j]=out.getString(columnsName[j]);
				}
				out.next();
			}
			stat.clearBatch();
			return K;
			} catch(SQLException e) 
			{
				ErrorSQL = e.toString(); 
				return null;
			}
							
		}
		
		public void Close() {
			try {
			stat.close();
			conn.close();	
			} 
			catch(Exception e) {ErrorSQL = e.toString();}
			}
 
		public String getErrorSQL() {
			return ErrorSQL;
		}
		
		
		private String[] getColumnNameFromMetaData(ResultSet rs)
		/** return String array of columns names from ResultSet meta data */
		{
			String[] result;
			try {
				int columnCount = rs.getMetaData().getColumnCount();
				result = new String[columnCount];
				for (int i=0;i<columnCount; i++)
				{
					result[i] = rs.getMetaData().getColumnName(i+1);
				}
				
			} catch (SQLException e) {
				ErrorSQL = e.toString();
				result = null;
			}
			return result;
		}
		
		private int getNumberOfRows(ResultSet rs)
		/** Return number of rows in ResultSet*/
		{
			try{
			rs.first();
			int i=1;
			while (rs.next()==true) {
				i++;			
			} 
			return i;
			} catch (SQLException e) {
			return 0;
			}
		}
	
}
