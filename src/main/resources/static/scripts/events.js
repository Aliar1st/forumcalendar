$(function () {

    $(document).on('click', 'form[name="like"] button[type="button"]', function (event) {
        sendRequest(event);
    });

    $(document).on('click', 'form[name="dislike"] button[type="button"]', function (event) {
        sendRequest(event);
    });

    function sendRequest(event) {
        var form = $(event.currentTarget).parent();
        var eventId = form.children('input[type="hidden"]').val();
        var likesField = form.parent().parent().children('label[class="likes"]');
        var dislikesField = form.parent().parent().children('label[class="dislikes"]');

        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: "eventId=" + eventId,
            success: function (data) {
                likesField.text(data.likecount);
                dislikesField.text(data.dislikecount);
            }
        });
    }
});

