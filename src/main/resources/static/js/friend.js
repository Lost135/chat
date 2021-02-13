$(function (){
    var loginId = $.cookie("loginId");
    var loginPw= $.cookie("loginPw");
    function all() {
        $.get("friend/showFriend", {loginId: loginId}, function (data) {
            var lis1 = "";
            var lis2 = "";
            for (var i = 0; i < data.length; i++) {
                if (data[i].status == "Y" && data[i].userId == loginId) {
                    var route1 = data[i];
                    var li1 = '<tr id="' + route1.friendId + '">\n' +
                        '<td class="td_left">\n' +
                        '<a href="">' + route1.friendName + '</a>\n' +
                        '</td>\n' +
                        '<td class="td_right">\n' +
                        '<input type="button" class="submit_fre" value="删除"/>\n' +
                        '</td>\n' +
                        '</tr>';
                    lis1 += li1;
                } else if (data[i].status == "Y" && data[i].friendId == loginId) {
                    var route1 = data[i];
                    var li1 = '<tr id="' + route1.userId + '">\n' +
                        '<td class="td_left">\n' +
                        '<a href="">' + route1.friendName + '</a>\n' +
                        '</td>\n' +
                        '<td class="td_right">\n' +
                        '<input type="button" class="submit_fre" value="删除"/>\n' +
                        '</td>\n' +
                        '</tr>';
                    lis1 += li1;
                } else if (data[i].status == "N" && data[i].friendId == loginId) {
                    var route2 = data[i];
                    var li2 = '<tr id="' + route2.userId + '">\n' +
                        '<td class="td_left">\n' +
                        '<a href="">' + route2.friendName + '</a>\n' +
                        '</td>\n' +
                        '<td class="td_right">\n' +
                        '<input type="button" class="submit_fre" value="确认"/>\n' +
                        '</td>\n' +
                        '</tr>';
                    lis2 += li2;
                }
            }
            $("#friends").html(lis1);
            $("#friendReq").html(lis2);
        });
    }
    all();

    $('body').on("click","#search-button",function (){
        var friendName = $("#search_input").val();
        var li = "";
        $.post("friend/findFriend",{loginId:loginId,friendName:friendName},function (data){
            if (data.status == 'nouser'){
                li = '<p>无此用户</p>';
            }else if(data.status == 'notadd'){
                li = '<tr id="'+data.friendId+'">\n' +
                    '<td class="td_left">\n' +
                    '<p>'+data.friendName+'</p>\n' +
                    '</td>\n' +
                    '<td class="td_right" >\n' +
                    '<input type="button" class="submit_fre" value="添加"/>\n' +
                    '</td>\n' +
                    '</tr>';
            }else if (data.status == 'add'){
                li = '<p>已申请好友</p>';
            }
            $('#search').html(li);
        });
    });

    $('body').on("click",".submit_fre",function(){
            var friendId = $(this).parent().parent().attr('id');
            $.post("friend/handleFriend",{loginId:loginId,friendId:friendId},function (data){
                $("#errorMsg").html(data.errorMsg);
                <!--局部刷新-->
                all();
                $("#middleBody").load(location.href+" #middleBody>*","");
            });
    });

})
