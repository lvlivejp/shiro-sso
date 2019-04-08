
$(function() {
    $("#loginBtn").on("click", function () {
        $("form").attr('action',"/auth/login");
        $("form").submit();
    });

    $("#test").on("click", function () {
        $.ajax({
            url: "/indexview"
        })
    });
});