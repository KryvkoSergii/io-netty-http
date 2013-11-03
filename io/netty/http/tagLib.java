package io.netty.http;
 
public class tagLib {
	/** Class contains http tags to provide composition of http page.
	 */
 
	public String tagDocumHead()
	//	add tag <!DOCTYPE html>
	{
		return "<!DOCTYPE html>"+'\n';
	}
	
	public String tagMetaData(String link)
	{
		//		add tag <meta...>
		return "<meta http-equiv="+'"'+"refresh"+'"'+ "content="+'"'+"1; url=http://"+link+'"'+">";
	}
	
	public String tagHtml(boolean isOpen)
	{
		//	add tag <html>
	if (isOpen==true){return "<html>"+'\n';} 
	//	add tag </html>
	if (isOpen==false) {return "</html>"+'\n';}
	return null;
	}
	
	public String tagHead(boolean isOpen)
	{
		//	add tag <head>
	if (isOpen==true){return "<head>"+'\n';} 
	//	add tag </head>
	if (isOpen==false) {return "</head>"+'\n';}
	return null;
	}
	
	public String tagTitle(boolean isOpen)
	{
		//	add tag <title>
	if (isOpen==true){return "<title>"+'\n';} 
	//	add tag </title>
	if (isOpen==false) {return "</title>"+'\n';}
	return null;
	}
	
	public String tagBody(boolean isOpen)
	{
		//	add tag <body>
	if (isOpen==true){return "<body>"+'\n';} 
	//	add tag </body>
	if (isOpen==false) {return "</body>"+'\n';}
	return null;
	}
	
	public String tagWriteText(String text)
	{
		//	add tag <b> Text </b>
	return "<b>"+text+"</b>"+'\n';
	}
	
	public String tagTable(boolean isOpen)
	{
		//	add tag <table>
	if (isOpen==true){return "<table border="+'"'+"1"+'"'+">"+'\n';}
	//	add tag </table>
	if (isOpen==false) {return "</table>"+'\n';}
	return null;
	}
	
	public StringBuilder tagAddRowToTable(String text1,String text2,String text3)
	{
		/**
		<tr align="left" >
		<td width="220">text1</td>
		<td width="50">text2</td>
		<td width="50">text3</td>
		</tr>
		 */
		StringBuilder a=new StringBuilder();
		a.append("<tr align="+'"'+"left"+'"'+" >"+'\n');
		a.append("<td width="+'"'+"220"+'"'+">"+text1+"</td>"+'\n');
		a.append("<td width="+'"'+"100"+'"'+">"+text2+"</td>"+'\n');
		if (!(text3==null))
		{
			a.append("<td width="+'"'+"100"+'"'+">"+text3+"</td>"+'\n');
		}
		a.append("</tr>"+'\n');
		return a;
	}
	
	public StringBuilder tagAddRowToFullTable(String text1,String text2,String text3,String text4,String text5,String text6)
	{
		/**
		<tr align="left" >
		<td width="220">text1</td>
		<td width="50">text2</td>
		<td width="50">text3</td>
		</tr>
		 */
		StringBuilder a=new StringBuilder();
		a.append("<tr align="+'"'+"left"+'"'+" >"+'\n');
		a.append("<td width="+'"'+"220"+'"'+">"+text1+"</td>"+'\n');
		a.append("<td width="+'"'+"100"+'"'+">"+text2+"</td>"+'\n');
		a.append("<td width="+'"'+"100"+'"'+">"+text3+"</td>"+'\n');
		a.append("<td width="+'"'+"100"+'"'+">"+text4+"</td>"+'\n');
		a.append("<td width="+'"'+"100"+'"'+">"+text5+"</td>"+'\n');
		a.append("<td width="+'"'+"100"+'"'+">"+text6+"</td>"+'\n');
		a.append("</tr>"+'\n');
		return a;
	}
	
}
