<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/dwr/interface/DWRMRSPatientTypeService.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/engine.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/util.js" ></openmrs:htmlInclude>


<script type="text/javascript">
<!--

function getMessageTypeDWR()
{
	var patientType = document.getElementById("pType").value;
	//alert(patientType);
	
	DWRMRSPatientTypeService.getPatientType(patientType,typeResults);
		
}

 function typeResults(patients)
{
	  for(var i=0;i<patients.length;i++)
	 {
		 var text = patients[i].text;
		 if(text == "")
			 {
			 document.getElementById("txtMsg").value="";
			 }
		 else{
		document.getElementById("txtMsg").value=text;
		 }
		 
	 }
}
 function getPatientSearch()
 {
 	var pid = document.getElementById("patientid").value;
 	
 	DWRMRSPatientTypeService.patIdentifierSearch(pid, retunob);
 }
 
 function retunob(wat){
	 alert(wat);
 }

/*    var cellFunctions =
       [
         function(patients) { return patients.patientId; },
         function(patients) { return patients.gender; },
         function(patients) { return patients.givenName; },
         function(patients) { return patients.mobileNumber; },
         
       ]; */

function validateCellNum(cellnum){
	var reg = /^[\+]?([0-9]{3,20})$/;
	 if(reg.test(cellnum) == false) {
	      return false;
	 }
	 return true;
}

function verifyPatietnID()
{
	
	if(pid=="")
		{
		alert("Please enter Patient ID");
		}
}
function addCellNumberToSendList(){
	var cellnum=document.getElementById("ipcellnumber").value;
	if(validateCellNum(cellnum)){
		if(cellnum!=""){
			if(!findDuplicate(cellnum)){
				addToListBox(cellnum, cellnum);
			}else{
				alert('Number already exists in list');
			}
			
	        document.getElementById("ipcellnumber").value="";
		}
	}else{
    	alert("Invalid Cell Number");
	}
	
}

function addToListBox(text,value){
	var opt = document.createElement("option");
   
    opt.text = text;
    opt.value = value;
    // Add an Option object to Drop Down/List Box
    try{
    document.getElementById("cellnum").options.add(opt,null);//stndrd
    }catch (e) {
    	document.getElementById("cellnum").options.add(opt);//ie only
	}
}
function deleteCellNum(){
	var cellnumss=document.getElementById("cellnum");
	
	for (i=0; i<cellnumss.options.length; i++) {
		
		//if (cellnumss.options[i].selected) {
			try{
				cellnumss.remove(i,null);//stndrd
		        }catch (e) {
		        	cellnumss.remove(i);//ie only
				};
				i--;
	    //}
	}
	
}

function choosePatients(){
	showChoosePatientTemplate();
}
function chooseByTreatmentStatus(obj){
	DWRPatientService.getPatientsWithTreatmentStatus(obj.value,processPatientsReturned);
	obj.checked=false;
}
var processPatientsReturned = function(patients) {
	clearDiv();
	clearmsgDiv();
	if(patients!=null){
		if(patients.length==0){
			showError("No Patients Returned...Check log for details if patients exist but not retrieved..");
		}else{
			for(var i=0;i<patients.length;i++){
				var text=patients[i].patientId+" : "+patients[i].firstName+" : "+patients[i].cellNoLatest;
				if(!findDuplicate(patients[i].cellNoLatest)){
					addToListBox(text, patients[i].cellNoLatest);
				}else{
					document.getElementById("duplicateNums").innerHTML+=text+"<br>";
				}
			}
		}
	showSendMsgTemplate();
	}else{
		window.location='login.htm?logmessage=Session expired. Please login again';
	}
};
var processPatErrorMessage=function(result){
	showSendMsgTemplate();
	showError(result);
};
function showSendMsgTemplate(){
	document.getElementById("sendMsgTemplate").style.display="table";
	document.getElementById("choosePatientTemplate").style.display="none";
}
function showChoosePatientTemplate(){
	document.getElementById("sendMsgTemplate").style.display="none";
	document.getElementById("choosePatientTemplate").style.display="table";
}
function findDuplicate(cellNumber){
	
	var cellnums=document.getElementById("cellnum");
	
	for (var i=0; i<cellnums.options.length; i++) {
		if (cellnums.options[i].value.substring(cellnums.options[i].value.length-10)==cellNumber.substring(cellNumber.length-10)) {
			return true;
	    }
	}
	return false;
	
}
function clearDiv() {
	document.getElementById("duplicateNums").innerHTML="";
}
function sendSMSs() {
	var msg=document.getElementById("txtMsg").value;
	getPatientSearch();
	
	//var hrs=document.getElementById("txbdueHours").value;
	//var descr=document.getElementById("txtDescr").value;
	/* if(hrs > 23 || hrs < 0){
		alert('Please enter due date hours between 0 to 23');
		return;
	} */
	if(trim(msg)!=''){
		var cellnumlist=[];
		var cellnumoptions=document.getElementById("cellnum");
		var pid=document.getElementById("patientid").value;
		if(cellnumoptions.options.length != 0){
			if(pid!='')
				{
								
			for(i=0;i<cellnumoptions.options.length;i++){
				cellnumlist[i]=cellnumoptions.options[i].value;
			}//alert(cellnumlist[0]+" "+cellnumlist[1]);
			
			for(var count=0; count < cellnumoptions.options.length; count++) {
				cellnumoptions.options[count].selected = true;
			}
			document.getElementById("frm").submit();
				}
			 else
				{
				alert("Please enter PatientID");
				}
		}else{
			alert('Please enter cell numbers to send message');
		}

	}else{
		
		alert('Please enter an appropriate message text');
		return false;
	}
}
var processSendMsgResultReturned=function(result){
	if(result==null){
		window.location='login.htm?logmessage=Session expired. Please login again';
		return;
	}
	document.getElementById("txtMsg").value="";
	document.getElementById("txtDescr").value="";
	showError(result);
};
function clearCellNumList(){
	var cellnums=document.getElementById("cellnum");
	for (i=0; i<cellnums.options.length; i++) {
			try{
				cellnums.remove(i,null);//stndrd
		        }catch (e) {
		        	cellnums.remove(i);//ie only
				};
				i--;
	}
}
function trim(str) {
	var	str = str.replace(/^\s\s*/, ''),
		ws = /\s/,
		i = str.length;
	while (ws.test(str.charAt(--i)));
	return str.slice(0, i + 1);
}
function clearmsgDiv() {
	document.getElementById("divSendMsgResult").innerHTML="";
	document.getElementById("divError").style.display="none";
}
function showError(errormsg) {
	document.getElementById("divError").style.display="table";
	document.getElementById("divSendMsgResult").innerHTML=errormsg;
}
//-->
</script>

<%-- <p>Hello ${user.username}!</p>
<p>Hello ${user.userId}!</p>
<p>Hello ${user.systemId}!</p>
<p>Hello ${user.person.gender}!</p>
 --%>
<form id="frm" method="post">
	<table>
		<tr>
			<td>Enter SMS text here..</td>
			<td><span style="padding-left: 1cm;color:blue;font-weight:bold">${messageText}</span></td>
		</tr>
		<tr>
		<td>Select Patient Type:&nbsp;&nbsp;
					<select name="pType" id="pType" onclick="getMessageTypeDWR();">
					<option>-------Selection-------</option>
					<option>TB</option>
            		<option>HIV</option>
            		<option>Poliomyelitis</option>
            		
            		</select>
		</td>
		</tr>
		<tr>
			<td valign="top">
				<div>
					
					
					<textarea id="txtMsg" name="txtMsg" rows="10" cols="50"></textarea>
					<br>
			<input type="button" value="SEND" style="width: 30mm; height: 10mm;"
						onclick="sendSMSs();"/>
				</div>
			</td>
			
			
			
			<td><div style="padding-left: 1cm;">Enter Patient Identifier</div>  
			<div style="padding-left: 1cm;"><input id="patientid" name="patientid" type="text" style="width: 60mm"/> 
			
			</div>
			<br>
				<div style="padding-left: 1cm;">
				
					<input id="ipcellnumber" type="text" style="width: 60mm" />
					<input type="button" value="Add" style="font-size: x-small; width: 20mm" onclick="addCellNumberToSendList();" /> 
					<select id="cellnum" 
						name="cellnum" multiple="multiple"
						style="border-color: black; border-width: thin; height: 300px; width: 85mm; border-style: none; overflow: visible; display: list-item;">
					</select>
		</div> 
				
			</td>
			
		</tr>
		  
		<tr>
		<td></td>
		<td>
		<div align="right">
 	 <input type="button" style="width: 20mm;" value="Remove" onclick="deleteCellNum();"/>
		</div>  

		</td>

	</tr>
	</table>
	
</form>
<%@ include file="outboundResults.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp"%>