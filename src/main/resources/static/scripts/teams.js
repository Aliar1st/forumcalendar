$(function () {

    var shiftId = $('input[name="shiftId"]').val();
    var container = $('.modal');


    $(document).on('click', '#addTeam', function (event) {
        $.ajax({
            type: "GET",
            url: "/team/add?shiftId=" + shiftId,
            success: function (data) {
                container.html(data);
            }
        });
    });

    $(document).on('click', '.modal-footer > button[type="button"]', function (event) {
        var form = $('form[name="addTeamForm"]');
        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (data) {
                if (data !== "") {
                    container.html(data);
                }
                else {
                    document.location.href = location.href;
                }
            }
        });
    });

});





