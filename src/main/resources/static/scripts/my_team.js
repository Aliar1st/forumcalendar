$(function () {
    var teamNameField = $("#teamName");
    var editTeamNameButton = $("#editTeamName");
    var getLinkButton = $("#getLink");
    var becomeCaptainButton = $("#becomeCaptain");

    var editTeamNameForm = '#editTeamNameForm';
    var submitEditTeamNameForm = 'form[id="editTeamNameForm"]>button[type="button"]';
    var closeEditTeamNameForm = 'form[id="editTeamNameForm"]>button[type="reset"]';

    var genLinkForm = '#genLinkForm';
    var submitGenLinkForm = 'form[id="genLinkForm"]>button[type="button"]';
    var closeGenLinkForm = 'form[id="genLinkForm"]>button[type="reset"]';

    var editNameFormFlag = false;
    var genLinkFormFlag = false;

    $(editTeamNameButton).on('click', function (event) {
        if (!editNameFormFlag) {
            $.ajax({
                type: "GET",
                url: "/team/editName",
                success: function (data) {
                    $("#content").append(data);
                    editNameFormFlag = true;
                }
            });
        }
    });

    $(getLinkButton).on('click', function (event) {
        if (!genLinkFormFlag) {
            $.ajax({
                type: "GET",
                url: "/team/getLink",
                success: function (data) {
                    $("#content").append(data);
                    genLinkFormFlag = true;
                }
            });
        }
    });

    $(becomeCaptainButton).on('click', function (event) {
        $.ajax({
            type: "POST",
            url: "/team/becomeCaptain",
            success: function (data) {
                $().toastmessage('showNoticeToast', data);

            }
        });
    });

    $(document).on('click', submitGenLinkForm, function (event) {
        var form = $(genLinkForm);
        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (data) {
                $("#content").append(data);
                $(genLinkForm).remove();
                genLinkFormFlag = true;
            }
        });
    });

    $(document).on('click', submitEditTeamNameForm, function (event) {
        var newTeamName = $('form[id="editTeamNameForm"]>input[name="teamName"]').val();
        if (!$.isEmptyObject(newTeamName)) {
            var form = $(editTeamNameForm);
            $.ajax({
                type: form.attr('method'),
                url: form.attr('action'),
                data: form.serialize(),
                success: function (data) {
                    $().toastmessage('showNoticeToast', "Имя команды успешно изменено");
                    teamNameField.text(data);
                }
            });
        }
        else {
            $().toastmessage('showWarningToast', "Пустое имя команды!");
        }
    });

    $(document).on('click', closeGenLinkForm, function (event) {
        $(genLinkForm).remove();
        genLinkFormFlag = false;
    });

    $(document).on('click', closeEditTeamNameForm, function (event) {
        $(editTeamNameForm).remove();
        editNameFormFlag = false;
    });

});





