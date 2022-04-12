google.charts.load('current', {packages: ['corechart', 'bar']}); // google chart load 하는 구문
google.charts.setOnLoadCallback(drawBasic); // function 등록

function drawBasic() {
// 결국 여기서 data 를 가져올 때, array 를 주소값을 가져온다 , 이것을 어떻게 고칠 수 있을까?
var timeValue = $('#time').text();
var codeValue = $('#code').text();

var time = timeValue.substring(1 , timeValue.length - 1).split(','); // 파싱을 해보자.
var code = codeValue.substring(1 , codeValue.length - 1).split(','); // 파싱을 해보자.

var data = new google.visualization.DataTable();

data.addColumn('string', '시간');
data.addColumn('number', '코드량\n(Byte)');

var dataRow = []; // 모르고 dataRow array로 선언안해서 지금까지 안됐었던 거였음
for(var i = 0; i < time.length; i++){
    dataRow = [time[i] , Number(code[i])];
    data.addRow(dataRow);
}

var options = {
//    width : 500,
    height : 700,
    titleTextStyle: {
        color : '#fbfbfe',
    },
    title: 'Version 별 코드량',
    backgroundColor: '#1E1E1E',
    is3D: true ,
    hAxis: {
        textStyle: {
            color : '#fbfbfe',
        },
        title: '시간',
        titleTextStyle: {
            color : '#fbfbfe'
        },
    },
    vAxis: {
        textStyle: {
            color : '#fbfbfe',
        },
        title: '코드량\n(Byte)',
        titleTextStyle: {
            color : '#fbfbfe'
        },
    },
    legend: {
        textStyle: {
            color : '#fbfbfe'
        },
    }
};

var chart = new google.visualization.ColumnChart(
                document.getElementById('chart_div'));
chart.draw(data, options);
}