$(function () {

    $(document).ready(function () {
        var shiftId = $('input[name="shiftId"]').val();
        var searchTeamInput = $("#team-search-input");
        var teamContainer = $('.content-personal');
        var ajaxSearchTeamUrl = '/team/search';
        var ajaxGetAllTeamsUrl = '/team/partialTeams';
        var ajaxGetAllTeamsData = 'shiftId=' + shiftId;

        var activityId = $('input[name="activityId"]').val();
        var searchSpeakerInput = $("#speaker-search-input");
        var speakerContainer = $('.speakers-container');
        var ajaxSearchSpeakerUrl = '/speaker/search';
        var ajaxGetAllSpeakersUrl = '/speaker/partialSpeakers';
        var ajaxGetAllSpeakersData = 'activityId=' + activityId;

        var searchActivityInput = $("#activity-search-input");
        var activityContainer = $('.forums-container');
        var ajaxSearchActivityUrl = '/activities/search';
        var ajaxGetAllActivitiesUrl = '/activities/partialActivities';

        if (shiftId === undefined && activityId === undefined) {
            search(searchActivityInput, activityContainer, ajaxSearchActivityUrl, ajaxGetAllActivitiesUrl, undefined);
        } else if (shiftId === undefined) {
            search(
                searchSpeakerInput, speakerContainer,
                ajaxSearchSpeakerUrl, ajaxGetAllSpeakersUrl,
                ajaxGetAllSpeakersData
            );
        } else {
            search(
                searchTeamInput, teamContainer,
                ajaxSearchTeamUrl, ajaxGetAllTeamsUrl,
                ajaxGetAllTeamsData
            );
        }

    });

    function search(searchInput, container, ajaxSearchUrl, ajaxGetAllUrl, ajaxGetAllData) {

        var start = true, tmp = "";
        $(document).on('keyup input', searchInput, function (event) {
            var query = searchInput.val();
            if (tmp !== query) {
                if (query !== "") {
                    fnDelay(GetQueryResult, query);
                }
                else if (query === "") {
                    fnDelay(GetAll);
                }
                tmp = query;
            }
            // var query = searchInput.val();
            // if (tmp !== query) {
            //     if (start) {
            //         GetQueryResult(query);
            //         start = false;
            //     }
            //     else if (query !== "") {
            //         fnDelay(GetQueryResult, query);
            //     }
            //     else if (query === "") {
            //         fnDelay(GetAll);
            //         start = true;
            //     }
            //     tmp = query;
            // }
        });

        function GetAll() {
            $.ajax({
                type: "POST",
                url: ajaxGetAllUrl,
                data: ajaxGetAllData,
                success: function (data) {
                    container.html(data);
                    searchInput.focus();
                }
            });
        }

        function GetQueryResult(query) {
            $.ajax({
                type: "POST",
                url: ajaxSearchUrl,
                data: "q=" + query + '&' + ajaxGetAllData,
                success: function (data) {
                    container.html(data);
                }
            });
        }

        var timer = 0, _SEC = 400;

        function fnDelay(func, query) {
            clearTimeout(timer);
            timer = setTimeout(function () {
                console.log(timer);
                func(query);
            }, _SEC);
        }
    }

});





