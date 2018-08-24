$(function () {

    var teamId = $('input[name="teamId"]').val();
    var teamNameField = $("#teamName");
    var editTeamNameButton = $("#editTeamName");
    var getLinkButton = $("#getLink");
    var becomeCaptainButton = $("#becomeCaptain");
    var inviteLink = $("#inviteLink");

    var editTeamNameForm = '#editTeamNameForm';


    $(document).on('click', '.kickMember', function (event) {
        var userId = $(event.currentTarget).parent().children('input[name="userId"]').val();
        $.ajax({
            type: "POST",
            url: location.href + "/kickMember?userId=" + userId,
            success: function (data) {
                $().toastmessage('showNoticeToast', data.kickedMember + " успешно удалён из команды.");
            }
        });
    });

    $(editTeamNameButton).on('click', function (event) {
        $.ajax({
            type: "GET",
            url: location.href + "/editName",
            success: function (data) {
                $(".modal").append(data);
            }
        });
    });

    $(getLinkButton).on('click', function (event) {
        var timeId = setTimeout(function () {
                $('.modal-body').html('<img id="link-preload" src="/static/images/line-load.svg"/>');
            }, 200);

        $.ajax({
            type: 'POST',
            url: '/team/getLink',
            data: 'teamRoleId=3&teamId=' + teamId,
            success: function (data) {
                $('.modal-body').html('<input id="inviteLink" class="form-control" readonly>');
                $("#inviteLink").val(data);
                new ClipboardJS('.copy-link');
                clearTimeout(timeId);
            }
        });
    });

    $(document).on('click', '.copy-link', function (event) {
        $('button[class="close"]').trigger('click');
        $().toastmessage('showNoticeToast', "Ссылка скопирована в буфер обмена");
    });


    $(getLinkButton).on('click', function (event) {

        $("#copyLinkModal").modal('show');
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


    $(document).on('click', '.modal-footer>button[type="button"]', function (event) {
        var newTeamName = $('.modal-body>input[name="teamName"]').val();
        if (!$.isEmptyObject(newTeamName)) {
            var form = $(editTeamNameForm);
            $.ajax({
                type: form.attr('method'),
                url: form.attr('action'),
                data: form.serialize(),
                success: function (data) {
                    if (data !== "") {
                        teamNameField.text(data);
                        $('button[class="close"]').trigger('click');
                        $().toastmessage('showNoticeToast', "Имя команды успешно изменено");
                    }
                    else {
                        document.location.href = location.href;
                    }
                }
            });
        }
        else {
            $().toastmessage('showWarningToast', "Пустое имя команды!");
        }
    });

    // var teamNameField = $("#teamName");
    // var editTeamNameButton = $("#editTeamName");
    // var getLinkButton = $("#getLink");
    // var becomeCaptainButton = $("#becomeCaptain");
    //
    // var editTeamNameForm = '#editTeamNameForm';
    // var submitEditTeamNameForm = 'form[id="editTeamNameForm"]>button[type="button"]';
    // var closeEditTeamNameForm = 'form[id="editTeamNameForm"]>button[type="reset"]';
    //
    // var genLinkForm = '#genLinkForm';
    // var submitGenLinkForm = 'form[id="genLinkForm"]>button[type="button"]';
    // var closeGenLinkForm = 'form[id="genLinkForm"]>button[type="reset"]';
    //
    // var editNameFormFlag = false;
    // var genLinkFormFlag = false;
    //
    //
    // $(document).on('click', '.kickMember', function (event) {
    //     var userId = $(event.currentTarget).parent().children('input[name="userId"]').val();
    //     $.ajax({
    //         type: "POST",
    //         url: location.href + "/kickMember?userId=" + userId,
    //         success: function (data) {
    //             $().toastmessage('showNoticeToast', data.kickedMember + " успешно удалён из команды.");
    //         }
    //     });
    // });
    //
    // $(editTeamNameButton).on('click', function (event) {
    //     if (!editNameFormFlag) {
    //         $.ajax({
    //             type: "GET",
    //             url: location.href + "/editName",
    //             success: function (data) {
    //                 $("#content").append(data);
    //                 editNameFormFlag = true;
    //             }
    //         });
    //     }
    // });
    //
    // $(getLinkButton).on('click', function (event) {
    //     if (!genLinkFormFlag) {
    //         $.ajax({
    //             type: "GET",
    //             url: "/team/getLink",
    //             success: function (data) {
    //                 $("#content").append(data);
    //                 genLinkFormFlag = true;
    //             }
    //         });
    //     }
    // });
    //
    // $(becomeCaptainButton).on('click', function (event) {
    //     $.ajax({
    //         type: "POST",
    //         url: "/team/becomeCaptain",
    //         success: function (data) {
    //             $().toastmessage('showNoticeToast', data);
    //         }
    //     });
    // });
    //
    // $(document).on('click', submitGenLinkForm, function (event) {
    //     var form = $(genLinkForm);
    //     $.ajax({
    //         type: form.attr('method'),
    //         url: form.attr('action'),
    //         data: form.serialize(),
    //         success: function (data) {
    //             $("#content").append(data);
    //             $(genLinkForm).remove();
    //             genLinkFormFlag = false;
    //         }
    //     });
    // });
    //
    // $(document).on('click', submitEditTeamNameForm, function (event) {
    //     var newTeamName = $('form[id="editTeamNameForm"]>input[name="teamName"]').val();
    //     if (!$.isEmptyObject(newTeamName)) {
    //         var form = $(editTeamNameForm);
    //         $.ajax({
    //             type: form.attr('method'),
    //             url: form.attr('action'),
    //             data: form.serialize(),
    //             success: function (data) {
    //                 $().toastmessage('showNoticeToast', "Имя команды успешно изменено");
    //                 teamNameField.text(data);
    //             }
    //         });
    //     }
    //     else {
    //         $().toastmessage('showWarningToast', "Пустое имя команды!");
    //     }
    // });
    //
    // $(document).on('click', closeGenLinkForm, function (event) {
    //     $(genLinkForm).remove();
    //     genLinkFormFlag = false;
    // });
    //
    // $(document).on('click', closeEditTeamNameForm, function (event) {
    //     $(editTeamNameForm).remove();
    //     editNameFormFlag = false;
    // });
});





