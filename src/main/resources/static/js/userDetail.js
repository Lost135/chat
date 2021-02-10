$(function () {
    var loginId = $.cookie("loginId");
    var loginPw= $.cookie("loginPw");
    $.post("user/detail",{loginId:loginId,loginPw:loginPw},function (data){
        $("#face").attr('src',data.smallPic + "?time="+(new Date()).getTime());
        $("#name").attr('placeholder',data.name);
        $("#email").attr('placeholder',data.email);
    });

    //1.给按钮绑定单击事件
    $("#faceForm").submit(function () {
        var pic = $("#pic")[0].files[0];
        var formData=new FormData();
        formData.append("pic",pic);
        formData.append("loginId",loginId);
        formData.append("loginPw",loginPw)
        //2.发送ajax请求，提交表单数据
        $.ajax({
            url: "/user/upload",
            type: "post",
            data: formData,
            processData: false, // 告诉jQuery不要去处理发送的数据
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            success: function (data) {
                $("#face").attr('src',data.re + "?time="+(new Date()).getTime());
                $("#errorMsg").html(data.errorMsg);
            }
        });
        return false;
    });

    $("#userForm").submit(function () {
        var name = $("#name").val();
        var password = $("#password").val();
        var email = $("#email").val();
        var formData=new FormData();
        formData.append("id",loginId);
        formData.append("name",name);
        formData.append("password",password);
        formData.append("email",email);
        formData.append("oldPassword",loginPw)
        //发送ajax请求，提交表单数据
        $.ajax({
            url: "/user/saveDetail",
            type: "post",
            data: formData,
            processData: false, // 告诉jQuery不要去处理发送的数据
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            success: function (data) {
                $("#errorMsg").html(data.errorMsg);
            }
        });
        return false;
    });
});

