$(function() {
    $("[directory-url]").click(function() {
        var url = $(this).attr("directory-url");
        location.href = url;
    })

    $("[file-url]").click(function() {
        var path = $(this).attr("file-url");
        var newWindow = window.open("about:blank");

        newWindow.location.href = path;
    })

    $("[content]").click(function() {
        var url = $(this).attr("content"); // content 정보를 얻고
        location.href = url;
    })

    $('#version_date').click(function() {
        var date = $('#version_input_date').value;
        var parent = $('#now_path').value;
        var url = "/folder/version?parent" + parent + "&localDateTime" + date;
        location.href = url;
    })
})

