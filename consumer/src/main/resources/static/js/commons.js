var tmp = new Vue({
    el: "#vue2",
    data:{
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
    mounted() {
        var that = this;
        axios.get('http://localhost:18088/user/getUserInfo')
            .then(function (resp) {
                console.log(resp.data);
                that.userInfo = resp.data;
                console.log(that.userInfo)
            });
    }
});
