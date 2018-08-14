var stompClient = null;

$(document).ready(function () {
    connect();

    $("#subscribe").click(function () {
        subscribe();
    });
});

var uniqueId = $.cookie("uniqueId");

function connect() {
    var socket = new SockJS('/notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/successSub/'+uniqueId, function (message) {
            showMessage(message.body);
        });
        stompClient.subscribe('/notify/'+uniqueId, function (eventJSON) {
            showEvent(JSON.parse(eventJSON.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function subscribe() {
    eventId = $('#eventId').val();
    stompClient.send("/sub/"+uniqueId, {}, eventId);
    //stompClient.send("/send", {}, JSON.stringify({'name': $("#name").val()}));
}

function showMessage(message) {
    $().toastmessage('showNoticeToast', message);
}

function showEvent(event) {
    $().toastmessage('showNoticeToast', 'Скоро событие ' + event.name);
}

// toastMessageSettings = {
//     text: message,
//     sticky: false,
//     position: 'top-right',
//     type: 'notice',
//     closeText: '',
//     close: function () {
//         console.log("toast is closed ...");
//     }
// };

// $(document).on('click', '#add_shift', function (event) {
//     $("#activity_form").append(
//         "<p>" +
//         "<label>Название" +
//         "<input th:field='*{shiftForms[0].name}' type='text'>" +
//         "<label th:if='${#fields.hasErrors('shiftForms[0].name')}'th:errors='*{shiftForms[0].name}'>error" +
//         "</label>" +
//         "</label>" +
//         "</p>" +
//         "<p>" +
//         "<label>Дата начала" +
//         "<input th:field='*{shiftForms[0].startDate}' type='date'>" +
//         "<label th:if='${#fields.hasErrors('shiftForms[0].startDate')}'" +
//         "th:errors='*{shiftForms[0].startDate}'>error" +
//         "</label>" +
//         "</label>" +
//         "</p>" +
//         "<p>" +
//         "<label>Дата конца" +
//         "<input th:field='*{shiftForms[0].endDate}' type='date'>" +
//         "<label th:if='${#fields.hasErrors('shiftForms[0].endDate')}'" +
//         "th:errors='*{shiftForms[0].endDate}'>error" +
//         "</label>" +
//         "</label>" +
//         "</p>" +
//         "<p>" +
//         "<label th:if='${#fields.hasErrors('shiftForms[0]')}'" +
//         "th:errors='*{shiftForms[0]}'>error" +
//         "</label>" +
//         "</p>"
//     );
//     alert('fsdf');
// });