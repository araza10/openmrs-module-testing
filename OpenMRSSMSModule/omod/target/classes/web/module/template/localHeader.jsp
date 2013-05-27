<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="gridstyle.jsp" %>
<openmrs:htmlInclude file="/dwr/interface/DWRMRSPatientService.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/engine.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/util.js" ></openmrs:htmlInclude>
<%@page import="org.openmrs.Patient"%>  
<%-- <script src="<openmrs:contextPath/>/dwr/interface/DWRPatientService.js"></script> --%>
<%-- <script src="<openmrs:contextPath/>/dwr/interface/DWRMRSPatientService.js"></script> --%>

<!--  <spring:htmlEscape defaultHtmlEscape="true" /> -->
<%-- <ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/OpenMRSSMSModule/manage.form"><spring:message
				code="OpenMRSSMSModule.manage" /></a>
	</li>
	
	<!-- Add further links here -->
</ul> --%>
<!-- <h2>
	<spring:message code="" />
</h2> -->


<script type="text/javascript">

function callingdwe()
{
	var patientsearch = document.getElementById("patientid_id").value;
	alert(patientsearch);
	DWRMRSPatientService.getSearchParameter(patientsearch, { async: false,
	     callback:results});
	
}
	 function results(patients)
	 {
		//alert(JSON.stringify(patients));
		 if(patients==null)
			 {
			 	alert("Patient not registered!");
			 	
			 }
		
		 else{
			alert(patients[0].error);
			
		 dwr.util.removeAllRows("tbod");
		 dwr.util.addRows("tbod",patients,cellFunctions); 
		 }
	 }
	 
	    var cellFunctions =
	        [
	          function(patients) { return patients.patientId; },
	          function(patients) { return patients.gender; },
	          function(patients) { return patients.givenName; },
	          function(patients) { return patients.mobileNumber; },
	          function(patients) { return patients.error; },
	          
	        ];
	 

	 


	//DWRUtil.removeAllRows("tbod");
	/* for(var i=0;i<patients.length;i++)
		{
 			var patientId = patients[i].patientId;
			var gender    = patients[i].gender;
			var givenName = patients[i].givenName;
			var mobileNumb= patients[i].mobileNumber;
			//alert(i);
			
			DWRUtil.addRows("tbod", patients,objectsFound,{ escapeHtml:false }); 
			
		} */


</script>
<table border="1" width="56%">
<tr>
<td>
<table>
<tr><td>

Patient Identifier or Patient Name
&nbsp;&nbsp;&nbsp;
 <input id="patientid_id" name="patientid_id" type="text" style="width: 55mm" /></td>
 <td><input type="button" value="SEARCH" onclick="callingdwe();" style="width: 17mm; height: 6mm;"/></td>
 </tr>

 </table>
</tr>
<tr>
<td>
 <%--  <%@ include file="grid.jsp"%>  --%>
 <table class="datagrid" width="100%">
    <thead class="header">
    <tr>
						<th>Patient Identifier</th>

						<th>Gender</th>
						<th>Given Name</th>

						<th>Cell Number</th>
						
	</tr>
    </thead>
     
     <tbody class="rows" id="tbod">
     <tr>
     <td>
     
     </td>
     </tr>
     </tbody>
     
    </table>
  
</td>

</tr>
</table>
