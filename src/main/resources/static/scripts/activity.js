$(function () {

    var addedShiftCount;

    var addShiftBtn = $('.add-shift-btn');
    var shiftContainer = $('.shift-container');
    var removeAddedShiftBtn = '.remove-added-shift';


    $(document).ready(function () {
        addedShiftCount = $('.modal-body').length;
    });


    $(document).on('click', removeAddedShiftBtn, function (event) {
        $(event.currentTarget).parent().remove();
        addedShiftCount--;
        recount();
    });

    addShiftBtn.click(function () {
        $.ajax({
            type: "POST",
            url: '/activities/getShiftForm',
            data: 'num=' + addedShiftCount,
            success: function (data) {
                shiftContainer.append(data);
                addedShiftCount++;
                recount();
            }
        });
    });

    function recount() {
        $.each($('.modal-body'), function (index) {
            $(this).children('.shift-name').text('Смена ' + (index + 1));
            $.each($(this).children('input'), function () {
                var nameAttr = $(this).attr('name');
                var res = nameAttr.replace(new RegExp('shiftForms\\[\\d\\]','g'), "shiftForms[" + index + "]");
                $(this).attr('name',res);
            });
        });
    }

});





