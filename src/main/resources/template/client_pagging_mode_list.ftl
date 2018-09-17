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
					<div class="portlet box grey">
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
								<!-- 如果没有要求导出的，就不要显示了
								<div class="btn-group pull-right">
									<button class="btn dropdown-toggle" data-toggle="dropdown">工具 <i class="fa fa-angle-down"></i>
									</button>
									<ul class="dropdown-menu pull-right">
										<li><a href="#">保存为PDF</a></li>
										<li><a href="#">导出到Excel</a></li>
									</ul>
								</div>
								 -->
							</div>
							<table class="table table-striped table-hover table-bordered" id="${modelName}list_table">
								<thead>
									<tr>
									<#list fieldHeaders as fieldHeader>
										<th>${fieldHeader}</th>
									</#list>
										<th></th>
										<th></th>
									</tr>
								</thead>
								<tbody>
								${r'<#list'} ${modelName}s as ${modelName}>
									<tr >
										<#list fieldNames as fieldName>
										<td>${r'${'}${modelName}.${fieldName}}</td>
										</#list>
										<td><a class="edit" href="${r'${'}base}/${modelName}/toedit/${r'${'}${modelName}.id}">修改</a></td>
										<td><a class="delete" href="${r'${'}base}/${modelName}/delete/${r'${'}${modelName}.id}">删除</a></td>
									</tr>
								${r'</#list>'}
								</tbody>
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
		var ${modelName}List = function () {
	
		    return {
	
		        //main function to initiate the module
		        init: function () {
		            
		            var oTable = $('#${modelName}list_table').DataTable( {
		                "language": {
		                    "url": "${r'${'}base}/assets/plugins/data-tables/chinese.json"
		                }
		            } );
	
		            $('#${modelName}list_table_new').live('click', function (e) {
		                e.preventDefault();
		                $(window.location).attr('href', '${r'${'}base}/${modelName}/toadd');
		            });
	
		        }
	
		    };
	
		}();
		jQuery(document).ready(function() {    
			${modelName}List.init();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>