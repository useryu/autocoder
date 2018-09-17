<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>编辑${modelRemarks}</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<meta name="MobileOptimized" content="320">
	${r'<#'}include "/common/common_styles.html">
</head>
<!-- END HEAD -->
${r'<#'}function isAdd ${modelName}>
  ${r'<#'}return ${modelName}.id==null>
${r'</#'}function>
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
					<div class="portlet box grey ">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-reorder"></i>
								${r'<#'}if isAdd(${modelName})>
								  新增${modelRemarks}
								${r'<#'}else>
								编辑${modelRemarks}
								${r'</#'}if>
							</div>
						</div>
						<div class="portlet-body form">
							<form class="form-horizontal" role="form" name="${modelName}Form" id="${modelName}Form" action="${r'<#'}if isAdd(${modelName})>${r'${base}'}/${modelName}/add${r'<#'}else>${r'${base}'}/${modelName}/edit${r'</#'}if>" method="post">
								<input type="hidden" name="id" value="${r'${'}${modelName}.id}"/>
								
								<#list fields as field>			
								
								<div class="form-body">
									<div class="form-group">
										<label class="col-md-3 control-label">${field.remarks}</label>
										<div class="col-md-9">
											<#if field.javaType=='java.util.Date'>
												<div class="input-group input-medium date date-picker" data-date-format="yyyy-mm-dd">
												<input class="form-control" readonly="" type="text" name="${field.attrName}" placeholder="选择${field.remarks}" value="${r'${'}${modelName}.${field.attrName}?date}">
												<span class="input-group-btn">
													<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
												</span>
												</div>
												<span class="help-block">选择${field.remarks}.</span>
											<#elseif field.javaType=='java.lang.Boolean'>
												<div class="make-switch" data-on-label="是" data-off-label="否">
													<input type="checkbox" name="${field.attrName}" ${r'<#if'} ${modelName}.${field.attrName}>checked${r'</#if>'} class="toggle"/>
												</div>
											<#elseif field.javaType=='java.lang.Integer'>
												<input type="text" class="form-control" name="${field.attrName}" placeholder="输入${field.remarks}" value="${r'${'}${modelName}.${field.attrName}}">
												<span class="help-block">输入${field.remarks}.</span>
											<#elseif field.javaType=='java.lang.Long'>
												<input type="text" class="form-control" name="${field.attrName}" placeholder="输入${field.remarks}" value="${r'${'}${modelName}.${field.attrName}}">
												<span class="help-block">输入${field.remarks}.</span>
											<#else>
												<input type="text" class="form-control" name="${field.attrName}" placeholder="输入${field.remarks}" value="${r'${'}${modelName}.${field.attrName}}">
												<span class="help-block">输入${field.remarks}.</span>
											</#if>
										</div>
									</div>
								</div>
								
								</#list>
								
								<#list objFields as field>
								<div class="form-group">
									<label class="control-label col-md-3">${field.remarks}</label>
									<div class="col-md-9">
										<select class="form-control select2_category" name="${field.attrName}.id">
											${r'<#list'} ${field.attrName}s as ${field.attrName}>
											<option value="${r'${'}${field.attrName}.id}" ${r'<#if'} ${field.attrName}.id==${modelName}.${field.attrName}.id>selected${r'</#if>'}>${r'${'}${field.attrName}}</option>
											${r'</#list>'}
										</select>
									</div>
								</div>
								</#list>
								
								<div class="form-actions fluid">
									<div class="col-md-offset-3 col-md-9">
										${r'<#if'} isAdd(${modelName})>
											<button type="submit" class="btn default">保存并新增下一条</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										${r'<#else>'}
											<button type="submit" class="btn default">保存并返回列表</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										${r'</#if'}>
										<button type="button" class="btn cancle default">取消</button>                              
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	${r'<#'}include "/common/bottom.html">
	<script>
		jQuery(document).ready(function() {    
			$('#${modelName}Form .cancle').live('click', function (e) {
                e.preventDefault();
                $(window.location).attr('href', '${r'${base}'}/${modelName}/list');
            });
			FormComponents.init();
            $('form').validate();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>