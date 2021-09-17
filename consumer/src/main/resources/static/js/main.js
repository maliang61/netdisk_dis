
var vue = new Vue({
    el:"#app",
    data:{
        fileList:[],
        fileUrl:"",
        userInfo:{
            uuid:null,
            userName:null,
            nickName:null,
            password:null,
            email:null,
            superUser:null,
            registrationDate:null
        }
    },
    methods:{
        init:function () {
            var that = this;
            axios.get("http://localhost:18083/file/getAllFile")
                .then(function (resp) {
                    that.fileList = resp.data.data.fileList;
                }).catch(function (error) {
                console.log(error);
            });
        },
        deleteFile:function (fileName) {
            if (confirm("你确定要删除?")){
                console.log(fileName)
                axios.post('http://localhost:18083/file/deleteFile?fileName='+fileName).then(res=>{
                    console.log(res);
                    this.init();
                })
            }
            ``
        },
        sub:function () {

        },
        download:function(fileName){
            var $eleForm = $("<form method='post'></form>");
            $eleForm.attr("action","http://localhost:18083/file/download?fileName="+fileName);
            $(document.body).append($eleForm);
            $eleForm.submit();
            //axios.post('http://localhost:18088/file/download?fileName='+fileName)
        },
        share:function(fileName){
            console.log(fileName);
            axios.post('http://localhost:18083/file/share?fileName='+fileName).then(res=>{
                this.fileUrl = res.data.fileUrl;
                console.log("success");
                let transfer = document.createElement('input');
                document.body.appendChild(transfer);
                transfer.value = this.fileUrl;  // 这里表示想要复制的内容
                transfer.select();
                if (document.execCommand('copy')) {
                    document.execCommand('copy');
                }
                transfer.blur();
                console.log('复制成功');
                document.body.removeChild(transfer);
                alert(this.fileUrl+"链接已复制到剪切板");

            })

        },
        logOut:function () {
            axios.post('http://localhost:18083/user/logout').then(res=>{
                console.log("");
                location.href = "index.html";
            })
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
    },
    mounted() {
        var that = this;
        axios.get('http://localhost:18083/user/getUserInfo')
            .then(function (resp) {
                console.log(resp.data);
                that.userInfo = resp.data;
                console.log(that.userInfo)
            });
    }
});
layui.use(['upload', 'element', 'layer'], function(){
    var $ = layui.jquery
        ,upload = layui.upload
        ,element = layui.element
        ,layer = layui.layer;

    //演示多文件列表
    var uploadListIns = upload.render({
        elem: '#testList'
        ,elemList: $('#demoList') //列表元素对象
        ,url: 'http://localhost:18083/file/upload'
        ,accept: 'file'
        ,multiple: true
        ,number: 3
        ,auto: false
        ,bindAction: '#testListAction'
        ,choose: function(obj){
            var that = this;
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function(index, file, result){
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td>'+ file.name +'</td>'
                    ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
                    ,'<td><div class="layui-progress" lay-filter="progress-demo-'+ index +'"><div class="layui-progress-bar" lay-percent=""></div></div></td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function(){
                    delete files[index]; //删除对应的文件
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                });

                that.elemList.append(tr);
                element.render('progress'); //渲染新加的进度条组件
            });
        }
        ,done: function(res, index, upload){ //成功的回调
            console.log(res);
            var that = this;
            if(res.code == 1){ //上传成功
                var tr = that.elemList.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(3).html(''); //清空操作
                delete this.files[index]; //删除文件队列已经上传成功的文件
                return;
            }
            this.error(index, upload);
        }
        ,allDone: function(obj){ //多文件上传完毕后的状态回调
            console.log(obj);
            location.href = "main.html"
        }
        ,error: function(index, upload){ //错误回调
            var that = this;
            var tr = that.elemList.find('tr#upload-'+ index)
                ,tds = tr.children();
            tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
        ,progress: function(n, elem, e, index){ //注意：index 参数为 layui 2.6.6 新增
            element.progress('progress-demo-'+ index, n + '%'); //执行进度条。n 即为返回的进度百分比
        }
    });


});
layui.use('table', function(){
    var table = layui.table;
    var i = 1;
    table.render({
        elem: '#demo'
        ,url:'http://localhost:18083/file/getAllFileByPage'
        ,methods: 'get'
        ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
            layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
            //,curr: 5 //设定初始在第 5 页
            ,groups: 5 //只显示 1 个连续页码
            ,first: false //不显示首页
            ,last: false //不显示尾页
        }
        ,cols:[
            [
                {field:'id', width:80, title: '序号', sort: true,type:'numbers'}
                ,{field:'fileName', width:400, align: 'center',title: '文件名'}
                ,{field:'fileSize', width:100, title: '文件大小'}
                ,{field:'fileDate', width:120, align: 'center',title: '上传时间'}
                ,{field:'op', align: 'center',title: '操作',toolbar: '#toolBar'}
            ]
        ]
        ,parseData: function (res) {
            //将原始数据解析成 table 组件所规定的数据
            console.log(res.count);
            return {
                "code": res.code, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.count, //解析数据长度
                "data": res.data //解析数据列表
            };
        }
        ,request: {
            pageName: 'pageNum' // 页码的参数名称，默认：page
            ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
        },
    });
    table.on('tool(test)',function (obj) {
        var data = obj.data;
        var e = obj.event;
        var fileUrl;
        if (e == 'download'){
            var $eleForm = $("<form method='post'></form>");
            $eleForm.attr("action","http://localhost:18083/file/download?fileName="+data.fileName);
            $(document.body).append($eleForm);
            $eleForm.submit();
        }else if (e == 'share'){
            axios.post('http://localhost:18083/file/share?fileName='+data.fileName).then(res=>{
                this.fileUrl = res.data.fileUrl;
                console.log("success");
                let transfer = document.createElement('input');
                document.body.appendChild(transfer);
                transfer.value = this.fileUrl;  // 这里表示想要复制的内容
                transfer.select();
                if (document.execCommand('copy')) {
                    document.execCommand('copy');
                }
                transfer.blur();
                console.log('复制成功');
                document.body.removeChild(transfer);
                alert(this.fileUrl+"链接已复制到剪切板");

            })
        }else if (e == 'delete'){
            if (confirm("你确定要删除?")){
                console.log(data.fileName)
                axios.post('http://localhost:18083/file/deleteFile?fileName='+data.fileName).then(res=>{
                    console.log(res);
                    location.href = "main.html";
                })
            }
        }

    })
});
layui.use('element', function(){
    var $ = layui.jquery
        ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
    var timer = setInterval(function (){ //进度条方法查看进度
        $.ajax({
            url: 'http://localhost:18083/file/getFileSize',
            success: function (data) {
                //动态设置百分比
                console.log(data.present);
                var percent = data.present;
                $('.layui-progress-bar').attr('lay-percent', percent);
                element.init();
                element.progress('progress', percent)
                if(percent == "100%"){
                    clearInterval(timer); //进度到100%，注意关闭定时器
                }
            },
            error: function (e) {
            }
        });
    });
    //触发事件
    // var active = {
    //     setPercent: function(){
    //         //设置50%进度
    //         element.progress('demo', '30%')
    //     }
    //     ,loading: function(othis){
    //         var DISABLED = 'layui-btn-disabled';
    //         if(othis.hasClass(DISABLED)) return;
    //
    //         //模拟loading
    //         var n = 0, timer = setInterval(function(){
    //             n = n + Math.random()*10|0;
    //             if(n>100){
    //                 n = 100;
    //                 clearInterval(timer);
    //                 othis.removeClass(DISABLED);
    //             }
    //             element.progress('demo', n+'%');
    //         }, 300+Math.random()*1000);
    //
    //         othis.addClass(DISABLED);
    //     }
    // };
    //
    // $('.site-demo-active').on('click', function(){
    //     var othis = $(this), type = $(this).data('type');
    //     active[type] ? active[type].call(this, othis) : '';
    // });
});
