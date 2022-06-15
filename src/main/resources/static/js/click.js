$(function() {
    $("[directory-url]").click(function() {
        var url = $(this).attr("directory-url");
        location.href = url;
    })

    $("[file-url]").click(function() {
        var path = $(this).attr("file-url");
        var newWindow = window.open("about:blank");

        newWindow.location.href = path;
    }) // window 객체 하나 형성하고, location.href 로 연결해주면 된다.

    $("[content]").click(function() {
        var url = $(this).attr("content"); // content 정보를 얻고
        location.href = url;
    })

    $('#version_date').click(function() {
        var date = $('#version_input_date').value;
        var parent = $('#now_path').value;
        var url = "/folder/version?parent=" + parent + "&localDateTime=" + date;
        location.href = url;
    })

//    $('#search-key-submit').click(function() { 잠깐 프젝 제출할 때 까지 이거 지워놓자.
//        var key = document.getElementById('search-key').value
//        var path = document.getElementById('now-path').value // 한번, document 로 해보자.
//
//        if (key == null || key.length == 0) {
//            location.href = "/directories?path=" + path;
//        }
//
//        var url = "/version/histories?key=" + key;
//        var newWindow = window.open("about:blank");
//        newWindow.location.href = url;
//    })

    $('#search-key-submit').click(function() { // 하위 디렉토리의 파일을 찾는 기능
        var key = document.getElementById('search-key').value
        var path = document.getElementById('now-path').value

        if (key == null || key.length == 0) { // 잘못 입력하면 다시 돌아가기
            location.href = "/directories?path=" + path;
        }

        var url = "/all?path=" + path + "&key=" + key;
        var newWindow = window.open("about:blank");
        newWindow.location.href = url;
    })
})
