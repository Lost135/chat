$(function () {
    //1.给登录按钮绑定单击事件
    $(".submit").click(function () {
        //2.发送ajax请求，提交表单数据
        $.post("user/login",$("#loginForm").serialize(),function (data) {
            if(data.flag){
                //登录成功
                location.href="userDetail.html";
            }else{
                //登录失败
                $("#errorMsg").html(data.errorMsg);
            }
        });
        return false;
    });
});