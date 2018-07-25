<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
  <form class="col-lg-3" method="POST" role="form" action="Myworkers.do">
    <input type="hidden" name="do" value="add">
    <input type="hidden" name="ctoken" value="{$CTOKEN|escape|default:""}">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-plus-square-o fa-fw"></i> Add New Worker
      </div>
        <div class="panel-body">
          <div class="form-group">
            <label>Worker Name</label>
            <input class="form-control" type="text" name="username" value="user" size="10" maxlength="20" required>
          </div>
          <div class="form-group">
            <label>Worker Password</label>
            <input class="form-control" type="text" name="password" value="password" size="10" maxlength="20" required>
          </div>
        </div>
      <div class="panel-footer">
        <input type="submit" value="Add New Worker" class="btn btn-success btn-sm">
      </div>
    </div>
  </form>

  <div class="col-lg-9">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-gears fa-fw"></i> Worker Configuration
      </div>
      <form method="post" role="form" action="Myworkers.do">
        <input type="hidden" name="do" value="update">
        <input type="hidden" name="ctoken" value="{$CTOKEN|escape|default:""}" />
        <div class="panel-body no-padding">
          <div class="table-responsive">
          <table class="table">
             <thead>
                <tr>
                  <th class="smallwidth">Worker Login</th>
                  <th class="smallwidth">Worker Password</th>
                  <th class="text-center">Active</th>
                  <th class="text-center">Monitor</th>
                  <th class="text-right">Khash/s</th>
                  <th class="text-right">Difficulty</th>
                  <th class="text-center">Action</th>
                </tr>
             </thead>
             <tbody>
             	<c:forEach var="worker" items="${WORKERS }" >
             	<input type="hidden" name="workers" value="${worker.id }"> 
               <tr>
                 <td>
                  <div class="input-group input-group-sm clear">
                  	<c:set var="styleVar" value=""/>
                  	<c:if test="${worker.hashrate > 0}">
                  		<c:set var="styleVar" value="color: orange"/>
                  	</c:if>
                  	<span style="${styleVar}" class="input-group-addon">${USERDATA.username }.</span>
                    <input type="text" name="${worker.id }.username" value="${worker.username}" size="10" required class="name" />
                  </div>
                 </td>
                 <td><input class="form-control" type="text" name="${worker.id }.password" value="${worker.password }" size="10" required></td>
                 <c:set var="classVar" value="fa fa-times fa-fw"/>
                 <c:if test="${worker.hashrate >0 }">
                 	<c:set var="classVar" value="fa fa-check fa-fw"/>
                 </c:if>
                 <td class="text-center"><i class="${classVar }"></i></td>
                 <td class="text-center">
                 <c:set var="checkVar" value=""/>
                 <c:if test="${worker.monitor eq 1 }">
                 	<c:set var="checkVar" value="checked"/>
                 </c:if>
                   <input type="checkbox" class="switch" data-size="mini"  name="${worker.id }.monitor" id="${worker.id }.monitor" value="1" ${checkVar }/>
                 </td>
                 <td class="text-right"><fmt:formatNumber value="${worker.hashrate}" pattern="0"/></td>
                 <td class="text-right"><fmt:formatNumber value="${worker.difficulty}" pattern="0.00"/></td>
                 <td class="text-center"><a href="Myworkers.do?do=delete&id=${worker.id}"><i class="fa fa-trash-o fa-fw"></i></a></td>
               </tr>
               </c:forEach>
             </tbody>
            </table>
          </div>
          <div class="panel-footer">
            <input type="submit" class="btn btn-success btn-sm" value="Update Workers">
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
