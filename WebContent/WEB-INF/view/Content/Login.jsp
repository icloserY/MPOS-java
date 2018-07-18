<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="row">
  <form class="col-md-4" role="form" action="Login.do" method="post" id="loginForm">
    <input type="hidden" name="ctoken" value="{$CTOKEN|escape|default:""}">
    <div class="panel panel-info">
      <div class="panel-heading">
        <i class="fa fa-sign-in fa-fw"></i> Login with existing account
      </div>
      <div class="panel-body">
        <div class="form-group"> 
          <div class="input-group input-group-sm">
            <span class="input-group-addon"><i class="fa fa-envelope-o fa-fw"></i></span>
            <input class="form-control" placeholder="E-mail" value="" name="username" type="email" autofocus required>
          </div>
          <div class="input-group input-group-sm">
            <span class="input-group-addon"><i class="fa fa-key fa-fw"></i></span>
            <input class="form-control" placeholder="Password" name="password" type="password" value="" required>
          </div>
        </div>
      </div>
      <center>
          <div class="g-recaptcha" data-sitekey="{$recaptcha_public_key}"></div>
          <script type="text/javascript" src="https://www.google.com/recaptcha/api.js"></script>
      </center>
      <div class="panel-footer">
        <input type="submit" class="btn btn-success btn-sm" value="Login" >
        <a href="Reset.do"><font size="1">Forgot your password?</font></a>
      </div>
    </div>
  </form>
</div>