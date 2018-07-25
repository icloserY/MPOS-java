<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-info">
			<div class="panel-heading">
				<i class="fa fa-bitbucket fa-fw"></i> Pool Donors
			</div>
			<div class="panel-body table-responsive">
				<c:set var="classVar" value="" />
				<c:if test="${DONORS != null }">
					<c:set var="classVar" value="datatable" />
				</c:if>
				<table
					class="table table-striped table-bordered table-hover ${classVar }">
					<thead>
						<tr>
							<th>Name</th>
							<th>%</th>
							<th>${currency}Total</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${DONORS != null }">
							<c:forEach var="donor" items="${DONORS}">
								<tr>
									<c:set var="donarName" value="${donor.username }" />
									<c:if
										test="${donor.is_anonymous == 0 and USERDATA.is_admin == 0 }">
										<c:set var="donarName" value="anonymous" />
									</c:if>
									<td>${donorName }</td>
									<td>${donor.donate_percent }</td>
									<td><fmt:formatNumber value="${donor.donation }"
											pattern="0.00" /></td>
								</tr>
							</c:forEach>

						</c:if>
						<c:if test="${DONORS == null }">
							<tr>
								<td colspan="3">No confirmed donations yet, please be
									patient!</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
