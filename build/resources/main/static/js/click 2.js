$(function() {
    $("[data-url]").click(function() {
        console.log("hello" + $(this).attr("data-url"));
        var url = $(this).attr("data-url");
        location.href = url;
    })
})