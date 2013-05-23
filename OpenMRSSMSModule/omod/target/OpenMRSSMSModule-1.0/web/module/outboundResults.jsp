<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:htmlInclude file="/dwr/interface/DWRMRSReportTypeService.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/engine.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/util.js" ></openmrs:htmlInclude>

<script type="text/javascript">
var patielist;
function dwrReportRetrieval()
{
	
	var patientid =   document.getElementById("patient_id").value;
	
	var table_ik = document.getElementById("chav");
		
	for(var ij = table_ik.rows.length - 1; ij > 0; ij--)
	{
		table_ik.deleteRow(ij);
	}
		
	
		DWRMRSReportTypeService.getMessageByName(patientid, messageList);

}
/*  function returningPatientlist()
{
	 messageList.bind(null, patlist);
	
	//alert(patlist[wat].patient_id+" "+patlist[wat].originator+" "+patlist[wat].recipient+" "+patlist[wat].status+" "+patlist[wat].text);
} 
*/
	
 function myfuncprevious()
{
 	var count=0;
	

	var table_ik = document.getElementById("chav");
	var recordsintable = table_ik.rows.length-1;
	alert(table_ik.rows.length);
	var currentcount = document.getElementById("hiy").value;
	alert(currentcount);
	var toshow = (currentcount-recordsintable)-3;
	
	if(toshow < 0)
		{
		alert("No Previous Records!!");
		return;
		}
	for(var ij = table_ik.rows.length - 1; ij > 0; ij--)
	{
		alert(ij+" Deleting Row!!");
		table_ik.deleteRow(ij);
	}
	var currentnumberofrecords = patielist.length;
	
	
	
	
	while(count!=3)
		{
		
	tabBody=document.getElementById("tever");
    row=document.createElement("TR");
    cell1 = document.createElement("TD");
    cell2 = document.createElement("TD");
    cell3 = document.createElement("TD");
    cell4 = document.createElement("TD");
    cell5 = document.createElement("TD");
    textnode1=document.createTextNode(patielist[toshow].patient_id);
    textnode2=document.createTextNode(patielist[toshow].originator);
    textnode3=document.createTextNode(patielist[toshow].recipient);
    textnode4=document.createTextNode(patielist[toshow].status);
    textnode5=document.createTextNode(patielist[toshow].text);
    cell1.appendChild(textnode1);
    cell2.appendChild(textnode2);
    cell3.appendChild(textnode3);
    cell4.appendChild(textnode4);
    cell5.appendChild(textnode5);
    row.appendChild(cell1);
    row.appendChild(cell2);
    row.appendChild(cell3);
    row.appendChild(cell4);
    row.appendChild(cell5);
    tabBody.appendChild(row);
    count++;
    toshow++;
		}
	document.getElementById("hiy").value=toshow;
	
} 
 function myfunc()
 {
	var count = 0;
	var wat = document.getElementById("hiy").value;
	alert(patielist.length+" "+wat);
 	if(wat>=patielist.length)
		{
		alert("No more records");
		return;
		} 
	var table_ik = document.getElementById("chav");
	
	for(var ij = table_ik.rows.length - 1; ij > 0; ij--)
	{
		table_ik.deleteRow(ij);
	}
	
	//alert(wat);
	//alert(patielist[wat].patient_id+" "+patielist[wat].originator+" "+patielist[wat].recipient+" "+patielist[wat].status+" "+patielist[wat].text);
	while(wat<patielist.length && count<3)
		{
	tabBody=document.getElementById("tever");
    row=document.createElement("TR");
    cell1 = document.createElement("TD");
    cell2 = document.createElement("TD");
    cell3 = document.createElement("TD");
    cell4 = document.createElement("TD");
    cell5 = document.createElement("TD");
    textnode1=document.createTextNode(patielist[wat].patient_id);
    textnode2=document.createTextNode(patielist[wat].originator);
    textnode3=document.createTextNode(patielist[wat].recipient);
    textnode4=document.createTextNode(patielist[wat].status);
    textnode5=document.createTextNode(patielist[wat].text);
    cell1.appendChild(textnode1);
    cell2.appendChild(textnode2);
    cell3.appendChild(textnode3);
    cell4.appendChild(textnode4);
    cell5.appendChild(textnode5);
    row.appendChild(cell1);
    row.appendChild(cell2);
    row.appendChild(cell3);
    row.appendChild(cell4);
    row.appendChild(cell5);
    tabBody.appendChild(row);
    wat++;
    count++;
    document.getElementById("hiy").value=wat;
		}
} 

function messageList(patlist)
{	
	patielist = patlist;
	var totalpatientobjects = patlist.length;
	var i;
	
	//document.getElementById("tever").deleteRow(4);
		//alert(JSON.stringify(patlist));
			for(i=0;i<patlist.length;i++)
				{
				if(i==3)
					break;//displaying only 1 patient. Now click next to view next patient
		//alert(patlist[i].patient_id+" "+patlist[i].originator+" "+patlist[i].recipient+" "+patlist[i].status+" "+patlist[i].text);
		//tabBody=document.getElementsByTagName("tbody").item(0);
		tabBody=document.getElementById("tever");
        row=document.createElement("TR");
        cell1 = document.createElement("TD");
        cell2 = document.createElement("TD");
        cell3 = document.createElement("TD");
        cell4 = document.createElement("TD");
        cell5 = document.createElement("TD");
        textnode1=document.createTextNode(patlist[i].patient_id);
        textnode2=document.createTextNode(patlist[i].originator);
        textnode3=document.createTextNode(patlist[i].recipient);
        textnode4=document.createTextNode(patlist[i].status);
        textnode5=document.createTextNode(patlist[i].text);
        cell1.appendChild(textnode1);
        cell2.appendChild(textnode2);
        cell3.appendChild(textnode3);
        cell4.appendChild(textnode4);
        cell5.appendChild(textnode5);
        row.appendChild(cell1);
        row.appendChild(cell2);
        row.appendChild(cell3);
        row.appendChild(cell4);
        row.appendChild(cell5);
        tabBody.appendChild(row);
				}
			
			document.getElementById("hiy").value=i;
				
			
	/*  dwr.util.removeAllRows("tbodd");
	 dwr.util.addRows("tbodd",patlist,cellFunc);//for each patient/patlist, create columns in cellFunc array, so patlist[0], 5 columns
 */
}

/*  var cellFunc =
    [
      function(patlist) { return patlist[i].patient_id; },
      function(patlist) { return patlist[i].originator; },
      function(patlist) { return patlist[i].recipient; },
      function(patlist) { return patlist[i].status; },
      function(patlist) { return patlist[i].text; },
      
    ]; 
 */
</script>

<table border="1" width="56%">
<tr>
<td>
<table>
<tr>

<td>

Enter Patient ID to check status of SMS
&nbsp;
 <input id="patient_id" name="patient_id" type="text" style="width: 55mm" /></td>
 <td><input type="button" value="SEARCH" onclick="dwrReportRetrieval();" style="width: 17mm; height: 6mm;"/></td>
 <td><input type="hidden" id="hiy"/>
 <a href="#" onclick="myfuncprevious();"/>[&lt; Previous]</a>
 <a href="#" onclick="myfunc();"/>[Next &gt;]</a>
 </td>
</tr>

 </table>
</tr>
<tr>
<td>
 <%--  <%@ include file="grid.jsp"%>  --%>
 <table class="datagrid" width="100%" id="chav">
    <thead class="header">
    <tr>
						<th>Patient ID</th>
						<th>Originator</th>
						<th>Recipient</th>
						<th>Status</th>
						<th>Text</th>
						
	</tr>
    </thead>
     
     <tbody class="rows" id="tever">
     <tr>
     <td>
     
     </td>
     </tr>
     </tbody>
     
    </table>
  
</td>

</tr>
</table>
