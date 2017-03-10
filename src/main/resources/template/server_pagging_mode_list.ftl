<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>${modelRemarks}列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<meta name="MobileOptimized" content="320">
	${r'<#'}include "/common/common_styles.html">
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	${r'<#'}include "/common/header.html">
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar navbar-collapse collapse">
		${r'<#'}include "/common/menu.html">
		</div>
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->
		<div class="page-content">
			<div class="clearfix"></div>
			
			<div class="row">
				<div class="col-md-12">
					<!-- BEGIN EXAMPLE TABLE PORTLET-->
					<div class="portlet box grey" id="${modelName}Box">
						<div class="portlet-title">
							<div class="caption"><i class="fa fa-edit"></i>${modelRemarks}管理</div>
						</div>
						<div class="portlet-body">
							<div class="table-toolbar">
								<div class="btn-group">
									<button id="${modelName}list_table_new" class="btn grey">
									新增 <i class="fa fa-plus"></i>
									</button>
								</div>
								<div class="btn-group pull-right">
									<button class="btn grey" v-on:click="toggleAdvSearch">
									<span  v-if="showAdvSearch">关闭高级搜索<i class="fa fa-minus"></i></span><span  v-if="!showAdvSearch">打开高级搜索<i class="fa fa-plus"></i></span> 
									</button>
									&nbsp;&nbsp;
									<!-- 
									<button class="btn dropdown-toggle" data-toggle="dropdown">工具 <i class="fa fa-angle-down"></i>
									</button>
									<ul class="dropdown-menu pull-right">
										<li><a href="#">保存为PDF</a></li>
										<li><a href="#">导出到Excel</a></li>
									</ul>
									 -->
								</div>
							</div>
							<div class="portlet box grey" id="advSearchBox" v-show="showAdvSearch">
							<div class="portlet-title">
							<div class="caption"><i class="fa fa-search"></i>高级搜索</div>
							</div>
							<div class="portlet-body">
								<div class="row">
									<div class="col-md-1">
										<label class="control-label"></label>
									</div>
									<div class="col-md-3">
										<label class="control-label">字段</label>
									</div>
									<div class="col-md-3">
										<label class="control-label">操作</label>
									</div>
									<div class="col-md-3">
										<label class="control-label">数值</label>
									</div>
									<div class="col-md-2">
										<label class="control-label"></label>
									</div>
								</div>
								<div class="form-body">
								<div class="row" v-for="searchItem in searchItems">
								<div class="form-group">
									<div class="col-md-1">
										<label class="control-label" v-if="searchItem.showAnd">并且</label>
									</div>
									<div class="col-md-3">
										<select class="form-control" v-model="searchItem.field" v-on:change="changeField(searchItem,this)">
										<#list fields as field>
											<option value="${field.attrName}" datatype="${field.javaType}">${field.remarks}</option>
										</#list>
										</select>
									</div>
									<div class="col-md-3">
										<select class="form-control" v-model="searchItem.oper">
											<option value="gt">大于</option>
											<option value="ge">大于等于</option>
											<option value="eq">等于</option>
											<option value="lt">小于</option>
											<option value="le">小于等于</option>
										</select>
									</div>
									<div class="col-md-3">
										<input class="form-control"  v-model="searchItem.value" type="text" v-show="searchItem.type=='text'">
										<div class="input-group input-medium date date-picker" data-date-format="yyyy-mm-dd" v-show="searchItem.type=='date'">
											<input class="form-control" readonly="" type="text" v-model="searchItem.value">
											<span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
											</span>
										</div>
									</div>
									<div class="col-md-2">
										<button class="btn default" v-on:click="removeSearchItem(searchItem)" v-if="searchItem.showRemove">
										删除 <i class="fa fa-minus"></i>
										</button>
										<span v-if="searchItem.showRemove">&nbsp;&nbsp;</span>
										<button class="btn default" v-on:click="addSearchItem">
										增加<i class="fa fa-plus"></i>
										</button>
									</div>
								</div>
								</div>
								</div>
								<div class="row">
									<div class="col-md-4">
										<label class="control-label"></label>
									</div>
									<div class="col-md-3">
										<button class="btn default" v-on:click="submitSearch">
										检索 <i class="fa fa-search"></i>
										</button>
										<button class="btn default" v-on:click="clearSearch">
										清除 <i class="fa fa-undo"></i>
										</button>
									</div>
									<div class="col-md-5">
										<label class="control-label"></label>
									</div>
								</div>
							</div>
							</div>
							<table class="table table-striped table-hover table-bordered" id="${modelName}list_table">
								<thead>
									<tr>
									${r'<#'}list headers as header>
										<th>${r'${'}header}</th>
									${r'</#'}list>
										<th></th>
										<th></th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<!-- END EXAMPLE TABLE PORTLET-->
				</div>
			</div>
		</div>
		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	${r'<#'}include "/common/bottom.html">
	<script>
		var oTable;
		var advSearch="";
		var ${modelName}List = function () {
	
		    return {
	
		        //main function to initiate the module
		        init: function () {
		            
		            oTable = $('#${modelName}list_table').DataTable( {
		                "language": {
		                    "url": "${r'${base}'}/assets/plugins/data-tables/chinese.json"
		                },
		                "searching": true,
		                "searchDelay": 1000*2, //in ms
		                "processing": true,
		                "serverSide": true,
		                "ajax": {
		                	url:"${r'${base}'}/${modelName}/list.json",
		                	type: "post",
		                	"data": function ( d ) {
		                        //添加额外的参数传给服务器
		                        d.advSearch = advSearch;
		                    }
		                },
		                "columnDefs": [{
		                    "render": function(data, type, row) {
		                        return data + ' (' + 1 + ')';
		                    },
		                    "targets": 0
		                },
		                {
		                    "visible": false,
		                    "targets": []
		                }]
		            } );
	
		            $('#${modelName}list_table_new').live('click', function (e) {
		                e.preventDefault();
		                $(window.location).attr('href', '${r'${base}'}/${modelName}/toadd');
		            });
	
		        }
	
		    };
	
		}();
		jQuery(document).ready(function() {
			advSearchBox = new Vue({
				el: '#${modelName}Box',
				data: {
					searchItems: [{"field":"","oper":"eq","value":"","showAnd":false,"showRemove":false,"type":"text"}],
					showAdvSearch: false
				},
			    ready: function () {      
			    	FormComponents.init();
				},
				methods: {

					addSearchItem: function () {
						this.searchItems.push({"field":"","oper":"eq","value":"","showAnd":true,"showRemove":true,"type":"text"});
						window.setTimeout(function(){
							FormComponents.init();
						},1000)
					},

					removeSearchItem: function (searchItem) {
						if(this.searchItems.length>1){
							this.searchItems.$remove(searchItem);
						}
					}, 
					submitSearch: function() {
						var haveNull = false;
						this.searchItems.forEach(function(item){
						    if(item.field=="" || item.value==""){
						    	alert("字段或数值不能为空");
						    	haveNull = true;
						    }
						})  
						if(!haveNull){
							advSearch = JSON.stringify(this.searchItems);
							oTable.search("").draw();
						}
					}, 
					clearSearch: function() {
						advSearch = "";
						this.searchItems = [{"field":"","oper":"eq","value":"","showAnd":false,"showRemove":false,"type":"text"}]; 
						oTable.search("").draw();
					},
					toggleAdvSearch: function() {
						this.showAdvSearch = !this.showAdvSearch;
						FormComponents.init();
						return false;
					},
					changeField: function(searchItem,ele) {
						var selectEle = ele.$event.target;
						var selectOption = $(selectEle).find("option:selected");
						var type = selectOption.attr("datatype");
						if(type=="java.util.Date"){
							searchItem.type="date"
						}else{
							searchItem.type="text"
						}
						return false;
					}
				}
			});
			${modelName}List.init();   
			FormComponents.init();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>