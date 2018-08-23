$(function () {

    $(document).on('click', 'form[name="like"] button[type="button"]', function (event) {
        var likeForm = $(event.currentTarget).parent();

        sendRequest(event);
    });

    $(document).on('click', 'form[name="dislike"] button[type="button"]', function (event) {
        sendRequest(event);
    });

    function sendRequest(event) {
        var currentForm = $(event.currentTarget).parent();

        var mainBlock = currentForm.parent().parent();

        var likeForm = mainBlock.children('.like-div').children('form[name="like"]');
        var dislikeForm = mainBlock.children('.dislike-div').children('form[name="dislike"]');

        var eventId = likeForm.children('input[type="hidden"]').val();
        var likesField = likeForm.children('.info-date-shift');
        var dislikesField = dislikeForm.children('.info-date-shift');

        $.ajax({
            type: currentForm.attr('method'),
            url: currentForm.attr('action'),
            data: "eventId=" + eventId,
            success: function (data) {
                likesField.text(data.likecount);
                dislikesField.text(data.dislikecount);
            }
        });
    }
});

