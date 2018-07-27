<script src="/Resources/site_assets/bootstrap/js/plugins/date.format.js"></script>
<script src="/Resources/site_assets/bootstrap/js/plugins/soundjs-0.6.0.min.js"></script>

<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-info">
      <div class="panel-heading">
        <h4 class="panel-title"><i class="fa fa-align-left fa-fw"></i> Overview</h4>
      </div>
      <div class="panel-body text-center">
        <div class="row">
          <div class="col-lg-12">
          <jsp:include page="/WEB-INF/view/Content/dashboard/overview.jsp" flush="false" />
          <jsp:include page="/WEB-INF/view/Content/dashboard/prop_default.jsp" flush="false" />
          <jsp:include page="/WEB-INF/view/Content/dashboard/account_data.jsp" flush="false" />
          <!-- {if !$DISABLED_API} -->
          <jsp:include page="/WEB-INF/view/Content/dashboard/worker_information.jsp" flush="false" />
          <!-- {/if} -->
          <jsp:include page="/WEB-INF/view/Content/dashboard/blocks.jsp" flush="false" />
          </div>
        </div>
      </div>
      <div class="panel-footer">
        <h6>Refresh interval: <!-- {$GLOBAL.config.statistics_ajax_refresh_interval|default:"10"} --> seconds, worker and account <!-- {$GLOBAL.config.statistics_ajax_long_refresh_interval|default:"10"} --> seconds. Hashrate based on shares submitted in the past <!-- {$INTERVAL|default:"5"} --> minutes.</h6>
      </div>
    </div>
  </div>
</div>
 <jsp:include page="/WEB-INF/view/Content/dashboard/js/api.jsp" flush="false" />
<!-- 
  {* Include our JS libraries, we allow a live updating JS and a static one *}
  {if !$DISABLED_DASHBOARD and !$DISABLED_DASHBOARD_API}
  {include file="dashboard/js/api.tpl"}
  {else} 
  {include file="dashboard/js/static.tpl"}
  {/if}
{/if}

DashBoard
 -->