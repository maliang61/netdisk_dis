var vue = new Vue({
    el:"#search",
    data:{
        fileList:[],
    },
    methods:{
        init:function () {
            var that = this;
            axios.get("")
                .then(function (resp) {
                    that.fileList = resp.data.data.fileList;
                }).catch(function (error) {
                console.log(error);
            });
        }
    },
    filters:{
        numFilter: function(fileSize){
            fileSize = fileSize/1024;
            return parseFloat(fileSize).toFixed(2);
        },
        dateFilter: function (fileDate) {
            return fileDate.substr(0,10);
        }
    },
    created:function () {
        this.init();
    }
});
