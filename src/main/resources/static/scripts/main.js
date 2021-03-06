$(function () {
    var uniqueId = $.cookie("uniqueId");
    var stompClient = null;

    $(document).ready(function () {
        connect();
    });

    $(".check-btn").click(function () {
        var mess = $('.form-control').val();
        if (mess !== "") {
            sendAdminMessage(mess);
        }
    });

    function connect() {
        var socket = new SockJS('/notification');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            // stompClient.subscribe('/successSub/'+uniqueId, function (message) {
            //     showMessage(message.body);
            // });
            stompClient.subscribe('/notify/' + uniqueId, function (eventJSON) {
                showEvent(JSON.parse(eventJSON.body));
            });
            stompClient.subscribe('/receiveAdminMess', function (message) {
                showMessage(message.body);
            });
        });
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    function sendAdminMessage(message) {
        stompClient.send("/sendAdminMess", {}, message);
        $('.form-control').val("");
        //stompClient.send("/send", {}, JSON.stringify({'name': $("#name").val()}));
    }

    function showMessage(message) {
       $().toastmessage('showNoticeToast', message);
    }

    function showEvent(event) {
        $().toastmessage('showNoticeToast', 'Скоро событие ' + event.name);
    }

    //Олино

    $('#calendar').datepicker({
        inline: true,
        firstDay: 1,
        showOtherMonths: true,
        prevText: 'пред.',
        nextText: 'след.',
        dayNamesMin: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
        monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
            'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
        onSelect: function (selectedDate) {
            location.href = "/events?date=" + selectedDate; // /news?date=2014-02-22
        }
    });

    // $('.calendar-content td').on('click', function(evt) {

    //     window.open("shedule_day.html", '_self');
    // });

    $("#ellipsisEditPersonal").on('click', function () {
        // Открыть модальное окно с id="exampleModal"
        $("#modalDopMenu").modal('show');
    });

    $("#editTeamName").on('click', function () {
        // Открыть модальное окно с id="exampleModal"
        $("#editModalTeam").modal('show');
    });

    $("#addTeam").on('click', function () {
        // Открыть модальное окно с id="exampleModal"
        $("#editModalTeam").modal('show');
    });

    $(".show-info").on('click', function () {
        // Открыть модальное окно с id="exampleModal"
        $("#infoEvent").modal('show');
    });

    $(".show-dop-info").on('click', function () {
        $(this).find(".dop-info-div").slideToggle();
        $(this).find(".div-speakers").toggle();
        $(this).find(".fa-angle-up").toggle();
        $(this).find(".fa-angle-down").toggle();
    });

    $(".show-dop-info-speaker").on('click', function () {
        $(this).parent().find(".dop-info-div").slideToggle();
        $(this).find(".fa-angle-up").toggle();
        $(this).find(".fa-angle-down").toggle();
    });


    $(".dislike-click-btn").on('click', function () {

        var mainParent = $(this).parent().parent().parent();

        var dislikeDiv = $(this).children('div');
        var likeDiv = mainParent.find('.like-click-btn').children('div');

        var dislikeDivClazz = dislikeDiv.attr('class');

        if (dislikeDivClazz === 'dislike') {
            dislikeDiv.attr('class', 'dislike-empty');
        }
        else {
            dislikeDiv.attr('class', 'dislike');
            likeDiv.attr('class', 'like-empty');
        }

        // $(this).find(".dislike-empty").toggle();
        // $(this).find(".dislike").toggle();
    });


    $(".like-click-btn").on('click', function () {

        var mainParent = $(this).parent().parent().parent();

        var likeDiv = $(this).children('div');
        var dislikeDiv = mainParent.find('.dislike-click-btn').children('div');

        var likeDivClazz = likeDiv.attr('class');

        if (likeDivClazz === 'like') {
            likeDiv.attr('class', 'like-empty');
        }
        else {
            likeDiv.attr('class', 'like');
            dislikeDiv.attr('class', 'dislike-empty');
        }
        //
        // $(this).find(".like-empty").toggle();
        // $(this).find(".like").toggle();
    });


    $(".search-icon").on('click', function () {
        $(".search-div").slideToggle();
    });

    $(".cursor-edit").on('click', function () {
        $("#infoEvent").modal('show');
    });

    $(".add-forum-shift").on('click', function () {
        $("#editModalTeam").modal('show');
    });

    // $("#inputFile").on('change', function (evt) {
    //     var tgt = evt.target || window.event.srcElement,
    //         files = tgt.files;
    //
    //     // FileReader support
    //     if (FileReader && files && files.length) {
    //         var fr = new FileReader();
    //         fr.onload = function () {
    //             document.getElementById('imageEdit').src = fr.result;
    //         }else{}
    //         fr.readAsDataURL(files[0]);
    //
    //     });

    $("#inputFile").change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#imageEdit').attr('src', e.target.result);
            }
            reader.readAsDataURL(this.files[0]);
        }
    });

    // function onFileSelected(event) {
    //     var selectedFile = event.target.files[0];
    //     var reader = new FileReader();
    //
    //     var imgtag = document.getElementById("imageEdit");
    //     imgtag.title = selectedFile.name;
    //
    //     reader.onload = function(event) {
    //         imgtag.src = event.target.result;
    //     };
    //
    //     reader.readAsDataURL(selectedFile);
    // }

});


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

$('.choosing-form-label').on('click', function () {
    $(this).children('input[type=radio]').prop("checked", true);
    $('#choosing-form').submit();
});