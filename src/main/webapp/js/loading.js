$(function () {
    $.get("loading.html",function (data) {
        $("#loading_box").html(data);
    });

});