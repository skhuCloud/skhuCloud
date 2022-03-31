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
    title: 'Version 별 코드량',
hAxis: {
    title: '시간',
},
vAxis: {
    title: '코드량\n(Byte)'
}
};

var chart = new google.visualization.ColumnChart(
                document.getElementById('chart_div'));
chart.draw(data, options);
}