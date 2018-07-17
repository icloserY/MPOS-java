<div class="row">
	<form class="col-md-4" role="form" method="POST" action="ChangePassword.do">
		<input type="hidden" name="token" value="${request.token}">
		<input type="hidden" name="do" value="resetPassword">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Password reset</h3>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<fieldset>
						<label>New Password</label> <input class="form-control" type="password" name="newPassword" required>
					</fieldset>
				</div>
				<div class="form-group">
					<fieldset>
						<label>Repeat New Password</label> <input class="form-control" type="password" name="newPassword2" required>
					</fieldset>
				</div>
			</div>
			<div class="panel-footer">
				<input type="hidden" name="cp_token" value="{$smarty.request.cp_token|escape|default:""}"> 
				<input type="hidden" name="utype" value="change_pw"> 
				<input type="submit" value="Change Password" class="btn btn-warning btn-sm">
			</div>
		</div>
	</form>
</div>
