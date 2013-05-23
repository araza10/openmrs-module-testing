package org.irdresearch.smstarseel;

//import ird.xoutTB.emailer.exception.EmailException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.irdresearch.smstarseel.context.TarseelContext;*/
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.App_Service;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseCode;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseMessage;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelGlobal;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelResponse;
//import org.irdresearch.smstarseel.handler.CallLogHandler;
import org.irdresearch.smstarseel.handler.InitDataHandler;
import org.irdresearch.smstarseel.handler.LoginHandler;
import org.irdresearch.smstarseel.handler.ReceiveFileHandler;
import org.irdresearch.smstarseel.handler.SmsHandler;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.StringUtils;

public class SmsTarseel extends HttpServlet {

	private static Socket echoSocket = null;
	private static  Thread closeSocketOnShutdown;

	private static Date lastPortErrorEmailDate = new Date(1211111111111L);
	/*Thread t = new Thread(new Runnable() {
	
	@Override
	public void run() {
		try {
            Boolean end = false;
            ServerSocket ss = new ServerSocket(38300);
            while(!end){
                    //Server is waiting for client here, if needed
                    Socket s = ss.accept();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter output = new PrintWriter(s.getOutputStream(),true); //Autoflush
                    String st = input.readLine();
                    output.println("Good bye and thanks for all the fish :)INPUT:"+st);
                    s.close();
                   
                    //if ( STOPPING conditions){ end = true; }
            }
            
            ss.close();
	    } 
	    catch (UnknownHostException e) {
	            e.printStackTrace();
	    } 
	    catch (IOException e) {
	            e.printStackTrace();
	    }

	}
});

t.start();
return true;*/

	@Override
	public void init() throws ServletException 
	{
		Properties prop;
		FileInputStream f;
		
		try {
			/*f = new FileInputStream(getServletContext().getRealPath("/WEB-INF/smstarseel.properties"));
			prop=new Properties();
			prop.load(f);
			TarseelContext.setProperties(prop);*/
			
			int maxFetchPerGo = 6;
			int maxDuplicateBoundary = 2;
			int maxSmsBoundary = 6;
			int maxSmsReverts = 6;
			
			/*try{
				maxFetchPerGo = Integer.parseInt(TarseelContext.getProperties().getProperty("outbound.max-fetch-per-go", "6"));
				maxDuplicateBoundary = Integer.parseInt(TarseelContext.getProperties().getProperty("outbound.spam-boundary.max-duplicate-allowed", "2"));
				maxSmsBoundary = Integer.parseInt(TarseelContext.getProperties().getProperty("outbound.spam-boundary.max-sms-allowed", "6"));
				maxSmsReverts = Integer.parseInt(TarseelContext.getProperties().getProperty("outbound.max-revert-tries", "6"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}*/
			
			TarseelWebGlobals.MAX_OUTBOUND_FETCH_PER_GO = maxFetchPerGo;
			TarseelWebGlobals.MAX_OUTBOUND_SPAM_DUPLICATE_BOUNDARY = maxDuplicateBoundary;
			TarseelWebGlobals.MAX_OUTBOUND_SPAM_SMS_ALLOWED_BOUNDARY = maxSmsBoundary;
			TarseelWebGlobals.MAX_OUTBOUND_REVERT_TRIES = maxSmsReverts;

			System.out.println("-MAX_OUTBOUND_FETCH_PER_GO"+maxFetchPerGo+"\n-MAX_OUTBOUND_SPAM_DUPLICATE_BOUNDARY"+maxDuplicateBoundary+"\n-MAX_OUTBOUND_SPAM_SMS_ALLOWED_BOUNDARY"+maxSmsBoundary+"\nMAX_OUTBOUND_REVERT_TRIES"+maxSmsReverts);
			
			//EmailEngine.instantiateEmailEngine(prop);
		} catch (Exception e) {
			e.printStackTrace();
		} /*catch (EmailException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		// TODO uncomment if doing via socket initSocket();
	}

	public static boolean setPort(String command)
	{
		// run the adb bridge
		try {
			Process p=Runtime.getRuntime().exec(command/*"C:\\android-sdk\\platform-tools\\adb.exe forward tcp:38300 tcp:38300"*/);
		
			Scanner sc = new Scanner(p.getErrorStream());
			if (sc.hasNext()) {
				while (sc.hasNext()) System.out.println(sc.next());
				
				System.out.println("Cannot start the Android debug bridge");
				return false;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION: "+e.toString());
			return false;
		}
		return true;
	}
	
/*	public static boolean initSocket() {

		System.out.println("EchoClient.main()");

		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				while (true)
				{
					try {
					if(echoSocket == null || echoSocket.isClosed())
					{
						PrintStream out = null;
						BufferedReader in = null;
						
						TarseelServices ts = TarseelContext.getServices();
						Setting set = ts.getSettingService().getSetting("android.command.port-forward");
						String command = set == null ? null : set.getValue();
						boolean portinited = false;
						if(command != null){
							portinited = setPort(command);
						}
						
						if(portinited){
							echoSocket = new Socket("localhost", 38300);
							out = new PrintStream(echoSocket.getOutputStream());
							in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
							
							System.out.println("connected!!");
							
							// add a shutdown hook to close the socket if system crashes or exits unexpectedly
							if(echoSocket != null && closeSocketOnShutdown == null){
								closeSocketOnShutdown = new Thread() {
								public void run() {
									System.out.println(new Date()+" Shutdown ran");
									try {
										echoSocket.shutdownInput();
									} catch (IOException e) {
										e.printStackTrace();
									}
									try {
										echoSocket.shutdownOutput();
									} catch (IOException e) {
										e.printStackTrace();
									}
									try {
										echoSocket.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								};
								
								Runtime.getRuntime().addShutdownHook(closeSocketOnShutdown);
							}
					//}
							//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
							// String userInput;
		
							while (true) {
									try {
										//out.println("client server: lets start"+new Date());
										//out.println(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG);
										
										String receivedinputmsg = null;
										String actualinputmessage = "";
										boolean readMore = true;
										while (readMore) {
											receivedinputmsg = in.readLine();
											System.out.println("receivedinputmsg: " + receivedinputmsg);
											
											if(receivedinputmsg == null || receivedinputmsg.endsWith(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG)){
												readMore = false;
											}
											
											if(receivedinputmsg != null){
												actualinputmessage += receivedinputmsg.replaceAll(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG, "");
											}
											
											System.out.println("actualinputmessage: " + actualinputmessage);
										}
										if(actualinputmessage != null && !actualinputmessage.equals("")){
											JSONObject jReq = SmsTarseelResponse.convertToJson(actualinputmessage);
										
											out.println(handleRequests(jReq, null));
											out.println(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG);
										}
										else {
											try {
												echoSocket.shutdownInput();
											} catch (IOException e) {
												e.printStackTrace();
											}
											try {
												echoSocket.shutdownOutput();
											} catch (IOException e) {
												e.printStackTrace();
											}
											try {
												echoSocket.close();
											} catch (IOException e) {
												e.printStackTrace();
											}

											break;
										}
										
										System.out.println("write and read successfully");
									} catch (IOException e) {
										//e.printStackTrace();
									} catch (JSONException e) {
										e.printStackTrace();
									} catch (Exception e) {
										e.printStackTrace();
									}
									finally{
										ts.closeSession();
									}
								}
							
							}
							else{
								if(DateUtils.differenceBetweenIntervals(new Date(), lastPortErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
									EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error port lost", "SmsTarseel: Error port lost. Port not able to be instantiated while trying adb");
									lastPortErrorEmailDate = new Date();
								}
								
								try {
									Thread.sleep(1000*60*1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						} 
						catch (UnknownHostException e) 
						{
							System.err.println("Don't know about host: localhost.");
							//System.exit(1);
						} 
						catch (IOException e) 
						{
							System.err.println("Couldn't get I/O for " + "the connection to: localhost.");
							//System.exit(1);
						}
						finally{
							
						}
				}
			}
		});
		
		t.start();
		return true;
	}*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException
	{
		doPost(req, resp);
	}

	private static void handleRequests(JSONObject jReq, HttpServletResponse response, FileItem file) throws IOException, JSONException
	{
		SmsTarseelUtil.PHONECOMMLOGGER.info("request:"+jReq.toString());
		String service = jReq.getString(App_Service.NAME);
			
		if (StringUtils.isEmptyOrWhitespaceOnly(service))
		{
			SmsTarseelUtil.sendResponse(response, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INAVLID_REQUEST, "").jsonToString());
		}
		else if (service.equalsIgnoreCase(App_Service.LOGIN.VALUE()))
		{
			LoginHandler.handleDeviceLogin(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.REGISTER_DEVICE_PROJECT.VALUE()))
		{
			LoginHandler.registerDevice(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.QUERY_PROJECTLIST.VALUE()))
		{
			InitDataHandler.getProjectList(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SEND_LOG.VALUE()))
		{
			ReceiveFileHandler.receiveLogFileData(jReq, response, file);
		}
		else if (service.equalsIgnoreCase(App_Service.FETCH_PENDING_SMS.VALUE()))
		{
			SmsHandler.getPendingSmsTillNow(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SUBMIT_SMS_SEND_ATTEMPT_RESULT.VALUE()))
		{
			SmsHandler.submitSmsSendAttemptResult(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SUBMIT_RECIEVED_SMS.VALUE()))
		{
			SmsHandler.submitRecivedSms(jReq, response);
		}
		else if (service.equalsIgnoreCase(App_Service.SUBMIT_CALL_LOG.VALUE()))
		{
			//CallLogHandler.submitCallLog(jReq, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException
	{
		/*String init = req.getParameter("sockinit");
		if(init!=null && init.equalsIgnoreCase("doit")){
			initSocket();
			return;
		}
		else if(init!=null && init.equalsIgnoreCase("portfwd")){
			setPort();
			return;
		}*/
		PrintWriter out=null;
		try
		{
			int sport=req.getServerPort();
			String ctype=req.getContentType();
			int clenght=req.getContentLength();
			String stringJson = "";
			FileItem file = null;
			boolean isMultipart = ServletFileUpload.isMultipartContent( req );//checking whether this has more content, if it has it'll come in chunks
			boolean isJsonRequest = true;
			out = resp.getWriter();  
			
			//TODO uncomment line below if using single request methods to communicate
			//SmsTarseelUtil.PHONECOMMLOGGER.info("multipart:"+isMultipart);

			if (isMultipart) {
				// Create a factory for disk-based file items
				FileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				List /* FileItem */ items = upload.parseRequest(req);
				
				Iterator iter = items.iterator();
				while ( iter.hasNext() ) {
					FileItem item = (FileItem) iter.next();

					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase( SmsTarseelGlobal.JSON_REQUEST_DATA_PARAM_NAME )) {
							stringJson = item.getString();
						}
					}
					else {
						isJsonRequest = true;
						stringJson = item.getFieldName();
						file = item;
					}
				}
			}
			else{
				byte[] b = new byte[req.getContentLength()];
				req.getInputStream().read(b);
				stringJson = new String(b);
			}
			
			//JSONObject jReq = SmsTarseelResponse.convertToJson(req.getInputStream(), req.getContentLength());
			JSONObject jReq = null ;
			if(isJsonRequest){
				jReq = SmsTarseelResponse.convertToJson(stringJson);

				try{
					handleRequests(jReq, resp, file);
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
//				catch (Exception e) {
//					SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
//				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			out.println("Error BIIG TIME!!");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
