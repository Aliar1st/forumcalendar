$(function() {

    var selectActivity = $('select[name="activityId"]');
    var selectShift = $('select[name="shiftId"]');
    var selectTeam = $('select[name="teamId"]');
    var submitTeam = $('form[name="teamId"]>button[type="submit"]');

    selectActivity.change(function () {
        onSelectChange($(this));
    });
    selectShift.change(function () {
        onSelectChange($(this));
    });
    selectTeam.change(function () {
        if ($(this).val() != 0) {
            submitTeam.removeAttr('disabled');
        } else {
            submitTeam.attr('disabled', 'disabled');
        }
    });

    function onSelectChange(target) {
        var isActivityId = target.is(selectActivity);
        var form = isActivityId ? $('form[name="activityId"]') : $('form[name="shiftId"]');
        var selectAppend = isActivityId ? selectShift : selectTeam;

        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (data) {
                selectAppend.children(':not(:first)').remove();
                selectTeam.children(':not(:first)').remove();
                submitTeam.attr('disabled', 'disabled');

                $.each(data, function (i, item) {
                    selectAppend.append("<option value='"+i+"'>"+item+"</option>");
                });
            }
        })
    }
});