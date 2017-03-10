Vue.component('common-menu', {
  template: '#menu',
  data: function () {
    return {
      someChildProperty: true
    }
  },
  methods: {
		//menu event methods
      handleselect:function(a,b){
      	window.location.href=a;
      }
  }
});
Vue.component('common-header', {
	  template: '#header',
	  data: function () {
		    return {
		      someChildProperty: true
		    }
	  },
	    methods: {
			//退出登录
			logout:function(){
				var _this=this;
				this.$confirm('确认退出吗?', '提示', {
					//type: 'warning'
				}).then(() => {
					toUrl(base+'/login');
				});
			}
	    }
});