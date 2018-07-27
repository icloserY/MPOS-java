<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
  <form class="col-lg-4" action="Invitations.do" method="POST" role="form">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-envelope fa-fw"></i> Invitation
      </div>
      <div class="panel-body">
        <input type="hidden" name="do" value="sendInvitation">
        <input type="hidden" name="ctoken" value="{$CTOKEN|escape|default:""}" />
        <div class="form-group">
          <label>E-Mail</label>
          <input class="form-control" type="text" name="email" value="${email}" size="30" />
        </div>
        <div class="form-group">
          <label>Message</label>
          <c:set var="messageVar" value="Please accept my invitation to this awesome pool."/>
          <c:if test="${message != null and message ne ''}">
          	<c:set var="messageVar" value="${message }"/>
          </c:if>
          <textarea class="form-control" name="message" rows="5">${messageVar}</textarea>
        </div>
      </div>
      <div class="panel-footer">
        <input type="submit" value="Invite" class="btn btn-success btn-sm">
      </div>
    </div>
  </form>

  <div class="col-lg-8">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-mail-reply fa-fw"></i> Past Invitations
      </div>
      <div class="panel-body">
      
        <div class="table-responsive">
          <table class="table table-hover">
            <thead style="font-size:13px;">
              <tr>
                <th>E-Mail</th>
                <th>Sent</th>
                <th>Activated</th>
              </tr>
            </thead>
            <tbody>
            <c:forEach var="invite" items="${INVITATIONS }">
            <tr>
            	<td>${invite.email }</td>
            	<td>${invite.time }</td>
            	<c:set var="classVar" value="cancel"/>
            	<c:if test="${invite.is_activated == 1 }">
            		<c:set var="classVar" value="ok"/>	
            	</c:if>
				<td><i class="icon-${classVar}"></i></td>
            </tr>
            </c:forEach>
            <tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
