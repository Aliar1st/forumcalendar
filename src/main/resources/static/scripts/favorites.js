$(function () {

    $(document).on('click', 'form[name="toggleSubscription"] button[type="button"]', function (event) {

        var uniqueId = $.cookie("uniqueId");
        var form = $(event.currentTarget).parent();
        var eventId = form.children('input[name="eventId"]').val();

        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: "eventId=" + eventId + "&uniqueId=" + uniqueId,
            success: function (data) {

                var button = form.children('button[type="button"]');

                if (data.status === 'error') {
                    button.html('error');
                }
                else {
                    if (data.isSubscribe) {
                         button.html('<img class="back-icon" src="/static/images/star.svg"/>');
                        // $().toastmessage('showNoticeToast', "Вы подписались на событие " + data.eventName);
                    }
                    else {
                         button.html('<img class="back-icon" src="/static/images/star-empty.svg"/>');
                        // $().toastmessage('showNoticeToast', "Вы отписались от события " + data.eventName);
                    }
                }
            }
        });
    });
});

