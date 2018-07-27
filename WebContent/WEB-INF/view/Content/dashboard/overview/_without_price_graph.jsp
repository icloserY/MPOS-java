<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="col-md-spark-2 col-sm-6">
	<div class="circle-tile fade">
		<div class="circle-tile-heading lightblue">
			<i class="fa fa-male fa-fw fa-2x"></i>
		</div>
		<div class="circle-tile-content lightblue">
			<div class="circle-tile-description text-faded">
				<p class="h5 up-more">My Hashrate</p>
				<div class="circle-tile-number text-faded up">
					<span class="overview" id="b-hashrate">${user_hashrate }</span> <span
						class="overview-mhs">
						<!--  {$GLOBAL.hashunits.personal} -->
					</span> <br> <span class="personal-hashrate-bar spark-18"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="col-md-spark-2 col-sm-6">
	<div class="circle-tile fade">
		<div class="circle-tile-heading lightblue">
			<i class="fa fa-users fa-fw fa-2x"></i>
		</div>
		<div class="circle-tile-content lightblue">
			<div class="circle-tile-description text-faded">
				<p class="h5 up-more">Pool Hashrate</p>
				<div class="circle-tile-number text-faded up">
					<span class="overview" id="b-poolhashrate">${hashrate }</span> <span
						class="overview-mhs">
						<!--  {$GLOBAL.hashunits.pool} -->
					</span> <br> <span class="pool-hashrate-bar spark-18"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="col-md-spark-2 col-sm-6">
	<div class="circle-tile fade">
		<div class="circle-tile-heading lightblue">
			<i class="fa fa-share-square fa-fw fa-2x"></i>
		</div>
		<div class="circle-tile-content lightblue">
			<div class="circle-tile-description text-faded">
				<p class="h5 up-more">My Sharerate</p>
				<div class="circle-tile-number text-faded up">
					<span class="overview" id="b-sharerate">${user_sharerate }</span> <span
						class="overview-mhs"> S/s</span> <br> <span
						class="personal-sharerate-bar spark-18"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="col-md-spark-2 col-sm-6">
	<div class="circle-tile fade">
		<div class="circle-tile-heading lightblue">
			<i class="fa fa-truck fa-fw fa-2x"></i>
		</div>
		<div class="circle-tile-content lightblue">
			<div class="circle-tile-description text-faded">
				<p class="h5 up-more">Pool Workers</p>
				<div class="circle-tile-number text-faded up">
					<span class="overview" id="b-poolworkers">${workers }</span> <br>
					<span class="pool-workers-bar spark-18"></span>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="col-md-spark-2 col-sm-6">
	<div class="circle-tile fade">
		<div class="circle-tile-heading lightblue">
			<i class="fa fa-h-square fa-fw fa-2x"></i>
		</div>
		<div class="circle-tile-content lightblue">
			<div class="circle-tile-description text-faded">
				<p class="h5 up-more">Net Hashrate</p>
				<div class="circle-tile-number text-faded up">
					<span class="overview" id="b-nethashrate">
						<c:choose>
							<c:when test="${nethashrate > 0 }">
								${nethashrate }
							</c:when>
							<c:otherwise>
								n/a
							</c:otherwise>
						</c:choose>
					</span> <span
						class="overview-mhs"> <!-- {$GLOBAL.hashunits.network} --></span> <br>
					<span class="pool-nethashrate-bar spark-18"></span>
				</div>
			</div>
		</div>
	</div>
</div>
