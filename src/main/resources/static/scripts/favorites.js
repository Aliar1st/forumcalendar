$(function() {

    $('form[name="toggleSubscription"] button[type="button"]').click(function () {

        var form = $('form[name="toggleSubscription"]');

        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (data) {
                var button = form.children('button[type="button"]');

                if (data.status === 'ok') {
                    button.html(button.html() == 0 ? '1' : '0');
                } else {
                    button.html('error');
                }
            }
        })
    });
});